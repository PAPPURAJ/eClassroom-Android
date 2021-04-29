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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.blogspot.rajbtc.onlineclass.adapter.VideoAdapter;
import com.blogspot.rajbtc.onlineclass.dataclass.VideoDataclass;
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

public class VideoLectureActivity extends AppCompatActivity {
    private FloatingActionButton fab;
    private RecyclerView noticeRecy;;
    private Spinner subSpn;
    private DatabaseReference fireRef= FirebaseDatabase.getInstance().getReference("Data");
    private String adminID,adminPass;
    private ArrayList<String> subArraylist;
    private ArrayList<VideoDataclass> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videolecture);
        initView();
        checkAdminOrUser();
        loadFireData();
    }

    void initView(){
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        subSpn=findViewById(R.id.videoLectureSubSpn);
        noticeRecy=findViewById(R.id.videoLectureRecy);
        noticeRecy.setHasFixedSize(true);
        noticeRecy.setLayoutManager(new LinearLayoutManager(this));

        fab=findViewById(R.id.videoLectureFab);

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

        final EditText videoNameEt=new EditText(this);
        videoNameEt.setHint("Video name");
       // final EditText subjectEt=new EditText(this);
        //subjectEt.setHint("Subject name");
        final AutoCompleteTextView subjectAutoTv=new AutoCompleteTextView(this);
        subjectAutoTv.setAdapter(new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,subArraylist));
        subjectAutoTv.setHint("Subject name");
        subjectAutoTv.setThreshold(1);

        final EditText teacherEt=new EditText(this);
        teacherEt.setHint("Teacher name");

        final EditText linkEt=new EditText(this);
        linkEt.setHint("URL");
        linkEt.setInputType(InputType.TYPE_TEXT_VARIATION_URI);

        linearLayout.addView(videoNameEt);
        linearLayout.addView(subjectAutoTv);
        linearLayout.addView(teacherEt);
        linearLayout.addView(linkEt);


        AlertDialog.Builder builder=new AlertDialog.Builder(VideoLectureActivity.this);
        builder.setView(linearLayout).setTitle("Input")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {



                        String videoName=videoNameEt.getText().toString();
                        String sub=subjectAutoTv.getText().toString();
                        String teach=teacherEt.getText().toString();
                        String link=linkEt.getText().toString().replace(" ","");

                        if(videoName.equals("") || sub.equals("")  || teach.equals("") || link.equals("")){
                            Toast.makeText(getApplicationContext(),"Please fulfil all input",Toast.LENGTH_SHORT).show();
                            return;
                        }

                        String date = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
                        String time = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());


                        VideoDataclass data=new VideoDataclass(null,videoName,sub,teach,link,date,time);

                        fireRef.child(adminID.replace('.','_').replace("@","__")+adminPass).child("video").push().setValue(data).addOnSuccessListener(new OnSuccessListener<Void>() {
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

        subArraylist=new ArrayList<>();
        subArraylist.add("All");
        arrayList=new ArrayList<>();
        noticeRecy.setAdapter(null);


        fireRef.child(adminID.replace('.','_').replace("@","__")+adminPass).child("video").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                VideoDataclass val=snapshot.getValue(VideoDataclass.class);
                if(!subArraylist.contains(val.getSubjectName()))
                    subArraylist.add(val.getSubjectName());

                val.setReference(snapshot.getRef());
                arrayList.add(val);
                setupSubSelectSpn();
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







    void setupSubSelectSpn(){
        subSpn.setAdapter(new ArrayAdapter<String>(getApplicationContext(),R.layout.support_simple_spinner_dropdown_item,subArraylist));
        subSpn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ArrayList<VideoDataclass> temp=new ArrayList<>();
                if(position==0){
                    ArrayList<VideoDataclass> ar=new ArrayList<>();
                    ar.add(null);
                    for(int i=0;i<arrayList.size();i++)
                        ar.add(arrayList.get(i));

                    noticeRecy.setAdapter(new VideoAdapter(VideoLectureActivity.this,ar));
                    noticeRecy.scrollToPosition(ar.size()-1);
                }
                else{temp.add(null);
                    for(int i=0;i<arrayList.size();i++){
                        if(subArraylist.get(position).equals(arrayList.get(i).getSubjectName()))
                            temp.add(arrayList.get(i));
                    }

                    noticeRecy.setAdapter(new VideoAdapter(VideoLectureActivity.this,temp));
                    noticeRecy.scrollToPosition(temp.size()-1);
                }



            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}