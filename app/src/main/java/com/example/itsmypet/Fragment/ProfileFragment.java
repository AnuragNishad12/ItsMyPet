package com.example.itsmypet.Fragment;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.itsmypet.Model.PetModel;
import com.example.itsmypet.R;
import com.example.itsmypet.databinding.FragmentProfileBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;


public class ProfileFragment extends Fragment {



    public ProfileFragment() {

    }
    FragmentProfileBinding binding;
    FirebaseAuth auth;
    DatabaseReference database;
    String userId;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

       binding = FragmentProfileBinding.inflate(inflater,container,false);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference();

        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser!=null){
            userId = currentUser.getUid();
            DatabaseReference userRef = database.child("PetRegister").child(userId);
            userRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        PetModel user = snapshot.getValue(PetModel.class);
                        if (user !=null){
                            binding.Rname.setText(user.getName());
                            binding.Raddress.setText(user.getAddress());
                            binding.RpinCode.setText(user.getPincode());
                            binding.Country.setText(user.getCountry());
                            Picasso.get().load(user.getImg()).into(binding.profileImage);
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }


        Query query =database.child("MReport").child(userId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    binding.button2.setVisibility(View.VISIBLE);
                }else {
                    binding.button2.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        binding.button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database.child("MReport").child(userId).removeValue();
            }
        });












       return binding.getRoot();
    }
}