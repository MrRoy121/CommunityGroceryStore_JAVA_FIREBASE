package com.example.communitygrocerystoreapp.User;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.communitygrocerystoreapp.R;
import com.example.communitygrocerystoreapp.utils.CartItemAdapter;
import com.example.communitygrocerystoreapp.utils.CartItemModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CartActivity extends AppCompatActivity {


    ArrayList<CartItemModel> istda = new ArrayList<>();
    TextView bal;
    ProgressBar pbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        bal = findViewById(R.id.bal);
        pbar = findViewById(R.id.pbar);
        bal.setText(getIntent().getStringExtra("bal"));
        RecyclerView recyclerView = findViewById(R.id.cartrv);
        CartItemAdapter adapter = new CartItemAdapter(istda, CartActivity.this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        FirebaseFirestore.getInstance().collection("Cart").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("Items").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot qu) {
                for (DocumentSnapshot ds : qu) {
                    CartItemModel dd = new CartItemModel(ds.getString("Name"), 1, ds.getString("Price"), ds.getId());
                    istda.add(dd);
                    adapter.notifyDataSetChanged();
                }
            }
        });

        findViewById(R.id.order).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double total = 0.0;
                for(CartItemModel cs : istda){
                    total = total + Double.parseDouble(cs.getPrice()) * cs.getCount();
                }
                double ball = Double.parseDouble(bal.getText().toString());
                if(ball>total && !(total == 0.0)){
                    Double finalTotal = total;
                    new AlertDialog.Builder(CartActivity.this)
                            .setTitle("Confirm Order")
                            .setMessage("Total Cost is " + total)

                            .setPositiveButton("Order",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog,
                                                            int which) {
                                            pbar.setVisibility(View.VISIBLE);
                                            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                            StringBuilder ss= new StringBuilder();
                                            for(CartItemModel dd: istda){
                                                if(ss.length()==0){
                                                    ss = new StringBuilder(dd.getName());
                                                }else{
                                                    ss.append(", ").append(dd.getName());
                                                }
                                            }
                                            Map<String, Object> all= new HashMap<>();
                                            all.put("Cart", ss.toString());
                                            all.put("Total", String.valueOf(finalTotal));
                                            FirebaseFirestore.getInstance().collection("Cart_Request").document(uid).set(all);
                                            DocumentReference ds = FirebaseFirestore.getInstance().collection("Cart").document(FirebaseAuth.getInstance().getUid());
                                            ds.collection("Items").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                @Override
                                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                    for (DocumentSnapshot q : queryDocumentSnapshots){
                                                        ds.collection("Items").document(q.getId()).delete();
                                                    }
                                                    ds.delete();
                                                }
                                            });
                                            Map<String, String> bal = new HashMap<>();
                                            bal.put("Available", String.valueOf(ball - finalTotal));
                                            FirebaseFirestore.getInstance().collection("Account").document(uid).set(bal).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    istda = new ArrayList<>();
                                                    adapter.notifyDataSetChanged();
                                                    Toast.makeText(CartActivity.this, "Payment Complete Order Succesfull\nWait for the delivery!", Toast.LENGTH_SHORT).show();
                                                    finish();
                                                    startActivity(new Intent(CartActivity.this,MainActivity.class));
                                                }
                                            });
                                           }
                                    })
                            .setNegativeButton("Cancel",
                                    new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog,
                                                            int which) {
                                            dialog.dismiss();
                                        }
                                    }).show();
                }else{
                    Toast.makeText(CartActivity.this, "Total Amount is Rs:"+total+ "\nNot Available in The Balance!!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        startActivity(new Intent(CartActivity.this,MainActivity.class));
    }
}