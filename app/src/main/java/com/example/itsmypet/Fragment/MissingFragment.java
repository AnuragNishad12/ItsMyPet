package com.example.itsmypet.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.itsmypet.Adapter.RAdapter;
import com.example.itsmypet.Model.RModel;
import com.example.itsmypet.R;
import com.example.itsmypet.databinding.FragmentMissingBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class MissingFragment extends Fragment {


    public MissingFragment() {

    }
    FragmentMissingBinding binding;
    ArrayList<RModel> list = new ArrayList<>();
    FirebaseDatabase database;
    RAdapter adapter;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

       binding = FragmentMissingBinding.inflate(inflater,container,false);
        adapter = new RAdapter(list,getContext());
        binding.recyclerR.setAdapter(adapter);
        database = FirebaseDatabase.getInstance();

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(),2);
        binding.recyclerR.setLayoutManager(layoutManager);
        database.getReference().child("MReport").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    if (dataSnapshot.exists()) {
                        RModel petModel = dataSnapshot.getValue(RModel.class);
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


        return  binding.getRoot();
    }
}