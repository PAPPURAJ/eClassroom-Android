package com.blogspot.rajbtc.onlineclass;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

public class AboutDeveloper extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_developer);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setupOnclick();

    }


    void setupOnclick(){
        findViewById(R.id.contact_pappurajCallTv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCallBtnClick("+8801832755401");
            }
        });



        findViewById(R.id.contact_pappurajFbTv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_VIEW,Uri.parse("https://www.facebook.com/PAPPURAJ.DUET"));
                startActivity(intent);

            }
        });



        findViewById(R.id.contact_pappurajLinkedinTv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_VIEW,Uri.parse("https://www.linkedin.com/in/PAPPURAJ"));
                startActivity(intent);
            }
        });


        findViewById(R.id.contact_pappurajMailTv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(Intent.ACTION_VIEW,Uri.parse("mailto:pappuraj.duet@gmail.com"));
                startActivity(intent);
            }
        });


        findViewById(R.id.contact_nayeemCallTv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCallBtnClick("+8801303048321");
            }
        });



        findViewById(R.id.contact_nayeemFbTv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_VIEW,Uri.parse("https://www.facebook.com/ahmednayem.bd/"));
                startActivity(intent);

            }
        });



        findViewById(R.id.contact_nayeemLinkedinTv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_VIEW,Uri.parse("https://www.linkedin.com/in/ahmednayem/"));
                startActivity(intent);
            }
        });


        findViewById(R.id.contact_nayeemMailTv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(Intent.ACTION_VIEW,Uri.parse("mailto:nayemahmed921@gmail.com"));
                startActivity(intent);
            }
        });




        findViewById(R.id.contact_yaminCallTv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCallBtnClick("+8801915395544");
            }
        });



        findViewById(R.id.contact_yaminFbTv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_VIEW,Uri.parse("https://facebook.com/mdyamin007"));
                startActivity(intent);

            }
        });



        findViewById(R.id.contact_yaminLinkedinTv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_VIEW,Uri.parse("https://www.linkedin.com/in/mdyamin"));
                startActivity(intent);
            }
        });


        findViewById(R.id.contact_yaminMailTv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(Intent.ACTION_VIEW,Uri.parse("mailto:mdyamin007@yahoo.com"));
                startActivity(intent);
            }
        });



        findViewById(R.id.contact_nabilCallTv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCallBtnClick("+8801521211410");
            }
        });



        findViewById(R.id.contact_nabilFbTv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_VIEW,Uri.parse("https://www.facebook.com/tanvirliban"));
                startActivity(intent);

            }
        });



        findViewById(R.id.contact_nabilLinkedinTv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_VIEW,Uri.parse("https://www.linkedin.com/in/tanvirliban"));
                startActivity(intent);
            }
        });


        findViewById(R.id.contact_nabilMailTv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(Intent.ACTION_VIEW,Uri.parse("mailto:tanvirliban@gmail.com"));
                startActivity(intent);
            }
        });




    }








    private void onCallBtnClick(String num){
        if (Build.VERSION.SDK_INT < 23) {
            phoneCall(num);
        }else {

            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {

                phoneCall(num);
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

    private void phoneCall(String num){
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:"+num));
            startActivity(callIntent);
        }else{
            Toast.makeText(this, "You don't assign permission.", Toast.LENGTH_SHORT).show();
        }
    }



}