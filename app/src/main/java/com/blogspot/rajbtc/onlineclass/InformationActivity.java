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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import com.blogspot.rajbtc.onlineclass.adapter.InformationAdapter;
import com.blogspot.rajbtc.onlineclass.dataclass.InformationData;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

public class InformationActivity extends AppCompatActivity {
    
    private RecyclerView recyclerView;
    private FloatingActionButton fab;
    private SearchView searchView;
    private DatabaseReference fireRef= FirebaseDatabase.getInstance().getReference("Data");
    private String adminID,adminPass;
    private InformationAdapter adapter;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setupView();
        checkAdminOrUser();
        loadFireData();

    }

    private void setupView() {
        searchView=findViewById(R.id.information_Sv);
        recyclerView=findViewById(R.id.information_Rv);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        fab=findViewById(R.id.info_Fab);
        adapter=new InformationAdapter(this,null);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upToDb();
            }
        });

    }

    private void setSearch(){
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
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

        final EditText nameEt=new EditText(this);
        nameEt.setHint("Name");
        final EditText stdIDEt=new EditText(this);
        stdIDEt.setHint("Student ID");
        final Spinner bloodSpn=new Spinner(this);
        bloodSpn.setAdapter(new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item,new String[]{"A+","A-","B+","B-","O+","O-","AB+","AB-","None"}));
        final EditText phnEt=new EditText(this);
        phnEt.setHint("Phone");
        phnEt.setInputType(InputType.TYPE_CLASS_PHONE);
        final EditText mailEt=new EditText(this);
        mailEt.setHint("Email");
        mailEt.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);




        linearLayout.addView(nameEt);
        linearLayout.addView(stdIDEt);
        linearLayout.addView(bloodSpn);
        linearLayout.addView(phnEt);
        linearLayout.addView(mailEt);


        AlertDialog.Builder builder=new AlertDialog.Builder(InformationActivity.this);
        builder.setView(linearLayout).setTitle("Input")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        String name=nameEt.getText().toString();
                        String stdID=stdIDEt.getText().toString();
                        String blood=bloodSpn.getSelectedItem().toString();
                        String phone=phnEt.getText().toString();
                        String mail=mailEt.getText().toString();

                        if(name.equals("") || stdID.equals("") || blood.equals("") || mail.equals("")){
                            Toast.makeText(getApplicationContext(),"Please fulfil all input",Toast.LENGTH_SHORT).show();
                            return;
                        }



                        InformationData data=new InformationData(null,name,stdID,blood,phone,mail);

                        fireRef.child(adminID.replace('.','_').replace("@","__")+adminPass).child("userinfo").push().setValue(data).addOnSuccessListener(new OnSuccessListener<Void>() {
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
        final ArrayList<InformationData> arrayList=new ArrayList<>();
        recyclerView.setAdapter(null);
        fireRef.child(adminID.replace('.','_').replace("@","__")+adminPass).child("userinfo").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                InformationData val=snapshot.getValue(InformationData.class);
                val.setReference(snapshot.getRef());
                arrayList.add(val);
                try{
                    Collections.sort(arrayList, new Comparator<InformationData>() {
                        public int compare(InformationData left, InformationData right)  {
                            return Integer.parseInt(left.getRoll()) - Integer.parseInt(right.getRoll()); // The order depends on the direction of sorting.
                        }
                    });
                }catch (Exception e){

                }
                adapter=new InformationAdapter(InformationActivity.this,arrayList);
                recyclerView.setAdapter(adapter);
                setSearch();
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