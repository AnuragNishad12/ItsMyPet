package com.example.itsmypet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.example.itsmypet.Fragment.MissingFragment;
import com.example.itsmypet.Model.RModel;
import com.example.itsmypet.databinding.ActivityDetailsBinding;
import com.example.itsmypet.databinding.ActivityMissingReportBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class MissingReport extends AppCompatActivity {

    ActivityMissingReportBinding binding;
    Uri image2;
    FirebaseAuth auth;
    FirebaseStorage storage;
    StorageReference storageReference;
    FirebaseDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMissingReportBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        database = FirebaseDatabase.getInstance();
        ProgressDialog progressDialog = new ProgressDialog(MissingReport.this);
        progressDialog.setTitle("Report Missing");
        progressDialog.setMessage("we're creating your Report");


        binding.profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iGallery = new Intent(Intent.ACTION_PICK);
                iGallery.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(iGallery,1);
            }
        });

        binding.RButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();

                String name = binding.Rname.getText().toString();
                String address = binding.Raddress.getText().toString();
                String pinCode = binding.RpinCode.getText().toString();
                String country = binding.Country.getText().toString();
                String details = binding.Rdetails.getText().toString();
                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(address) || TextUtils.isEmpty(pinCode) || TextUtils.isEmpty(country) || TextUtils.isEmpty(details)) {
                    Toast.makeText(MissingReport.this, "Please fill in all the fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (image2!=null){
                    storageReference = storage.getReference().child("Register2").child(auth.getUid());
                    storageReference.putFile(image2).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            progressDialog.dismiss();
                            if (task.isSuccessful()){
                                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String image = uri.toString();
                                        String uid = auth.getCurrentUser().getUid();
                                        RModel user = new RModel(image,name,address,pinCode,country,details);
                                        database.getReference().child("MReport").child(uid).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Intent intent1 = new Intent(MissingReport.this, MainActivity.class);
                                                startActivity(intent1);

                                            }

                                        });
                                    }
                                });

                            }
                        }
                    });
                }
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("MReport");
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            for (DataSnapshot snapshot1:snapshot.getChildren()){
                                RModel model = snapshot1.getValue(RModel.class);
                                if (model!=null){
                                    createNotification(model);

                                }
                            }
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            if (requestCode==1){
                image2 = data.getData();
                binding.profileImage.setImageURI(image2);
            }
        }
    }
    public void createNotification(RModel model) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "MyChannelId",
                    "MyChannelName",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(MissingReport.this, "MyChannelId")
                .setSmallIcon(R.drawable.ic_notifications)
                .setContentTitle("Pet Missing Report ")
                .setContentText("Name :" + model.getName() + " " + " Address: " + model.getAddress())
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);
        Intent intent = new Intent(MissingReport.this, MissingFragment.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(MissingReport.this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
        builder.setContentIntent(pendingIntent);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(MissingReport.this);
        notificationManagerCompat.notify(1, builder.build());
    }
}