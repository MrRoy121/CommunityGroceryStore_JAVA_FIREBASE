package com.example.communitygrocerystoreapp.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.communitygrocerystoreapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class CartItemAdapter extends RecyclerView.Adapter<CartItemAdapter.ViewHolder>{
    private ArrayList<CartItemModel> listdata;
    Context context;

    public CartItemAdapter(ArrayList<CartItemModel> listdata, Context context) {
        this.listdata = listdata;
        this.context = context;
    }

    @Override
    public CartItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.cart_item, parent, false);
        CartItemAdapter.ViewHolder viewHolder = new CartItemAdapter.ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CartItemAdapter.ViewHolder holder, int position) {
        final CartItemModel myListData = listdata.get(position);
        int p = position;
        holder.iname.setText(listdata.get(position).name);
        holder.icount.setText(String.valueOf(listdata.get(position).count));
        holder.iprice.setText(String.valueOf(listdata.get(position).price));
        holder.itotal.setText(String.valueOf(Double.parseDouble(listdata.get(position).price)*listdata.get(position).count));
        holder.idelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DocumentReference ds = FirebaseFirestore.getInstance().collection("Cart").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
                ds.collection("Items").document(myListData.id).delete();
                listdata.remove(p);
                notifyDataSetChanged();
                Toast.makeText(context, "Item is Deleted from the cart!!", Toast.LENGTH_SHORT).show();
            }
        });
        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listdata.set(p,new CartItemModel( myListData.name, myListData.count+1, myListData.price, myListData.id));
                notifyDataSetChanged();
            }
        });
        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(myListData.count>1){
                listdata.set(p,new CartItemModel( myListData.name, myListData.count-1, myListData.price, myListData.id));
                notifyDataSetChanged();}
            }
        });
    }


    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView idelete, add, remove;
        public TextView iname, iprice, icount, itotal;
        public ViewHolder(View itemView) {
            super(itemView);
            this.idelete = itemView.findViewById(R.id.idelete);
            this.iname = itemView.findViewById(R.id.iname);
            this.iprice = itemView.findViewById(R.id.iprice);
            this.itotal = itemView.findViewById(R.id.itotal);
            this.icount = itemView.findViewById(R.id.icount);
            this.add = itemView.findViewById(R.id.iadd);
            this.remove = itemView.findViewById(R.id.iremove);
        }
    }
}