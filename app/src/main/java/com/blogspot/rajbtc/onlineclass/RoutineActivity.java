package com.blogspot.rajbtc.onlineclass;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.blogspot.rajbtc.onlineclass.adapter.ClassAdapter;
import com.blogspot.rajbtc.onlineclass.dataclass.ClassData;
import com.blogspot.rajbtc.onlineclass.dataclass.NoticeData;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class RoutineActivity extends AppCompatActivity {
    private RecyclerView classRecy;
    private FloatingActionButton fab;
    private DatabaseReference fireRef=FirebaseDatabase.getInstance().getReference("Data");
    private TextView dayTv;

    private String adminID,adminPass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routine);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        classRecy=findViewById(R.id.routine_subRecy);

        dayTv=findViewById(R.id.routine_dayTv);
        classRecy.setHasFixedSize(true);
        classRecy.setLayoutManager(new LinearLayoutManager(this));
        fab=findViewById(R.id.routine_plusFlb);
        initClickFunc();
        checkAdminOrUser();


        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        String day=new SimpleDateFormat("EEEE", Locale.ENGLISH).format(date.getTime());
        Log.e("====Log",day);
        loadFireData(day);


    }


    void checkAdminOrUser(){
        String userType=getSharedPreferences("userData",MODE_PRIVATE).getString("userType","null");
        if(userType.equals("null")){
            Toast.makeText(getApplicationContext(),"Please login again!",Toast.LENGTH_LONG).show();
            finish();
        }
        else if(userType.toLowerCase().equals("user")){
            adminID=getSharedPreferences("adminInfo",MODE_PRIVATE).getString("classID","null");
            adminPass=getSharedPreferences("adminInfo",MODE_PRIVATE).getString("classPass","null");
            if(adminID.equals("null") || adminPass.equals("")){
                Toast.makeText(getApplicationContext(),"Please login again!",Toast.LENGTH_LONG).show();
                finish();
            }
            fab.hide();
        }

        else {
            adminID=FirebaseAuth.getInstance().getCurrentUser().getEmail();
            adminPass=getSharedPreferences("userData",MODE_PRIVATE).getString("passForUser","null");
            if(adminPass.equals("null")){
                Toast.makeText(getApplicationContext(),"Please login again!",Toast.LENGTH_LONG).show();
                FirebaseAuth.getInstance().signOut();
                finish();
            }
        }



    }



    void initClickFunc(){


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    uploadData();

            }
        });




        findViewById(R.id.routine_sunBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                loadFireData("Sunday");
            }
        });



        findViewById(R.id.routine_monBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loadFireData("Monday");
            }
        });



        findViewById(R.id.routine_tueBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loadFireData("Tuesday");
            }
        });



        findViewById(R.id.routine_wedBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loadFireData("Wednesday");
            }
        });



        findViewById(R.id.routine_thusSunBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFireData("Thursday");
            }
        });



        findViewById(R.id.routine_friBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFireData("Friday");
            }
        });


        findViewById(R.id.routine_satBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFireData("Saturday");

            }
        });


    }

    private void uploadData() {
        final View view= LayoutInflater.from(RoutineActivity.this).inflate(R.layout.dialog_class_input,null);
        AlertDialog.Builder builder=new AlertDialog.Builder(RoutineActivity.this);
        builder.setView(view).setTitle("Input")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String className=((TextView)view.findViewById(R.id.dialog_ClassInput_clsNameEt)).getText().toString();
                        String day=((Spinner)view.findViewById(R.id.dialog_ClassInput_daySpn)).getSelectedItem().toString();
                        String endTime=((TextView)view.findViewById(R.id.dialog_ClassInput_endTimeEt)).getText().toString();
                        String startTime=((TextView)view.findViewById(R.id.dialog_ClassInput_startTimeEt)).getText().toString();
                        String link=((TextView)view.findViewById(R.id.dialog_ClassInput_linkEt)).getText().toString();
                        String teacher=((TextView)view.findViewById(R.id.dialog_ClassInput_teacherNameEt)).getText().toString();

                        if(className.equals("") || day.equals("") || endTime.equals("") || startTime.equals("") || link.equals("") || teacher.equals("")){
                            Toast.makeText(getApplicationContext(),"Please fulfil all input",Toast.LENGTH_SHORT).show();
                            return;
                        }

                        String date = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
                        String time = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());


                        ClassData classUpData=new ClassData(null,className,day,teacher,startTime,endTime,time+" "+date,link);
                        NoticeData noticeData=new NoticeData(null,"Your "+className+" routine has been uploaded!",time,date);

                        fireRef.child(adminID.replace('.','_').replace("@","__")+adminPass).child("routine").child(day).push().setValue(classUpData);
                        fireRef.child(adminID.replace('.','_').replace("@","__")+adminPass).child("notice").push().setValue(noticeData);




                    }
                }).setNegativeButton("Cancel",null);

        AlertDialog alertDialog=builder.create();
        alertDialog.show();
    }


    void loadFireData(final String day){
        dayTv.setText(day);
        final ArrayList<ClassData> arrayList=new ArrayList<>();
        classRecy.setAdapter(null);
        fireRef.child(adminID.replace('.','_').replace("@","__")+adminPass).child("routine").child(day).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                ClassData classData=snapshot.getValue(ClassData.class);
                classData.setDocID(snapshot.getRef());
                arrayList.add(classData);
                classRecy.setAdapter(new ClassAdapter(RoutineActivity.this,arrayList,fireRef.child(adminID.replace('.','_').replace("@","__")+adminPass).child("notice")));
                classRecy.scrollToPosition(arrayList.size()-1);
            }


            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}