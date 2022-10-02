package com.example.communitygrocerystoreapp.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.communitygrocerystoreapp.R;

public class AdminMainActivity extends AppCompatActivity {

    Button regi_req, add_cate, add_prdct, edit_prdct, orders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);

        regi_req = findViewById(R.id.regi_req);
        add_cate = findViewById(R.id.add_Category);
        add_prdct = findViewById(R.id.add_prduct);
        orders = findViewById(R.id.deli);
        edit_prdct = findViewById(R.id.edit_prduct);


        add_cate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminMainActivity.this, AddCategoryActivity.class));
            }
        });
        add_prdct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminMainActivity.this, AddProductActivity.class));
            }
        });
        regi_req.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminMainActivity.this, RegiRequestActivity.class));
            }
        });
        orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminMainActivity.this, OrdersActivity.class));
            }
        });
        edit_prdct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminMainActivity.this, EditProductPriceActivity.class));
            }
        });
    }
}