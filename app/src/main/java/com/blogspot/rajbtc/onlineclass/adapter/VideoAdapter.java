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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.blogspot.rajbtc.onlineclass.R;
import com.blogspot.rajbtc.onlineclass.dataclass.InformationData;
import com.blogspot.rajbtc.onlineclass.dataclass.NoticeData;
import com.blogspot.rajbtc.onlineclass.dataclass.VideoDataclass;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.NoticeViewHolder> {

    private Context context;
    private ArrayList<VideoDataclass> arrayList;
    private ArrayList<String> subArraylist=new ArrayList<>();

    public VideoAdapter(Context context, ArrayList<VideoDataclass> arrayList) {
        this.context = context;
        this.arrayList=arrayList;
        for(int i=1;i<arrayList.size();i++)
            if(!subArraylist.contains(arrayList.get(i).getSubjectName()))
                subArraylist.add(arrayList.get(i).getSubjectName());
    }

    @NonNull
    @Override
    public NoticeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType==1){
            View view=LayoutInflater.from(context).inflate(R.layout.dummy,parent,false);

            ViewGroup.LayoutParams params = view.getLayoutParams();
            params.height = 200;
            params.width = 100;
            view.setLayoutParams(params);
            return new NoticeViewHolder(view);
        }

        return new NoticeViewHolder(LayoutInflater.from(context).inflate(R.layout.single_video_recy,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull NoticeViewHolder holder, final int position) {

        if(position==0 || position==arrayList.size())
            return;


        try {
            String _24HourTime = arrayList.get(position).getTime();
            SimpleDateFormat _24HourSDF = new SimpleDateFormat("HH:mm");
            SimpleDateFormat _12HourSDF = new SimpleDateFormat("hh:mm a");
            Date _24HourDt = _24HourSDF.parse(_24HourTime);
            holder.time.setText(_12HourSDF.format(_24HourDt));
        } catch (Exception e) {
            holder.time.setText(arrayList.get(position).getTime());
        }




        holder.videoName.setText("Video: "+arrayList.get(position).getVideoName());
        holder.subName.setText("Subject: "+arrayList.get(position).getSubjectName());
        holder.teacherName.setText("Teacher: "+arrayList.get(position).getTeacherName());
        holder.date.setText(arrayList.get(position).getDate());
       // holder.time.setText(arrayList.get(position).getTime());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
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
                            arrayList.get(position).getReference().removeValue(new DatabaseReference.CompletionListener() {
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
                            updateData(arrayList.get(position),position);
                        }
                    }).show();

                    return  true;
                }
            });



    }











    void updateData(final VideoDataclass infoData, final int position){

        final LinearLayout linearLayout=new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        final EditText videoNameEt=new EditText(context);
        videoNameEt.setHint("Video name");
        videoNameEt.setText(arrayList.get(position).getVideoName());
        final AutoCompleteTextView subjectAutoTv=new AutoCompleteTextView(context);
        subjectAutoTv.setAdapter(new ArrayAdapter<String>(context,R.layout.support_simple_spinner_dropdown_item,subArraylist));
        subjectAutoTv.setHint("Subject name");
        subjectAutoTv.setThreshold(1);
        subjectAutoTv.setText(arrayList.get(position).getSubjectName());

        final EditText teacherEt=new EditText(context);
        teacherEt.setHint("Teacher name");
        teacherEt.setText(arrayList.get(position).getTeacherName());

        final EditText linkEt=new EditText(context);
        linkEt.setHint("URL");
        linkEt.setInputType(InputType.TYPE_TEXT_VARIATION_URI);
        linkEt.setText(arrayList.get(position).getLink());

        linearLayout.addView(videoNameEt);
        linearLayout.addView(subjectAutoTv);
        linearLayout.addView(teacherEt);
        linearLayout.addView(linkEt);


        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setView(linearLayout).setTitle("Input")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        final String videoName=videoNameEt.getText().toString();
                        final String sub=subjectAutoTv.getText().toString();
                        final String teach=teacherEt.getText().toString();
                        final String link=linkEt.getText().toString().replace(" ","");

                        if(videoName.equals("") || sub.equals("")  || teach.equals("") || link.equals("")){
                            Toast.makeText(context,"Please fulfil all input",Toast.LENGTH_SHORT).show();
                            return;
                        }

                        final String date = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
                        final String time = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());


                        VideoDataclass data=new VideoDataclass(null,videoName,sub,teach,link,date,time);

                        infoData.getReference().setValue(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                arrayList.get(position).setVideoName(videoName);
                                arrayList.get(position).setSubjectName(sub);
                                arrayList.get(position).setTeacherName(teach);
                                arrayList.get(position).setLink(link);
                                arrayList.get(position).setDate(date);
                                arrayList.get(position).setTime(time);
                                notifyDataSetChanged();
                                Toast.makeText(context,"Uploaded!",Toast.LENGTH_SHORT).show();

                            }
                        });





                    }
                }).setNegativeButton("Cancel",null);

        AlertDialog alertDialog=builder.create();
        alertDialog.show();

    }















    boolean isAdmin(){
        String userType=context.getSharedPreferences("userData",Context.MODE_PRIVATE).getString("userType","null");
        //    Log.e(TAG,userType);
        return userType.toLowerCase().equals("admin");

    }


    @Override
    public int getItemCount() {

            return arrayList.size()+1;
    }


    @Override
    public int getItemViewType(int position) {
        if(position==0 || position==arrayList.size())
            return 1;


        return 0;
    }


    public class NoticeViewHolder extends RecyclerView.ViewHolder{
        TextView videoName,subName,teacherName, date, time;

        public NoticeViewHolder(@NonNull View itemView) {
            super(itemView);
            videoName=itemView.findViewById(R.id.singleVideoNameTv);
            subName=itemView.findViewById(R.id.singleVideoSubTv);
            teacherName=itemView.findViewById(R.id.singleVideoTeacherTv);
            date=itemView.findViewById(R.id.singleVideoDateTv);
            time=itemView.findViewById(R.id.singleVideoTimeTv);
        }
    }
}
