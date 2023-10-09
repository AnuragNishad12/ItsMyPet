package com.example.itsmypet.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.itsmypet.MissingReport;
import com.example.itsmypet.Adapter.PetAdapter;
import com.example.itsmypet.Model.PetModel;
import com.example.itsmypet.databinding.FragmentHomeBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class HomeFragment extends Fragment {

    ArrayList<PetModel> list = new ArrayList<>();
    FirebaseDatabase database;
    PetAdapter adapter;
   FragmentHomeBinding binding;

    public HomeFragment() {

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       binding = FragmentHomeBinding.inflate(inflater,container,false);
        adapter = new PetAdapter(list,getContext());
        binding.recyclerView.setAdapter(adapter);
        database = FirebaseDatabase.getInstance();

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(),2);
        binding.recyclerView.setLayoutManager(layoutManager);


        database.getReference().child("PetRegister").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    if (dataSnapshot.exists()) {
                        PetModel petModel = dataSnapshot.getValue(PetModel.class);
                        if (petModel != null) {
                            list.add(petModel);
                        }
                    }
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseError", "Error: " + error.getMessage());
            }
        });

        binding.floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              Intent intent = new Intent(getActivity(),MissingReport.class);
              startActivity(intent);
            }
        });












        return binding.getRoot();
    }
}