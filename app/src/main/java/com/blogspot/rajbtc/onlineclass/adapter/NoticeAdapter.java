package com.blogspot.rajbtc.onlineclass.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.blogspot.rajbtc.onlineclass.R;
import com.blogspot.rajbtc.onlineclass.dataclass.NoticeData;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.NoticeViewHolder> {

    private Context context;
    private ArrayList<NoticeData> arrayList;

    public NoticeAdapter(Context context, ArrayList<NoticeData> arrayList) {
        this.context = context;
        this.arrayList=arrayList;
    }

    @NonNull
    @Override
    public NoticeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType==1)
            return new NoticeViewHolder(LayoutInflater.from(context).inflate(R.layout.dummy,parent,false));

        return new NoticeViewHolder(LayoutInflater.from(context).inflate(R.layout.single_notice_recy,parent,false));
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



        holder.notice.setText(arrayList.get(position).getNotice());
        holder.date.setText(arrayList.get(position).getDate());
      //  holder.time.setText(arrayList.get(position).getTime());

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
                }).setNegativeButton("No",null).show();

                return  true;
            }
        });

    }


    @Override
    public int getItemViewType(int position) {
        if(position==0 || position==arrayList.size())
            return 1;
        return 0;
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

    public class NoticeViewHolder extends RecyclerView.ViewHolder{
        TextView notice, date, time;

        public NoticeViewHolder(@NonNull View itemView) {
            super(itemView);
            notice=itemView.findViewById(R.id.singleNoticeNoticeTv);
            date=itemView.findViewById(R.id.singleNoticeDateTv);
            time=itemView.findViewById(R.id.singleNoticeTimeTv);
        }
    }
}
