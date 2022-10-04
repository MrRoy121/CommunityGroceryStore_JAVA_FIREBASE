package com.example.communitygrocerystoreapp.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.communitygrocerystoreapp.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder>{

    ArrayList<UserModel> listdata;
    Context ctx;

    FirebaseFirestore fs = FirebaseFirestore.getInstance();
    public UserAdapter( ArrayList<UserModel> listdata, Context ctx) {
        this.listdata = listdata;
        this.ctx = ctx;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.user_items, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final UserModel myListData = listdata.get(position);
        holder.t1.setText(listdata.get(position).getFname()+" "+ listdata.get(position).getLname());
        holder.t2.setText(listdata.get(position).getEmail());
        holder.t3.setText(listdata.get(position).getUid());
        holder.t4.setText(listdata.get(position).getPhone());
        holder.t5.setText(listdata.get(position).getHname()+", "+ listdata.get(position).getCity());
        holder.Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(ctx).setIcon(android.R.drawable.ic_input_add)
                        .setTitle("Confirm Registration").setMessage("Confirm Registration for "+ myListData.getFname())
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                Map<String, Object> house = new HashMap<>();
                                house.put("House No", myListData.hno);
                                house.put("Housing Society Name", myListData.hname);
                                house.put("City", myListData.city);
                                Map<String, Object> user = new HashMap<>();
                                user.put("Email", myListData.email);
                                user.put("First Name", myListData.fname);
                                user.put("Last Name", myListData.lname);
                                user.put("Pin", myListData.pass);
                                user.put("Phone", myListData.phone);
                                user.put("Address", house);
                                fs.collection("Registered_User").document(myListData.uid).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Map<String, String> bal = new HashMap<>();
                                        bal.put("Available", "5000");
                                        fs.collection("Account").document(myListData.uid).set(bal);
                                        fs.collection("User_Request").document(myListData.uid).delete();
                                        listdata.remove(myListData);
                                        notifyDataSetChanged();
                                    }
                                });
                            }
                        }).setNegativeButton("No", null).show();
            }
        });
    }


    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView t1, t2, t3,t4,t5;
        public LinearLayout Layout;
        public ViewHolder(View itemView) {
            super(itemView);
            this.t1 = (TextView) itemView.findViewById(R.id.name);
            this.t2 = (TextView) itemView.findViewById(R.id.email);
            this.t3 = (TextView) itemView.findViewById(R.id.uid);
            this.t4 = (TextView) itemView.findViewById(R.id.phone);
            this.t5 = (TextView) itemView.findViewById(R.id.add);
            this.Layout = itemView.findViewById(R.id.llout);
        }
    }
}