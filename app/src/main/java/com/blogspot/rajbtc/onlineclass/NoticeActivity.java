package com.blogspot.rajbtc.onlineclass;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.blogspot.rajbtc.onlineclass.adapter.NoticeAdapter;
import com.blogspot.rajbtc.onlineclass.dataclass.NoticeData;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class NoticeActivity extends AppCompatActivity {
    private FloatingActionButton fab;
    private RecyclerView noticeRecy;
    private DatabaseReference fireRef= FirebaseDatabase.getInstance().getReference("Data");
    private String adminID,adminPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        initView();
        checkAdminOrUser();
        try {
            loadFireData();
        }
        catch (Exception e){
            Toast.makeText(getApplicationContext(),"Data loading failed!",Toast.LENGTH_SHORT).show();
        }
    }

    void initView(){
        noticeRecy=findViewById(R.id.noticeRecy);
        noticeRecy.setHasFixedSize(true);
        noticeRecy.setLayoutManager(new LinearLayoutManager(this));
        
        fab=findViewById(R.id.noticeFab);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                            upToDb();
            }
        });
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
            adminID= FirebaseAuth.getInstance().getCurrentUser().getEmail();
            adminPass=getSharedPreferences("userData",MODE_PRIVATE).getString("passForUser","null");
            if(adminPass.equals("null")){
                Toast.makeText(getApplicationContext(),"Please login again!",Toast.LENGTH_LONG).show();
                FirebaseAuth.getInstance().signOut();
                finish();
            }
        }



    }

    void upToDb(){

        final LinearLayout linearLayout=new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        final EditText slideNameEt=new EditText(this);
        slideNameEt.setHint("Notice");
        slideNameEt.setInputType(InputType.TYPE_TEXT_VARIATION_LONG_MESSAGE);


        linearLayout.addView(slideNameEt);


        AlertDialog.Builder builder=new AlertDialog.Builder(NoticeActivity.this);
        builder.setView(linearLayout).setTitle("Input")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        String slideName=slideNameEt.getText().toString();

                        if(slideName.equals("")){
                            Toast.makeText(getApplicationContext(),"Please fulfil all input",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        String date = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
                        String time = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());

                        NoticeData data=new NoticeData(null,slideName,time,date);

                        fireRef.child(adminID.replace('.','_').replace("@","__")+adminPass).child("notice").push().setValue(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getApplicationContext(),"Uploaded!",Toast.LENGTH_SHORT).show();
                            }
                        });




                    }
                }).setNegativeButton("Cancel",null);

        AlertDialog alertDialog=builder.create();
        alertDialog.show();

    }

    void loadFireData(){
        final ArrayList<NoticeData> arrayList=new ArrayList<>();
        arrayList.add(null);
        noticeRecy.setAdapter(null);
        fireRef.child(adminID.replace('.','_').replace("@","__")+adminPass).child("notice").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                NoticeData val=snapshot.getValue(NoticeData.class);
                val.setReference(snapshot.getRef());
                arrayList.add(val);
                noticeRecy.setAdapter(new NoticeAdapter(NoticeActivity.this,arrayList));
                //noticeRecy.scrollToPosition(arrayList.size()-1);
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