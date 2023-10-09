package com.example.itsmypet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.example.itsmypet.Model.PetModel;
import com.example.itsmypet.databinding.ActivityRegisterBinding;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class Register extends AppCompatActivity {

    ActivityRegisterBinding binding;
    Uri image;
    FirebaseAuth auth;
    FirebaseStorage storage;
    StorageReference storageReference;
    FirebaseDatabase database;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        binding.profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iGallery = new Intent(Intent.ACTION_PICK);
                iGallery.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(iGallery,1);
            }
        });

        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        database = FirebaseDatabase.getInstance();
        ProgressDialog progressDialog = new ProgressDialog(Register.this);
        progressDialog.setTitle("Registering");
        progressDialog.setMessage("we're Registering your Report");


        binding.profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iGallery = new Intent(Intent.ACTION_PICK);
                iGallery.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(iGallery,1);
            }
        });


        binding.register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                String name = binding.petName.getText().toString();
                String address = binding.petAddress.getText().toString();
                String pinCode = binding.petPincode.getText().toString();
                String country = binding.country.getText().toString();

                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(address) || TextUtils.isEmpty(pinCode) || TextUtils.isEmpty(country)) {
                    Toast.makeText(Register.this, "Please fill in all the fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (image!=null){

                    storageReference = storage.getReference().child("Register").child(auth.getUid());
                    storageReference.putFile(image).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            progressDialog.dismiss();
                            if (task.isSuccessful()){
                                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String image = uri.toString();
                                        String uid = auth.getCurrentUser().getUid();
                                        PetModel user = new PetModel(name,address,pinCode,country,image);
                                        database.getReference().child("PetRegister").child(uid).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Intent intent1 = new Intent(Register.this, MainActivity.class);
                                                startActivity(intent1);
                                                finishAffinity();
                                            }
                                        });
                                    }
                                });
                            }
                        }
                    });
                }
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            if (requestCode==1){
                image = data.getData();
                binding.profileImage.setImageURI(image);
            }
        }
    }

}