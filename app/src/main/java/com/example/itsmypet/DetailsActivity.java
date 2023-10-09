package com.example.itsmypet;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.itsmypet.databinding.ActivityDetailsBinding;
import com.squareup.picasso.Picasso;

public class DetailsActivity extends AppCompatActivity {

    ActivityDetailsBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getActionBar().hide();
        String name = getIntent().getStringExtra("name");
        String country = getIntent().getStringExtra("country");
        String address = getIntent().getStringExtra("address");
        String details = getIntent().getStringExtra("details");
        String pincode = getIntent().getStringExtra("pincode");
        String img = getIntent().getStringExtra("img");
        Picasso.get().load(img).placeholder(R.drawable.profile).into(binding.profileImage);
        binding.Country.setText(country);
        binding.RpinCode.setText(pincode);
        binding.Raddress.setText(address);
        binding.Rdetails.setText(details);
        binding.Rname.setText(name);
    }
}