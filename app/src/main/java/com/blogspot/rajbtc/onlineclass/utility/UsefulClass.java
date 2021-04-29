package com.blogspot.rajbtc.onlineclass.utility;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class UsefulClass {
    AlertDialog alertDialog;

    public UsefulClass(Context context){
        ProgressBar progressBar=new ProgressBar(context);
        LinearLayout linearLayout=new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setGravity(Gravity.CENTER);
        linearLayout.addView(progressBar);
        TextView textView=new TextView(context);
        textView.setText("Please wait!");
        textView.setTextSize(25);
        textView.setGravity(Gravity.CENTER_HORIZONTAL);
        linearLayout.addView(textView);

        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setView(linearLayout);
         alertDialog=builder.create();

    }

    public void start(){
        alertDialog.show();
    }

    public void stop(){
        alertDialog.hide();
    }

}
