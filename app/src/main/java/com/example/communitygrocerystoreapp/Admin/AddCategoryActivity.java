package com.example.communitygrocerystoreapp.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.communitygrocerystoreapp.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddCategoryActivity extends AppCompatActivity {

    ArrayAdapter<String> arr;
    FirebaseFirestore fs = FirebaseFirestore.getInstance();
    ArrayList<String> cate = new ArrayList<>();
    EditText ecat;
    Button add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);


        ecat = findViewById(R.id.ecat);
        add = findViewById(R.id.add);
        ListView l = findViewById(R.id.lview);
        arr = new ArrayAdapter<String>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, cate);
        l.setAdapter(arr);

        fs.collection("Category").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(!queryDocumentSnapshots.isEmpty()){
                    for(DocumentSnapshot ds : queryDocumentSnapshots){
                        cate.add(ds.getString("Name"));
                        arr.notifyDataSetChanged();
                    }
                }
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ecat.getText().length()==0){
                    Toast.makeText(AddCategoryActivity.this, "Category Field is Empty!!", Toast.LENGTH_SHORT).show();
                }else{
                    Map<String, String> ca = new HashMap<>();
                    ca.put("Name", ecat.getText().toString());
                    fs.collection("Category").add(ca);
                    finish();
                    startActivity(getIntent());
                }
            }
        });
    }
}