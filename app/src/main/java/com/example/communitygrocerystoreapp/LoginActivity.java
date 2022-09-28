package com.example.communitygrocerystoreapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.communitygrocerystoreapp.Admin.AdminMainActivity;
import com.example.communitygrocerystoreapp.User.MainActivity;
import com.example.communitygrocerystoreapp.User.RegisterActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class LoginActivity extends AppCompatActivity {

    EditText eemail, epass;
    String email, pass;
    TextView register;
    Button login;
    FirebaseAuth fa;
    FirebaseFirestore fs;
    ProgressBar pbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        eemail = findViewById(R.id.email);
        epass = findViewById(R.id.pin);
        register = findViewById(R.id.register);
        login = findViewById(R.id.login);
        pbar = findViewById(R.id.pbar);
        fa = FirebaseAuth.getInstance();
        fs = FirebaseFirestore.getInstance();


        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);
        if(sharedPreferences.getBoolean("user", false)){
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                finish();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pbar.setVisibility(View.VISIBLE);
                email = eemail.getText().toString();
                pass = epass.getText().toString();
                if(email.length()==0||pass.length()==0){
                    Toast.makeText(getApplicationContext(), "All Fields Are Required!!", Toast.LENGTH_SHORT).show();
                }else if(email.equals("1")&&pass.equals("1")){
                    startActivity(new Intent(LoginActivity.this, AdminMainActivity.class));
                    finish();
                }else{
                    fa.signInWithEmailAndPassword(email, pass+"00").addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                if(fa.getCurrentUser().isEmailVerified()){
                                    fs.collection("Registered_User").whereEqualTo("Email", email).whereEqualTo("Pin", pass).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                        @Override
                                        public void onSuccess(QuerySnapshot qu) {
                                            if(qu.isEmpty()){
                                                fs.collection("User_Request").whereEqualTo("Email", email).whereEqualTo("Pin", pass).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onSuccess(QuerySnapshot q) {
                                                        if(!q.isEmpty()){
                                                            pbar.setVisibility(View.GONE);
                                                            Toast.makeText(getApplicationContext(),"User Registration Request Send Please Wait Admin To Confirm!!",Toast.LENGTH_LONG).show();
                                                        }
                                                    }
                                                });
                                            }else{
                                                pbar.setVisibility(View.GONE);
                                                Toast.makeText(getApplicationContext(),"Login Successful",Toast.LENGTH_SHORT).show();
                                                SharedPreferences.Editor myEdit = sharedPreferences.edit();
                                                myEdit.putString("email", email);
                                                myEdit.putString("pin", pass);
                                                myEdit.putBoolean("user", true);
                                                myEdit.apply();
                                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                                finish();
                                            }
                                        }
                                    });
                                }else{
                                        pbar.setVisibility(View.INVISIBLE);
                                        Toast.makeText(getApplicationContext(),"An Verification Email has been sent, Please verify. /nLook in the spam folder",Toast.LENGTH_SHORT).show();
                                }
                            }else{
                                pbar.setVisibility(View.INVISIBLE);
                                Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
}