package com.example.communitygrocerystoreapp.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.communitygrocerystoreapp.R;
import com.example.communitygrocerystoreapp.utils.ProductModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class EditProductPriceActivity extends AppCompatActivity {


    ArrayList<ProductModel> ssss = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product_price);

        ListView idd = findViewById(R.id.product_id);
        ArrayAdapter arr = new ArrayAdapter (EditProductPriceActivity.this, android.R.layout.simple_list_item_2, android.R.id.text1, ssss)
        {
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                TextView text2 = (TextView) view.findViewById(android.R.id.text2);
                text2.setText(ssss.get(position).getUid());
                text1.setText(ssss.get(position).getName());
                return view;
            }
        };
        idd.setAdapter(arr);

        FirebaseFirestore.getInstance().collection("Products").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(!queryDocumentSnapshots.isEmpty()){
                    for(DocumentSnapshot dd: queryDocumentSnapshots){
                        ssss.add( new ProductModel(dd.getId(), dd.getString("Name")));
                        arr.notifyDataSetChanged();
                    }
                }
            }
        });

        findViewById(R.id.excel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EditProductPriceActivity.this, ExcelSheet.class));
            }
        });


        findViewById(R.id.notepad).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EditProductPriceActivity.this, Notepad.class));
            }
        });




    }
}