package com.blogspot.rajbtc.onlineclass.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.blogspot.rajbtc.onlineclass.R;
import com.blogspot.rajbtc.onlineclass.dataclass.ClassData;
import com.blogspot.rajbtc.onlineclass.dataclass.NoticeData;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ClassAdapter extends RecyclerView.Adapter<ClassAdapter.ClassUserViewHolder> {

    private Context context;
    private ArrayList<ClassData> arrayList;
    private DatabaseReference noticeRef;

    public ClassAdapter(Context context, ArrayList<ClassData> arrayList, DatabaseReference noticeRef) {
        this.context = context;
        this.arrayList=arrayList;

        this.noticeRef=noticeRef;

    }

    @NonNull
    @Override
    public ClassUserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ClassUserViewHolder(LayoutInflater.from(context).inflate(R.layout.single_class_recy,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ClassUserViewHolder holder, final int position) {

//        String time;
//
//        try {
//            String _24HourTime = arrayList.get(position).getStartTime();
//            SimpleDateFormat _24HourSDF = new SimpleDateFormat("HH:mm");
//            SimpleDateFormat _12HourSDF = new SimpleDateFormat("hh:mm a");
//            Date _24HourDt = _24HourSDF.parse(_24HourTime);
//           time=_12HourSDF.format(_24HourDt);
//        } catch (Exception e) {
//            time=arrayList.get(position).getStartTime();
//        }



        holder.timeAndDuration.setText((arrayList.get(position).getStartTime()+"-"+arrayList.get(position).getEndTime()).toUpperCase());
        holder.subName.setText(arrayList.get(position).getClassName());
        holder.teacherName.setText(arrayList.get(position).getTeacher());

        holder.joinClassTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri;
                if(arrayList.get(position).getLink().startsWith("https://"))
                    uri=Uri.parse(arrayList.get(position).getLink());
                else
                    uri=Uri.parse("https://"+arrayList.get(position).getLink());

                        Intent intent=new Intent(Intent.ACTION_VIEW, uri);
                context.startActivity(intent);
            }
        });

        if(isAdmin())
             holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder ab=new AlertDialog.Builder(context);
                ab.setTitle("Are you want to delete?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        arrayList.get(position).getDocID().removeValue(new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                Toast.makeText(context,"Data removed!",Toast.LENGTH_LONG).show();
                                arrayList.remove(position);
                                notifyDataSetChanged();
                            }
                        });
                    }
                }).setNegativeButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                            uploadData(arrayList.get(position),position);
                    }
                }).show();

                return  true;
            }
        });






    }

    boolean isAdmin(){
        String userType=context.getSharedPreferences("userData",Context.MODE_PRIVATE).getString("userType","null");
        //    Log.e(TAG,userType);
        return userType.toLowerCase().equals("admin");

    }






    private void uploadData(final ClassData infoData, final int position) {
        LinearLayout mainLayout=new LinearLayout(context);
        mainLayout.setOrientation(LinearLayout.VERTICAL);

        final EditText classNameEt=new EditText(context);
        classNameEt.setText(arrayList.get(position).getClassName());
        classNameEt.setHint("Class name");
        final EditText startTimeEt=new EditText(context);
        startTimeEt.setText(arrayList.get(position).getStartTime());
        startTimeEt.setHint("Start time");
        startTimeEt.setInputType(InputType.TYPE_DATETIME_VARIATION_TIME);
        final EditText endTimeEt=new EditText(context);
        endTimeEt.setText(arrayList.get(position).getEndTime());
        endTimeEt.setHint("End time");
        endTimeEt.setInputType(InputType.TYPE_DATETIME_VARIATION_TIME);
        final EditText linkEt=new EditText(context);
        linkEt.setText(arrayList.get(position).getLink());
        linkEt.setHint("Link");
        final EditText teacherEt=new EditText(context);
        teacherEt.setText(arrayList.get(position).getTeacher());
        teacherEt.setHint("Teacher");

        mainLayout.addView(classNameEt);
        mainLayout.addView(startTimeEt);
        mainLayout.addView(endTimeEt);
        mainLayout.addView(linkEt);
        mainLayout.addView(teacherEt);

        AlertDialog.Builder builder=new AlertDialog.Builder(context);

        builder.setView(mainLayout).setTitle("Input")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {



                        final String className=classNameEt.getText().toString();
                        final String endTime=endTimeEt.getText().toString();
                        final String startTime=startTimeEt.getText().toString();
                        final String link=linkEt.getText().toString();
                        final String teacher=teacherEt.getText().toString();

                        if(className.equals("")  || endTime.equals("") || startTime.equals("") || link.equals("") || teacher.equals("")){
                            Toast.makeText(context,"Please fulfil all input",Toast.LENGTH_SHORT).show();
                            return;
                        }

                        final String date = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
                        final String time = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());


                        ClassData classUpData=new ClassData(null,className,arrayList.get(position).getDay(),teacher,startTime,endTime,time+" "+date,link);
                        final NoticeData noticeData=new NoticeData(null,"Your "+className+" class time has been updated!",time,date);

                        infoData.getDocID().setValue(classUpData).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                noticeRef.push().setValue(noticeData);
                                arrayList.get(position).setClassName(className);
                                arrayList.get(position).setEndTime(endTime);
                                arrayList.get(position).setStartTime(startTime);
                                arrayList.get(position).setLink(link);
                                arrayList.get(position).setTeacher(teacher);
                                arrayList.get(position).setDay(arrayList.get(position).getDay());
                                notifyDataSetChanged();
                            }
                        });





                    }
                }).setNegativeButton("Cancel",null);

        AlertDialog alertDialog=builder.create();
        alertDialog.show();
    }








    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class ClassUserViewHolder extends RecyclerView.ViewHolder{

        TextView joinClassTv,timeAndDuration,teacherName,subName;

        public ClassUserViewHolder(@NonNull View itemView) {
            super(itemView);
            joinClassTv=itemView.findViewById(R.id.single_class_recy_joinClassTv);
            timeAndDuration=itemView.findViewById(R.id.single_class_recy_timeAndDurationTv);
            teacherName=itemView.findViewById(R.id.single_class_recy_teacherTv);
            subName=itemView.findViewById(R.id.single_class_recy_subTv);
        }
    }
}
