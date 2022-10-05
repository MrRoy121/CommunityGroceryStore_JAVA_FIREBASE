package com.example.communitygrocerystoreapp.User;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.communitygrocerystoreapp.R;
import com.example.communitygrocerystoreapp.utils.HistoryModel;
import com.example.communitygrocerystoreapp.utils.historyAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class OrderHistoryActivity extends AppCompatActivity {

    TextView t1, ttoal, items;
    FirebaseFirestore fs = FirebaseFirestore.getInstance();
    FirebaseAuth fa = FirebaseAuth.getInstance();
    CardView t2;
    ArrayList<HistoryModel> istda = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        t1 = findViewById(R.id.t1);
        t2 = findViewById(R.id.t2);
        ttoal = findViewById(R.id.ttotal);
        items = findViewById(R.id.items);

        fs.collection("Cart_Request").document(fa.getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot dt) {
                if(dt.exists()){
                    t1.setVisibility(View.VISIBLE);
                    t2.setVisibility(View.VISIBLE);
                    ttoal.setText(dt.getString("Total"));
                    items.setText(dt.getString("Cart"));
                }
            }
        });
        RecyclerView recyclerView = findViewById(R.id.productrv);
        historyAdapter adapter = new historyAdapter(istda, OrderHistoryActivity.this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        fs.collection("History").whereEqualTo("User", fa.getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
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