package com.nubar.jime.Fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.nubar.jime.Adapter.posts_adapter;
import com.nubar.jime.Classes.User;
import com.nubar.jime.Classes.post;
import com.nubar.jime.Classes.userCareerinfo;
import com.nubar.jime.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class createAppointment extends Fragment implements OnMapReadyCallback {
    public static createAppointment newInstance() {
        createAppointment fragment = new createAppointment();
        return fragment;
    }

    FloatingActionButton add_post;
    FirebaseAuth firebaseAuth;
    RecyclerView recyclerView;
    ArrayList<post> postslist;
    posts_adapter posts_adapter;
    public SweetAlertDialog pDialog;
    GoogleMap gMap;
    int AUTOCOMPLETE_REQUEST_CODE = 1;
    EditText auto_complete_box;
    MapView mMapView;
    double dlat;
    double dlong;
    List<Address> address;
    List<Address> addresss;
    List<Address> newaddresss;
    String latitude;
    String longitude;
    Geocoder geocoder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_fragment_createappointment, container, false);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        inintViews();
        if (!Places.isInitialized()) {
            Places.initialize(getContext(), "AIzaSyCgfbBqghK8_NNSCzw_LQalhmgwrm4TA2w");
        }
        pDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#faa805"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(true);
        pDialog.show();
        firebaseAuth = FirebaseAuth.getInstance();
        postslist = new ArrayList<>();
        posts_adapter = new posts_adapter(getContext(), postslist, firebaseAuth.getUid());
        get_post_data();
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
        recyclerView.setAdapter(posts_adapter);

        add_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Pay 1\uD83D\uDC8E?")
                        .setContentText("You have to pay one crystal only to post!")
                        .setConfirmText("No!")
                        .setCancelText("Pay 1\uD83D\uDC8E")
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {

                                SharedPreferences myPrefs = getActivity().getSharedPreferences("userprofiledata", getActivity().MODE_PRIVATE);
                                int total_crystals = Integer.parseInt(myPrefs.getString("Total_Crystals", "0"));

                                if (total_crystals < 1) {
                                    sweetAlertDialog.dismissWithAnimation();
                                    new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
                                            .setTitleText("Empty wallet")
                                            .setContentText("Please buy crystals to post!")
                                            .show();

                                } else {
                                    total_crystals -= 1;
                                    update_crystals_db(total_crystals);
                                    sweetAlertDialog.dismissWithAnimation();
                                    show_newPost_dialog();
                                }
                            }
                        })
                        .show();
            }
        });

    }

    public void update_crystals_db(final int remaining_crystls) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("userCareerinfo");
        ref.child(firebaseAuth.getUid()).child("info").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    dataSnapshot.getRef().child("total_crystals").setValue(remaining_crystls + "");
                    SharedPreferences.Editor editor = getContext().getSharedPreferences("userprofiledata", getActivity().MODE_PRIVATE).edit();
                    editor.putString("Total_Crystals", remaining_crystls + "");
                    editor.apply();
                } else {
                    Toast.makeText(getContext(), "unknown error occurred", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void inintViews() {
        add_post = getActivity().findViewById(R.id.add_new_post);
        recyclerView = getActivity().findViewById(R.id.myposts_recyclerview);

    }

    public void show_newPost_dialog() {
        //getting location from shared_prefrence
        SharedPreferences myPrefs = getActivity().getSharedPreferences("locationData", getActivity().MODE_PRIVATE);
        latitude = myPrefs.getString("latitude", "");
        longitude = myPrefs.getString("longitude", "");


        //latling in int
        dlat = Double.parseDouble(latitude);
        dlong = Double.parseDouble(longitude);

        //binding the dialog box design
        final Dialog dialog = new Dialog(getContext());
        //we don't want the dialog title
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        //dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dialog.setContentView(R.layout.layout_newpost_dialog);
        dialog.setCancelable(false);
        dialog.show();
        final EditText postTitle = (EditText) dialog.findViewById(R.id.txtpostitle);
        final EditText postDescription = (EditText) dialog.findViewById(R.id.txtpostdesc);
        final Button newpostbtn = dialog.findViewById(R.id.btnaddnewpost);
        auto_complete_box = (EditText) dialog.findViewById(R.id.locationenter);
        auto_complete_box.setFocusable(false);

        geocoder = new Geocoder(getContext(), Locale.getDefault());
        try {
            addresss = geocoder.getFromLocation(dlat, dlong, 1);
            auto_complete_box.setText(addresss.get(0).getAddressLine(0) + "");
        } catch (IOException e) {
            e.printStackTrace();
        }


        mMapView = (MapView) dialog.findViewById(R.id.mapView);
        MapsInitializer.initialize(getActivity());
        mMapView.onCreate(dialog.onSaveInstanceState());
        mMapView.onResume();
        mMapView.getMapAsync(this);


        auto_complete_box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSearchCalled();
            }
        });


        newpostbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (postTitle.getText().toString().isEmpty() || postDescription.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "Please enter all details", Toast.LENGTH_SHORT).show();
                } else {
                    final int random = new Random().nextInt((1000000 - 0) + 1) + 0;
                    DatabaseReference dref = FirebaseDatabase.getInstance().getReference("UserPosts").child(String.valueOf(random));
                    post post = new post(postTitle.getText().toString(), postDescription.getText().toString(), String.valueOf(random), latitude, longitude, firebaseAuth.getUid());
                    dref.setValue(post);
                    new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Success")
                            .setContentText("Posted successfully!")
                            .show();
                    dialog.dismiss();
                }
            }
        });


    }

    public void get_post_data() {
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        Query query1 = rootRef.child("UserPosts").orderByChild("uid").equalTo(firebaseAuth.getUid());
        query1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    postslist.clear();
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        post u = ds.getValue(post.class);
                        postslist.add(u);
                    }
                    posts_adapter.notifyDataSetChanged();
                    pDialog.dismiss();
                } else {
                    pDialog.dismiss();
                    //Toast.makeText(getActivity(), "Currently you have no posts", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void onSearchCalled() {
        // Set the fields to specify which types of place data to return.
        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG);
        // Start the autocomplete intent.
        Intent intent = new Autocomplete.IntentBuilder(
                AutocompleteActivityMode.OVERLAY, fields)
                .build(getContext());
        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                auto_complete_box.setText(place.getAddress());

                Geocoder coder = new Geocoder(getContext());
                try {
                    address = coder.getFromLocationName(place.getAddress(), 5);
                    dlat = address.get(0).getLatitude();
                    dlong = address.get(0).getLongitude();
                    latitude = String.valueOf(dlat);
                    longitude = String.valueOf(dlong);
                } catch (IOException e) {
                    Toast.makeText(getContext(), "" + e, Toast.LENGTH_SHORT).show();
                }
                mMapView.getMapAsync(this);
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.i("TAG", status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;
        gMap.clear();
        LatLng latLng = new LatLng(dlat, dlong);
        gMap.addMarker(new MarkerOptions().position(latLng).title("Location"));
        gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
        gMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {
                gMap.clear();
                MarkerOptions marker = new MarkerOptions().position(new LatLng(point.latitude, point.longitude)).title("Location");
                gMap.addMarker(marker);
                dlat = point.longitude;
                dlong = point.longitude;
                latitude = String.valueOf(dlat);
                longitude = String.valueOf(dlong);
                //on response to map click
                geocoder = new Geocoder(getContext(), Locale.getDefault());
                try {
                    newaddresss = geocoder.getFromLocation(dlat, dlong, 1);
                    auto_complete_box.setText(newaddresss.get(0).getAddressLine(0) + "");
                    newaddresss.clear();
                } catch (IOException e) {
                    Toast.makeText(getContext(), "" + e, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }
}
