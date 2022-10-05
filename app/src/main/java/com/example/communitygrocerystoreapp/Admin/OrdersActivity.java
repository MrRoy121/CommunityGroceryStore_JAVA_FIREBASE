package com.example.communitygrocerystoreapp.Admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ProgressBar;

import com.example.communitygrocerystoreapp.R;
import com.example.communitygrocerystoreapp.utils.AdminOrdersAdapter;
import com.example.communitygrocerystoreapp.utils.HistoryModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class OrdersActivity extends AppCompatActivity {


    FirebaseFirestore fs = FirebaseFirestore.getInstance();
    ArrayList<HistoryModel> istda = new ArrayList<>();
    public static ProgressBar pbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        pbar = findViewById(R.id.pbar);
        RecyclerView recyclerView = findViewById(R.id.productrv);
        AdminOrdersAdapter adapter = new AdminOrdersAdapter(istda, OrdersActivity.this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        fs.collection("Cart_Request").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot qs) {
                if(!qs.isEmpty()){
                    for(DocumentSnapshot ds: qs){
                        istda.add(new HistoryModel(ds.getString("Total"), ds.getString("Cart"), ds.getId()));
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }
}