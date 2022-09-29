package com.example.communitygrocerystoreapp.User;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.communitygrocerystoreapp.LoginActivity;
import com.example.communitygrocerystoreapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    EditText efname, elname, eemail, epass, ephone, ehno, ehname, ecity;
    String email, fname, lname, pass, phone, hno, hname, city;
    Button register;
    ProgressBar pbar;

    FirebaseFirestore fs;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        eemail = findViewById(R.id.email);
        efname = findViewById(R.id.fname);
        elname = findViewById(R.id.lname);
        ephone = findViewById(R.id.mobile);
        ehno = findViewById(R.id.hno);
        ehname = findViewById(R.id.hname);
        ecity = findViewById(R.id.city);
        epass = findViewById(R.id.pin);
        register = findViewById(R.id.register);
        pbar = findViewById(R.id.pbar);
        fs = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();


        findViewById(R.id.login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
            }
        });


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pbar.setVisibility(View.VISIBLE);
                email = eemail.getText().toString();
                fname = efname.getText().toString();
                pass = epass.getText().toString();
                hno = ehno.getText().toString();
                lname = elname.getText().toString();
                hname = ehname.getText().toString();
                city = ecity.getText().toString();
                phone = ephone.getText().toString();

                Map<String, Object> house = new HashMap<>();
                house.put("House No", hno);
                house.put("Housing Society Name", hname);
                house.put("City", city);
                Map<String, Object> user = new HashMap<>();
                user.put("Email", email);
                user.put("First Name", fname);
                user.put("Last Name", lname);
                user.put("Pin", pass);
                user.put("Phone", phone);
                user.put("Address", house);
                if (email.length() == 0 || fname.length() == 0 || pass.length() == 0 || lname.length() == 0 || phone.length() == 0 || hno.length() == 0 || city.length() == 0 || hname.length() == 0) {
                    pbar.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "All Fields Are Required!!", Toast.LENGTH_SHORT).show();
                } else if (pass.length() != 4) {
                    pbar.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "Pin Must Be 4 Number!!", Toast.LENGTH_SHORT).show();
                } else {
                    auth.createUserWithEmailAndPassword(email, pass + "00").addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                task.getResult().getUser().sendEmailVerification();
                                    fs.collection("User_Request").document(task.getResult().getUser().getUid()).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            pbar.setVisibility(View.GONE);
                                            Toast.makeText(getApplicationContext(), "A Verification email is send Pleas verify first!", Toast.LENGTH_LONG).show();
                                            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                            finish();
                                        }
                                    });
                            } else {
                                pbar.setVisibility(View.INVISIBLE);
                                Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                }
            }
        });
    }
}

