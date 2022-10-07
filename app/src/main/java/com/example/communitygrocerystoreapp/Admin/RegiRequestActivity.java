package com.example.communitygrocerystoreapp.Admin;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.communitygrocerystoreapp.R;
import com.example.communitygrocerystoreapp.utils.UserAdapter;
import com.example.communitygrocerystoreapp.utils.UserModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

public class RegiRequestActivity extends AppCompatActivity {

    ArrayList<UserModel> myListData = new ArrayList<>();
    FirebaseFirestore fs = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regi_request);


        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        UserAdapter adapter = new UserAdapter(myListData, RegiRequestActivity.this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        fs.collection("User_Request").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (!queryDocumentSnapshots.isEmpty()) {
                    for (DocumentSnapshot ds : queryDocumentSnapshots) {
                        HashMap<String, String> house = (HashMap<String, String>) ds.get("Address");

                        myListData.add(new UserModel(
                                ds.getString("Email"),
                                ds.getString("First Name"),
                                ds.getString("Last Name"),
                                ds.getString("Pin"),
                                ds.getString("Phone"), house.get("House No"),
                                house.get("Housing Society Name"),
                                house.get("City"),
                                ds.getId()
                        ));
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        });

    }
}