package com.example.communitygrocerystoreapp.Admin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.communitygrocerystoreapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AddProductActivity extends AppCompatActivity {

    private final int PICK_IMAGE_REQUEST = 22;
    FirebaseFirestore fs = FirebaseFirestore.getInstance();
    ArrayList<String> cate = new ArrayList<>();
    TextView pimage, pcate;
    EditText pname, pprice;
    StorageReference storageReference;
    Button add;
    private ImageView imageView;
    private Uri filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);


        pname = findViewById(R.id.pname);
        pprice = findViewById(R.id.pprice);
        pcate = findViewById(R.id.pcate);
        pimage = findViewById(R.id.pimage);
        Spinner spin = findViewById(R.id.sp);
        imageView = findViewById(R.id.img);
        add = findViewById(R.id.add);
        ArrayAdapter<String> aa = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, cate);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(aa);

        fs.collection("Category").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (!queryDocumentSnapshots.isEmpty()) {
                    for (DocumentSnapshot ds : queryDocumentSnapshots) {
                        cate.add(ds.getString("Name"));
                        aa.notifyDataSetChanged();
                    }
                }
            }
        });

        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                view.setVisibility(View.INVISIBLE);
                pcate.setText(cate.get(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                adapterView.setVisibility(View.INVISIBLE);
            }
        });
        pimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectImage();
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImage();
            }
        });

    }

    private void SelectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(
                intent,
                "Select Image from here..."),
                PICK_IMAGE_REQUEST);
    }


    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null) {

            filePath = data.getData();
            try {

                Bitmap bitmap = MediaStore
                        .Images
                        .Media
                        .getBitmap(
                                getContentResolver(),
                                filePath);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }
    }


    private void uploadImage() {
        if (filePath != null && pname.length() != 0 && pprice.length() != 0) {

            ProgressDialog progressDialog
                    = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            UUID UUID = null;
            StorageReference ref = FirebaseStorage.getInstance().getReference().child("Products/" + java.util.UUID.randomUUID().toString());

            ref.putFile(filePath).addOnSuccessListener(
                    new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Map<String, String> pro = new HashMap<>();
                                    pro.put("Name", pname.getText().toString());
                                    pro.put("Price", pprice.getText().toString());
                                    pro.put("Category", pcate.getText().toString());
                                    pro.put("image", uri.toString());
                                    fs.collection("Products").add(pro);
                                    progressDialog.dismiss();
                                    Toast.makeText(AddProductActivity.this, "Product Uploaded!!", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            });

                        }
                    })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            progressDialog.dismiss();
                            Toast
                                    .makeText(AddProductActivity.this,
                                            "Failed " + e.getMessage(),
                                            Toast.LENGTH_SHORT)
                                    .show();
                        }
                    });
        } else {
            Toast.makeText(AddProductActivity.this, "All Fields Are Required", Toast.LENGTH_SHORT).show();
        }
    }
}