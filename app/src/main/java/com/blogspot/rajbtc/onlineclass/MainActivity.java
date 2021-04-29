package com.blogspot.rajbtc.onlineclass;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    SharedPreferences sp;
    private String adminID="",adminPass="";
    private String TAG="====Main Activity====";
    private Intent serviceIntent;
    private SharedPreferences memorySetSp;
    private MenuItem runInBackSett;

   
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sp=getSharedPreferences("adminInfo",MODE_PRIVATE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        memorySetSp=getSharedPreferences("setting",MODE_PRIVATE);


        String date = new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime());
        ((TextView)findViewById(R.id.main_DateTv)).setText(date);

        checkPermission();

        adminID=sp.getString("classID","");
        adminPass=sp.getString("classPass","");
        setupToolbar();

        serviceIntent=new Intent(this,MyService.class);
        if(!isAdmin() && !adminID.equals("") && !adminPass.equals("") && !isMyServiceRunning(MyService.class) && memorySetSp.getInt("isrun",1)==1){
            startService(serviceIntent);
            runInBackSett.setChecked(true);
        }


    }










    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }






    public void routineClick(View view) {checkAndStart(RoutineActivity.class);}
    public void slideClick(View view) {checkAndStart(SlideActivity.class);}
    public void noticeClick(View view) {checkAndStart(NoticeActivity.class);}
    public void videoClick(View view) {checkAndStart(VideoLectureActivity.class);}
    public void informationClick(View view) {checkAndStart(InformationActivity.class);}







   
    void setupToolbar(){
        if(isAdmin()){
            setupAdminMenu();
        }
        else{
            if(adminID.equals("") || adminPass.equals(""))
                setupUserMenu();
            else
                setupAdminMenu();
        }

    }



    void checkAndStart(Class cls){
        if((adminID.equals("") || adminPass.equals("")) && !isAdmin()){
            Toast.makeText(getApplicationContext(),"Please connect a class",Toast.LENGTH_SHORT).show();
            return;
        }
        startActivity(new Intent(this, cls));
    }


    boolean isAdmin(){
        String userType=getSharedPreferences("userData",MODE_PRIVATE).getString("userType","null");
        Log.e(TAG,userType);
        return userType.toLowerCase().equals("admin");

    }



   
    void setupUserMenu(){
        final Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        toolbar.inflateMenu(R.menu.user_menu);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent in = new Intent(MainActivity.this, LoginActivity.class);

                if(item.getItemId()==R.id.menu_user_connectClass){
                    LinearLayout main=new LinearLayout(getApplicationContext());
                    main.setOrientation(LinearLayout.VERTICAL);
                    final EditText ID=new EditText(getApplicationContext());
                    ID.setHint("Admin email");
                    ID.setHintTextColor(Color.parseColor("#a9a9a9"));
                    ID.setTextColor(Color.BLACK);
                    final EditText pass=new EditText(getApplicationContext());
                    pass.setHint("Class pass");
                    pass.setTextColor(Color.BLACK);
                    pass.setHintTextColor(Color.parseColor("#a9a9a9"));
                    main.addView(ID);
                    main.addView(pass);

                    AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Connect class").setView(main).
                            setPositiveButton("Connect", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String classID=ID.getText().toString().replace(" ","");
                                    String classPass=pass.getText().toString().replace(" ","");

                                    if(classID.equals("") || classPass.equals("")){
                                        Toast.makeText(getApplicationContext(),"Please input first",Toast.LENGTH_LONG).show();
                                        return;
                                    }
                                    adminID=classID;
                                    adminPass=classPass;

                                    sp.edit().putString("classID",classID).putString("classPass",classPass).apply();
                                    Toast.makeText(getApplicationContext(),"Class connected with "+adminID+"!",Toast.LENGTH_LONG).show();
                                    toolbar.getMenu().clear();
                                    setupAdminMenu();
                                    startService(serviceIntent);
                                    runInBackSett.setChecked(true);

                                }
                            }).setNegativeButton("Cancel",null).show();
                }
                else if(item.getItemId()==R.id.menu_user_logout){
                    FirebaseAuth.getInstance().signOut();
                    in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    sp.edit().clear().commit();
                    stopService(serviceIntent);
                    startActivity(in);
                }



                else if(item.getItemId()==R.id.menu_user_about){

                    AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("About App")
                            .setMessage("eClassroom – Nowadays, Online class is our daily round. Sometimes it is troublesome to ourselves. Remembering the class time, the link, class's information/news we need to access social groups and sometimes we couldn't even find the correct news for so many chattings in the group. To eliminate these issues and troubles we decided to make an app which can ease our daily life and provide some facilities. eClassroom will tell and notify you (by an alarm) before class time, will provide you class links, slides, ebooks, class materials and many more. Your class representative can update/add class time, links, lecture materials and can post important notices on the app's notice board. ")
                            .setPositiveButton("Ok",null).show();

                }
                else if(item.getItemId()==R.id.menu_user_contact_about){
                    startActivity(new Intent(MainActivity.this,AboutDeveloper.class));

                }

                try {
                    runInBackSett.setChecked(isMyServiceRunning(MyService.class));
                }
                catch (Exception e){

                }




                return  true;
            }
        });

    }

   
    void setupAdminMenu(){

        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        toolbar.inflateMenu(R.menu.admin_menu);
        runInBackSett=toolbar.getMenu().findItem(R.id.menu_admin_setting);


        try {
            runInBackSett.setChecked(isMyServiceRunning(MyService.class));
        }
        catch (Exception e){

        }

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent in = new Intent(MainActivity.this, LoginActivity.class);


                 if(item.getItemId()==R.id.menu_admin_logout){
                    FirebaseAuth.getInstance().signOut();
                    in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                     sp.edit().clear().commit();
                    startActivity(in);
                     stopService(serviceIntent);
                }


                 else if(item.getItemId()==R.id.menu_admin_setting){
                     //startActivity(new Intent(MainActivity.this,SettingActivity.class));
                     if(item.isChecked()){
                         item.setChecked(false);
                         memorySetSp.edit().putInt("isrun",0).apply();
                         stopService(serviceIntent);
                         Toast.makeText(getApplicationContext(),"Background service is off",Toast.LENGTH_SHORT).show();

                     }

                     else{
                         item.setChecked(true);
                         memorySetSp.edit().putInt("isrun",1).apply();
                         startService(serviceIntent);
                         Toast.makeText(getApplicationContext(),"Background service is On",Toast.LENGTH_SHORT).show();
                     }


                 }


                 else if(item.getItemId()==R.id.menu_admin_contact_about){
                     startActivity(new Intent(MainActivity.this,AboutDeveloper.class));

                 }
                else if(item.getItemId()==R.id.menu_admin_about){

                     AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
                     builder.setTitle("About App")
                             .setMessage("eClassroom – Nowadays, Online class is our daily round. Sometimes it is troublesome to ourselves. Remembering the class time, the link, class's information/news we need to access social groups and sometimes we couldn't even find the correct news for so many chattings in the group. To eliminate these issues and troubles we decided to make an app which can ease our daily life and provide some facilities. eClassroom will tell and notify you (by an alarm) before class time, will provide you class links, slides, ebooks, class materials and many more. Your class representative can update/add class time, links, lecture materials and can post important notices on the app's notice board. ")
                             .setPositiveButton("Ok",null).show();
                }


                return  true;
            }
        });

    }
















    private void checkPermission(){
        if (Build.VERSION.SDK_INT < 23) {
        }else {

            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {

            }else {
                final String[] PERMISSIONS_STORAGE = {Manifest.permission.CALL_PHONE};
                //Asking request Permissions
                ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, 9);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        boolean permissionGranted = false;
        switch(requestCode){
            case 9:
                permissionGranted = grantResults[0]== PackageManager.PERMISSION_GRANTED;
                break;
        }
        if(permissionGranted){
        }else {
            Toast.makeText(this, "You don't assign permission.", Toast.LENGTH_SHORT).show();
        }
    }


}