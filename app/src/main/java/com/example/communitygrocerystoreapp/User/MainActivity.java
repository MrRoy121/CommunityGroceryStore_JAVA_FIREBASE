package com.example.communitygrocerystoreapp.User;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.communitygrocerystoreapp.R;
import com.example.communitygrocerystoreapp.SplashActivity;
import com.example.communitygrocerystoreapp.utils.ProductAdapter;
import com.example.communitygrocerystoreapp.utils.ProductModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    FirebaseFirestore fs = FirebaseFirestore.getInstance();
    FirebaseAuth fa = FirebaseAuth.getInstance();
    ArrayList<ProductModel> istda = new ArrayList<>();
    TextView bal;
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bal = findViewById(R.id.bal);
        uid = fa.getCurrentUser().getUid();

        fs.collection("Account").document(uid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    bal.setText(documentSnapshot.getString("Available"));
                }
            }
        });
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (task.isSuccessful()) {
                            String token = task.getResult();
                            Map<String, String>ss= new HashMap<>();
                            ss.put("token", token);
                            fs.collection("Token").document(uid).set(ss);
                        }
                    }
                });

        findViewById(R.id.orderHis).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, OrderHistoryActivity.class));
            }
        });

        findViewById(R.id.signout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);
                SharedPreferences.Editor myEdit = sharedPreferences.edit();
                myEdit.putString("email", "");
                myEdit.putString("pin", "");
                myEdit.putBoolean("user", false);
                myEdit.apply();
                finish();
                startActivity(new Intent(MainActivity.this, SplashActivity.class));
            }
        });

        findViewById(R.id.cart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent i = new Intent(MainActivity.this, CartActivity.class);
                i.putExtra("bal", bal.getText().toString());
                startActivity(i);
            }
        });

        findViewById(R.id.money).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(MainActivity.this);
                dialog.setContentView(R.layout.add_balance);

                EditText amount = dialog.findViewById(R.id.amount);
                Button add = dialog.findViewById(R.id.add);
                ProgressBar pbar = dialog.findViewById(R.id.pbar);

                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        pbar.setVisibility(View.VISIBLE);
                        if(amount.getText().length()==0){
                            Toast.makeText(MainActivity.this, "Please put a Amount to Add", Toast.LENGTH_SHORT).show();
                        }else{
                            Map<String, String> sas = new HashMap<>();
                            sas.put("Available", String.valueOf(Double.parseDouble(bal.getText().toString()) + Double.parseDouble(amount.getText().toString())));
                            fs.collection("Account").document(uid).set(sas).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    finish();
                                    Toast.makeText(MainActivity.this, "Credit Added!!", Toast.LENGTH_SHORT).show();
                                    startActivity(getIntent());
                                }
                            });
                        }
                    }
                });
                
            }
        });


        RecyclerView recyclerView = findViewById(R.id.productrv);
        ProductAdapter adapter = new ProductAdapter(istda, MainActivity.this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        fs.collection("Products").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot qs) {
                if(!qs.isEmpty()){
                    for(DocumentSnapshot ds: qs){
                        istda.add(new ProductModel(ds.getId(), ds.getString("Name"), ds.getString("image"), ds.getString("Category"), ds.getString("Price")));
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }
}