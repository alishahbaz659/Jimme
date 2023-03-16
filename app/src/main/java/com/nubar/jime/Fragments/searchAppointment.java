package com.nubar.jime.Fragments;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.android.SphericalUtil;
import com.nubar.jime.Adapter.search_appointments_adapters;
import com.nubar.jime.Classes.post;
import com.nubar.jime.R;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class searchAppointment extends Fragment {
    public static searchAppointment newInstance() {
        searchAppointment fragment = new searchAppointment();
        return fragment;
    }

    RecyclerView recyclerView;
    search_appointments_adapters search_appointments_adapters;
    List<post> postlistdb;
    public SweetAlertDialog pDialog;
    FirebaseAuth firebaseAuth;
    SearchView searchViewappointment;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_fragment_searchappointment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recyclerView = getActivity().findViewById(R.id.avaiable_posts_recyclerview1);
        searchViewappointment = getActivity().findViewById(R.id.searchViewappointment);
        postlistdb = new ArrayList<>();
        firebaseAuth = FirebaseAuth.getInstance();
        pDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#faa805"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        get_post_data();
        search_appointments_adapters = new search_appointments_adapters(getContext(), postlistdb);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
        recyclerView.setAdapter(search_appointments_adapters);
        //for searching
        searchViewappointment.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                search_appointments_adapters.getFilter().filter(newText);
                return false;
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    public void get_post_data() {
        pDialog.show();
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        Query query1 = rootRef.child("UserPosts");
        query1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    postlistdb.clear();
                    //get only those posts which does not contain current user signed in value
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        if (!ds.child("uid").getValue().equals(firebaseAuth.getUid())) {
                            post u = ds.getValue(post.class);
                            postlistdb.add(u);
                        }
                    }
                    search_appointments_adapters.notifyDataSetChanged();
                    pDialog.dismiss();
                } else {
                    pDialog.dismiss();
                    // Toast.makeText(getContext(), "No posts are available in 30KM", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
