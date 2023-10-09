package com.example.itsmypet.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.itsmypet.DetailsActivity;
import com.example.itsmypet.Model.RModel;
import com.example.itsmypet.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RAdapter extends RecyclerView.Adapter<RAdapter.ViewHolder> {

    ArrayList<RModel> users;
    Context context;

    public RAdapter(ArrayList<RModel> users, Context context) {
        this.users = users;
        this.context = context;
    }



    @NonNull
    @Override
    public RAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.show_data,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RAdapter.ViewHolder holder, int position) {

        RModel list = users.get(position);
        Picasso.get().load(list.getImg()).placeholder(R.drawable.profile).into(holder.image);
        holder.name.setText(list.getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailsActivity.class);
                intent.putExtra("name",list.getName());
                intent.putExtra("address",list.getAddress());
                intent.putExtra("pincode",list.getpCode());
                intent.putExtra("details",list.getDetails());
                intent.putExtra("country",list.getCountry());
                intent.putExtra("image",list.getImg());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });



    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView name;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.profile_image);
            name = itemView.findViewById(R.id.GetPetName);
        }
    }
}

