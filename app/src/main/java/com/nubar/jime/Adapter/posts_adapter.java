package com.nubar.jime.Adapter;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nubar.jime.Classes.post;
import com.nubar.jime.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class posts_adapter extends RecyclerView.Adapter<posts_adapter.MyViewHolder> {
    Context context;
    ArrayList<post> postlist;
    String uid;
    Geocoder geocoder;
    List<Address> addresses;
    private int lastPosition = -1;


    public posts_adapter(Context context, ArrayList<post> postlist, String uid) {
        this.context = context;
        this.postlist = postlist;
        this.uid = uid;

    }

    @NonNull
    @Override
    public posts_adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View listViewItem = inflater.inflate(R.layout.layout_new_posts_tickets, null, true);
        return new posts_adapter.MyViewHolder(listViewItem);
    }

    @Override
    public void onBindViewHolder(@NonNull posts_adapter.MyViewHolder holder, final int position) {
        holder.post_title.setText(postlist.get(position).getPost_title());
        holder.post_description.setText(postlist.get(position).getPost_description());

        Double lat = Double.valueOf(postlist.get(position).getLatitude());
        Double longi = Double.valueOf(postlist.get(position).getLongitude());

        geocoder = new Geocoder(context, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(lat, longi, 1);
            holder.post_location.setText(addresses.get(0).getAddressLine(0) + "");
        } catch (IOException e) {
            e.printStackTrace();
        }

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Are you sure?")
                        .setContentText("Won't be able to recover this post!")
                        .setConfirmText("No!")
                        .setCancelText("Yes,delete!")
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("UserPosts").child(postlist.get(position).getRandom_num());
                                ref.removeValue();
                                sweetAlertDialog.dismissWithAnimation();
                                new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                                        .setTitleText("All data deleted successfully!")
                                        .show();
                                postlist.remove(position);
                                notifyItemRemoved(position);
                            }
                        })
                        .show();
                return false;
            }
        });
        setAnimation(holder.itemView, position);
    }

    @Override
    public int getItemCount() {
        return postlist.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView post_title, post_description, post_location;
        //post location is the distance

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            post_title = itemView.findViewById(R.id.posttitle);
            post_description = itemView.findViewById(R.id.postdescription);
            post_location = itemView.findViewById(R.id.txtpostlocation);
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

}
