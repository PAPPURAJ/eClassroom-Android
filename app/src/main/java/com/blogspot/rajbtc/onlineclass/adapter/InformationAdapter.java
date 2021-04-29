package com.blogspot.rajbtc.onlineclass.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.blogspot.rajbtc.onlineclass.InformationActivity;
import com.blogspot.rajbtc.onlineclass.R;
import com.blogspot.rajbtc.onlineclass.dataclass.ClassData;
import com.blogspot.rajbtc.onlineclass.dataclass.InformationData;
import com.blogspot.rajbtc.onlineclass.utility.Compare;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class InformationAdapter  extends RecyclerView.Adapter<InformationAdapter.ViewHolder> implements Filterable {
    private Context context;
    private ArrayList<InformationData> arrayList;
    private ArrayList<InformationData> filteredList=new ArrayList<>();

    public InformationAdapter(Context context, ArrayList<InformationData> arrayList) {
        this.context = context;
        this.arrayList=arrayList;
        filteredList=arrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType==1)
            return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.dummy,parent,false));

        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.single_information,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        if(position==0 || position==filteredList.size()+1)
            return;


        holder.nameTv.setText(filteredList.get(position-1).getName());
        holder.stdIDTv.setText("STUDENT ID: "+filteredList.get(position-1).getRoll());
        holder.bloodTv.setText("BLOOD GROUP: "+filteredList.get(position-1).getBlood());

        holder.phoneIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    context.startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+filteredList.get(position-1).getPhone())));
                }catch (Exception E){
                    Toast.makeText(context,"Cannot be connected!",Toast.LENGTH_LONG).show();
                }

            }
        });

        holder.mailIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:"+filteredList.get(position-1).getMail())));
                }catch (Exception E){
                    Toast.makeText(context,"Cannot be connected!",Toast.LENGTH_LONG).show();
                }

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
                            filteredList.get(position-1).getReference().removeValue(new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                    Toast.makeText(context,"Data removed!",Toast.LENGTH_LONG).show();
                                    arrayList.remove(position-1);
                                    notifyDataSetChanged();
                                }
                            });
                        }
                    }).setNegativeButton("Update", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            updateData(filteredList.get(position-1),position-1);
                        }
                    }).show();

                    return  true;
                }
            });
    }



    @Override
    public int getItemCount() {
        return filteredList.size()+2;
    }

    @Override
    public int getItemViewType(int position) {
        if(position==0 || position==filteredList.size()+1)
            return 1;
        return 0;
    }







    void updateData(final InformationData infoData,  final int position){

        final LinearLayout linearLayout=new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        final EditText nameEt=new EditText(context);
        nameEt.setHint("Name");
        nameEt.setText(infoData.getName());
        final EditText stdIDEt=new EditText(context);
        stdIDEt.setHint("Student ID");
        stdIDEt.setText(infoData.getRoll());
        final Spinner bloodSpn=new Spinner(context);
        bloodSpn.setAdapter(new ArrayAdapter<String>(context,R.layout.support_simple_spinner_dropdown_item,new String[]{"A+","A-","B+","B-","O+","O-","AB+","AB-","None"}));
        final EditText phnEt=new EditText(context);
        phnEt.setHint("Phone");
        phnEt.setInputType(InputType.TYPE_CLASS_PHONE);
        phnEt.setText(infoData.getPhone());
        final EditText mailEt=new EditText(context);
        mailEt.setHint("Email");
        mailEt.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        mailEt.setText(infoData.getMail());



        linearLayout.addView(nameEt);
        linearLayout.addView(stdIDEt);
        linearLayout.addView(bloodSpn);
        linearLayout.addView(phnEt);
        linearLayout.addView(mailEt);


        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setView(linearLayout).setTitle("Input")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                         final String name=nameEt.getText().toString();
                         final String stdID=stdIDEt.getText().toString();
                         final String blood=bloodSpn.getSelectedItem().toString();
                         final String phone=phnEt.getText().toString();
                         final String mail=mailEt.getText().toString();

                        if(name.equals("") || stdID.equals("") || blood.equals("") || mail.equals("")){
                            Toast.makeText(context,"Please fulfil all input",Toast.LENGTH_SHORT).show();
                            return;
                        }



                        InformationData data=new InformationData(null,name,stdID,blood,phone,mail);

                        infoData.getReference().setValue(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                arrayList.get(position).setName(name);
                                arrayList.get(position).setRoll(stdID);
                                arrayList.get(position).setBlood(blood);
                                arrayList.get(position).setPhone(phone);
                                arrayList.get(position).setMail(mail);
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
        return userType.toLowerCase().equals("admin");

    }

    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String query = constraint.toString();

                ArrayList<InformationData> filtered = new ArrayList<>();

                if (query.isEmpty()) {
                    for(int i=0;i<arrayList.size();i++)
                        filtered.add(arrayList.get(i));
                } else {

                    for (int i=0;i<arrayList.size();i++) {
                        if (arrayList.get(i).getName().toLowerCase().contains(query.toLowerCase()) || arrayList.get(i).getPhone().toLowerCase().contains(query.toLowerCase()) ||arrayList.get(i).getBlood().toLowerCase().contains(query.toLowerCase()) ||arrayList.get(i).getRoll().toLowerCase().contains(query.toLowerCase())  ) {
                            filtered.add(arrayList.get(i));
                        }
                    }
                }

                FilterResults results = new FilterResults();
                results.count = filtered.size();
                results.values = filtered;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredList = (ArrayList<InformationData>) results.values;
                notifyDataSetChanged();
            }
        };

    }


    class ViewHolder extends RecyclerView.ViewHolder{
        TextView nameTv,stdIDTv,bloodTv;
        ImageView phoneIv,mailIv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTv=itemView.findViewById(R.id.singleInfo_nameTv);
            stdIDTv=itemView.findViewById(R.id.singleInfo_idTv);
            bloodTv=itemView.findViewById(R.id.singleInfo_bloodTv);
            phoneIv=itemView.findViewById(R.id.singleInfo_callIv);
            mailIv=itemView.findViewById(R.id.singleInfo_mailIv);
        }
    }
}
