package com.nubar.jime.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;
import com.nubar.jime.Activities.activity_professional_person_details;
import com.nubar.jime.Classes.post;
import com.nubar.jime.R;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class search_appointments_adapters extends RecyclerView.Adapter<search_appointments_adapters.MyViewHolder> implements Filterable {

    Context context;
    List<post> post_list;
    List<post> example_post_list;
    double distance;
    Double plat, plongi;
    DecimalFormat precision = new DecimalFormat("0.00");
    private int lastPosition = -1;

    public search_appointments_adapters(Context context, List<post> post_list) {
        this.context = context;
        this.post_list = post_list;
        this.example_post_list = post_list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View listViewItem = inflater.inflate(R.layout.layout_available_posts_ticket, null, true);
        return new MyViewHolder(listViewItem);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        holder.spotTitle.setText(example_post_list.get(position).getPost_title());
        holder.SpostDescription.setText(example_post_list.get(position).getPost_description());
        plat = Double.valueOf(example_post_list.get(position).getLatitude());
        plongi = Double.valueOf(example_post_list.get(position).getLongitude());
        getdistance();
        holder.spostlocation.setText(precision.format(distance / 1000) + "KM Away");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, activity_professional_person_details.class);
                intent.putExtra("post_title", example_post_list.get(position).getPost_title());
                intent.putExtra("post_uid", example_post_list.get(position).getUid());
                intent.putExtra("post_latitude", example_post_list.get(position).getLatitude());
                intent.putExtra("post_longitude", example_post_list.get(position).getLongitude());
                intent.putExtra("post_description", example_post_list.get(position).getPost_description());
                context.startActivity(intent);
            }
        });
        setAnimation(holder.itemView, position);
    }

    @Override
    public int getItemCount() {
        return example_post_list.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charSequenceString = constraint.toString();
                if (charSequenceString.isEmpty()) {
                    example_post_list = post_list;
                } else {
                    List<post> filteredList = new ArrayList<>();
                    for (post name : post_list) {
                        if (name.getPost_title().toLowerCase().contains(charSequenceString.toLowerCase())) {
                            filteredList.add(name);
                        }
                        example_post_list = filteredList;
                    }
                }
                FilterResults results = new FilterResults();
                results.values = example_post_list;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                example_post_list = (List<post>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView spotTitle, SpostDescription, spostlocation;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            spotTitle = itemView.findViewById(R.id.stxtpostitle);
            SpostDescription = itemView.findViewById(R.id.spostdescription);
            spostlocation = itemView.findViewById(R.id.spostlocatin);
        }
    }

    private void setAnimation(View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            //TranslateAnimation anim = new TranslateAnimation(0,-1000,0,-1000);
            ScaleAnimation anim = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            //anim.setDuration(new Random().nextInt(501));//to make duration random number between [0,501)
            anim.setDuration(550);//to make duration random number between [0,501)
            viewToAnimate.startAnimation(anim);
            lastPosition = position;
        }
    }

    public void getdistance() {
        //getting location from shared_prefrence
        SharedPreferences myPrefs = context.getSharedPreferences("locationData", context.MODE_PRIVATE);
        String userlatitude = myPrefs.getString("latitude", "");
        String userlongitude = myPrefs.getString("longitude", "");

        LatLng from = new LatLng(Double.parseDouble(userlatitude), Double.parseDouble(userlongitude));
        LatLng to = new LatLng(plat, plongi);
        //Calculating the distance in meters
        distance = SphericalUtil.computeDistanceBetween(from, to);
    }
}
