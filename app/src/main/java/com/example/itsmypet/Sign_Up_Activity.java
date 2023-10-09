package com.example.itsmypet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.example.itsmypet.Model.SignModel;
import com.example.itsmypet.databinding.ActivitySignUpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class Sign_Up_Activity extends AppCompatActivity {

  ActivitySignUpBinding binding;
    FirebaseFirestore db;
    FirebaseAuth auth;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding =ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
       getSupportActionBar().hide();
        progressDialog =new ProgressDialog(Sign_Up_Activity.this);
        progressDialog.setTitle("Creating Account");
        progressDialog.setMessage("we're creating your Account");
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();


        binding.LogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Sign_Up_Activity.this , Log_In_Activity.class);
                startActivity(intent);
                finishAffinity();
            }
        });

//        if (auth.getCurrentUser()!=null){
//            Intent intent = new Intent(Sign_Up_Activity.this,MainActivity.class);
//            startActivity(intent);
//            finish();
//        }

        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                String email = binding.email.getText().toString().trim();
                String password = binding.passwords.getText().toString().trim();
                String name = binding.editTextTextPersonName.getText().toString().trim();
                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(name) ) {
                    Toast.makeText(Sign_Up_Activity.this, "Please fill in all the fields", Toast.LENGTH_SHORT).show();
                    return;
                }
                auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {

                            String userId = Objects.requireNonNull(auth.getCurrentUser()).getUid();
                            SignModel user = new SignModel(name,email,password);
                            db.collection("users").document(userId).set(user);
                            Intent intent = new Intent(Sign_Up_Activity.this, Register.class);
                            startActivity(intent);
                            finishAffinity();
                        } else {
                            Exception exception = task.getException();

                        }

                    }
                });
            }
        });
    }
}