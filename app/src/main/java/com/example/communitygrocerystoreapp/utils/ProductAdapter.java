package com.example.communitygrocerystoreapp.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.communitygrocerystoreapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder>{
    private ArrayList<ProductModel> listdata;
    Context context;

    // RecyclerView recyclerView;
    public ProductAdapter(ArrayList<ProductModel> listdata, Context context) {
        this.listdata = listdata;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.product_items, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final ProductModel myListData = listdata.get(position);
        holder.name.setText(listdata.get(position).name);
        holder.cate.setText(listdata.get(position).cate);
        holder.price.setText(String.valueOf(listdata.get(position).price));
        Glide.with(context).load(listdata.get(position).img).into(holder.imageView);

        holder.crd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, String> ss= new HashMap<>();
                ss.put("null", "null");
                Map<String, String> sss= new HashMap<>();
                sss.put("Name", myListData.name);
                sss.put("Category", myListData.cate);
                sss.put("Price", myListData.price);
                DocumentReference ds = FirebaseFirestore.getInstance().collection("Cart").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
                ds.set(ss);
                ds.collection("Items").document(myListData.uid).set(sss);
                Toast.makeText(context, "Item is Added to the cart!!", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public FrameLayout crd;
        public TextView name, price, cate;
        public ViewHolder(View itemView) {
            super(itemView);
            this.imageView = (ImageView) itemView.findViewById(R.id.img);
            this.name = (TextView) itemView.findViewById(R.id.name);
            this.price = (TextView) itemView.findViewById(R.id.price);
            this.cate = (TextView) itemView.findViewById(R.id.cate);
            this.crd = itemView.findViewById(R.id.addcart);
        }
    }
}