package com.nubar.jime.Fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nubar.jime.Adapter.customers_appointment_adapter;
import com.nubar.jime.Classes.userAppointmentSchedule;
import com.nubar.jime.Classes.userAvailablitySchedule;
import com.nubar.jime.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class customers extends Fragment {
    public static customers newInstance() {
        customers fragment = new customers();
        return fragment;
    }

    RecyclerView recyclerViewcustomers;
    public SweetAlertDialog pDialog;
    List<userAppointmentSchedule> list;
    FirebaseAuth firebaseAuth;
    customers_appointment_adapter customers_appointment_adapter;
    String arr[] = {"saturday", "sunday", "monday", "tuesday", "wednesday", "thursday", "friday"};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_fragment_customers, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        list = new ArrayList<>();
        firebaseAuth = FirebaseAuth.getInstance();
        pDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#faa805"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        recyclerViewcustomers = getActivity().findViewById(R.id.recyclerviewcustomers);

        for (int i = 0; i < arr.length; i++) {
            get_status((String) Array.get(arr, i));
            customers_appointment_adapter = new customers_appointment_adapter(getContext(), list);
            recyclerViewcustomers.setLayoutManager(new GridLayoutManager(getContext(), 1));
            recyclerViewcustomers.setAdapter(customers_appointment_adapter);
        }
    }

    public void get_status(final String dayofweek) {
        pDialog.show();
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference query = rootRef.child("AppointmentSlots").child(firebaseAuth.getUid()).child(dayofweek);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //    list.clear();
                if (dataSnapshot.exists()) {
                    userAppointmentSchedule u = dataSnapshot.getValue(userAppointmentSchedule.class);
                    list.add(u);
                    pDialog.dismiss();
                } else {
                    pDialog.dismiss();
                }
                pDialog.dismiss();
                customers_appointment_adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}
