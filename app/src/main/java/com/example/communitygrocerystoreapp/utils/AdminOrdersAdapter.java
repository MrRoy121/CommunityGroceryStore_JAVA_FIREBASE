package com.example.communitygrocerystoreapp.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.communitygrocerystoreapp.Admin.OrdersActivity;
import com.example.communitygrocerystoreapp.R;
import com.example.communitygrocerystoreapp.SendNotificationPack.APIService;
import com.example.communitygrocerystoreapp.SendNotificationPack.Client;
import com.example.communitygrocerystoreapp.SendNotificationPack.Data;
import com.example.communitygrocerystoreapp.SendNotificationPack.MyResponse;
import com.example.communitygrocerystoreapp.SendNotificationPack.NotificationSender;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminOrdersAdapter extends RecyclerView.Adapter<AdminOrdersAdapter.ViewHolder>{

    ArrayList<HistoryModel> listdata;
    Context ctx;
    private APIService apiService;

    public AdminOrdersAdapter( ArrayList<HistoryModel> listdata, Context ctx) {
        this.listdata = listdata;
        this.ctx = ctx;
        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);
    }

    @Override
    public AdminOrdersAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.history_item, parent, false);
        AdminOrdersAdapter.ViewHolder viewHolder = new AdminOrdersAdapter.ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(AdminOrdersAdapter.ViewHolder holder, int position) {
        HistoryModel sss= listdata.get(position);
        holder.t1.setText(listdata.get(position).getAmount());
        holder.t2.setText(listdata.get(position).getItems());
        holder.uid.setVisibility(View.VISIBLE);
        holder.uid.setText("User Id: "+ listdata.get(position).uid);

        holder.ccc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(ctx)
                        .setTitle("Confirm Delivery")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                OrdersActivity.pbar.setVisibility(View.VISIBLE);
                                Map<String, Object> all= new HashMap<>();
                                all.put("Cart", sss.items);
                                all.put("Total", sss.amount);
                                all.put("User", sss.uid);
                                FirebaseFirestore.getInstance().collection("History").add(all).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        listdata.remove(sss);
                                        FirebaseFirestore.getInstance().collection("Cart_Request").document(sss.uid).delete();
                                        notifyDataSetChanged();
                                    }
                                });
                                FirebaseFirestore.getInstance().collection("Token").document(sss.uid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot d) {
                                        sendNotifications(d.getString("token"), "Products delivered", "The Product you have ordered from Community Grocery Store App is Delivered");
                                    }
                                });
                            }
                        }).setNegativeButton("No", null).show();
            }
        });

    }
    public void sendNotifications(String usertoken, String title, String message) {
        Data data = new Data(title, message);
        NotificationSender sender = new NotificationSender(data, usertoken);
        apiService.sendNotifcation(sender).enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                if (response.code() == 200) {
                    if (response.body().success != 1) {
                        Log.e("Respons", response.message());
                        OrdersActivity.pbar.setVisibility(View.INVISIBLE);
                    }else{
                        Toast.makeText(ctx, "Notification Delivered", Toast.LENGTH_SHORT).show();
                        OrdersActivity.pbar.setVisibility(View.INVISIBLE);
                    }
                }else{
                    Log.e("sdaas", String.valueOf(response.code()));
                    OrdersActivity.pbar.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onFailure(Call<MyResponse> call, Throwable t) {

                Log.e("sdds", t.getMessage());
            }
        });
    }

    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView t1, t2, uid;
        CardView ccc;
        public ViewHolder(View itemView) {
            super(itemView);
            this.t1 = itemView.findViewById(R.id.t1);
            this.t2 = itemView.findViewById(R.id.t2);
            this.uid = itemView.findViewById(R.id.uid);
            this.ccc = itemView.findViewById(R.id.ccc);
        }
    }
}