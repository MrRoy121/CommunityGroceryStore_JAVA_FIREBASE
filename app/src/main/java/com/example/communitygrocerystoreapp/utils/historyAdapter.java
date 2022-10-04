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

public class historyAdapter  extends RecyclerView.Adapter<historyAdapter.ViewHolder>{

    ArrayList<HistoryModel> listdata;
    Context ctx;

    public historyAdapter( ArrayList<HistoryModel> listdata, Context ctx) {
        this.listdata = listdata;
        this.ctx = ctx;
    }
    @Override
    public historyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.history_item, parent, false);
        historyAdapter.ViewHolder viewHolder = new historyAdapter.ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(historyAdapter.ViewHolder holder, int position) {

        holder.t1.setText(listdata.get(position).getAmount());
        holder.t2.setText(listdata.get(position).getItems());

    }


    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView t1, t2;
        public ViewHolder(View itemView) {
            super(itemView);
            this.t1 = (TextView) itemView.findViewById(R.id.t1);
            this.t2 = (TextView) itemView.findViewById(R.id.t2);
        }
    }
}