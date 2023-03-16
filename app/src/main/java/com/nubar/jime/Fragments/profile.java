package com.nubar.jime.Fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.nubar.jime.Activities.activity_calender;
import com.nubar.jime.Activities.activity_purchase_crystals;
import com.nubar.jime.Classes.User;
import com.nubar.jime.Classes.userAvailablitySchedule;
import com.nubar.jime.Classes.userCareerinfo;
import com.nubar.jime.R;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class profile extends Fragment {
    public static profile newInstance() {
        profile fragment = new profile();
        return fragment;
    }

    ImageView profile_image, img_buy_morecrystals;
    EditText profile_name, profile_email, carrier_location, user_total_crystals, user_fee;
    Spinner carrier;
    LinearLayout calender;
    FirebaseAuth firebaseAuth;
    ArrayList<User> userlist;
    Button updateProfile;
    ArrayList<userCareerinfo> userCareerinfoslist;
    public SweetAlertDialog pDialog;
    int AUTOCOMPLETE_REQUEST_CODE = 1;
    String proifle_crys;
    SharedPreferences.Editor editor;
    SharedPreferences.Editor profile_personal_data;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_fragment_profile, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (!Places.isInitialized()) {
            Places.initialize(getContext(), "AIzaSyCgfbBqghK8_NNSCzw_LQalhmgwrm4TA2w");
        }
        pDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#faa805"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        //intializing shareprefrence
        editor = getActivity().getSharedPreferences("userprofiledata", getActivity().MODE_PRIVATE).edit();

        profile_personal_data = getActivity().getSharedPreferences("userpersonaldetails", getActivity().MODE_PRIVATE).edit();

        firebaseAuth = FirebaseAuth.getInstance();
        userlist = new ArrayList<>();
        userCareerinfoslist = new ArrayList<>();
        pDialog.show();
        initViews();
        calender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), activity_calender.class));
            }
        });
        updateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences myPrefs = getActivity().getSharedPreferences("userprofiledata", getActivity().MODE_PRIVATE);
                boolean existance = myPrefs.contains("Total_Crystals");
                String value = myPrefs.getString("Total_Crystals", "");
                if (!existance) {
                    proifle_crys = "0";
                } else {
                    proifle_crys = value;
                }

                DatabaseReference dref = FirebaseDatabase.getInstance().getReference("userCareerinfo").child(firebaseAuth.getUid()).child("info");
                userCareerinfo userCareerinfo = new userCareerinfo(carrier.getSelectedItemPosition(), carrier_location.getText().toString(), proifle_crys, user_fee.getText().toString());
                dref.setValue(userCareerinfo);
                new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Success")
                        .setContentText("Profile updated successfully!")
                        .show();
            }
        });
        carrier_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSearchCalled();
            }
        });
        img_buy_morecrystals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), activity_purchase_crystals.class));
            }
        });

    }

    public void initViews() {
        profile_image = getActivity().findViewById(R.id.profile_image);
        profile_name = getActivity().findViewById(R.id.profilename);
        profile_email = getActivity().findViewById(R.id.profileemail);
        calender = getActivity().findViewById(R.id.calender);
        carrier = getActivity().findViewById(R.id.carrier);
        carrier_location = getActivity().findViewById(R.id.workingplace);
        carrier_location.setFocusable(false);
        updateProfile = getActivity().findViewById(R.id.updateprofilebtn);
        img_buy_morecrystals = getActivity().findViewById(R.id.img_buyCrystals);
        user_total_crystals = getActivity().findViewById(R.id.user_total_crystals);
        user_fee = getActivity().findViewById(R.id.fees);
    }

    @Override
    public void onStart() {
        super.onStart();
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        Query query = rootRef.child("Users").orderByChild("uid").equalTo(firebaseAuth.getUid());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userlist.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    User u = ds.getValue(User.class);
                    userlist.add(u);
                }
                profile_name.setText(userlist.get(0).getUsername());
                profile_email.setText(userlist.get(0).getEmail());
                String photurl = userlist.get(0).getPhotourl();
                photurl = photurl + "?type=large";
                Picasso.get().load(photurl).into(profile_image);
                pDialog.dismiss();
                //saving values to shared prefrence
                profile_personal_data.putString("userEmail", userlist.get(0).getEmail());
                profile_personal_data.putString("userName", userlist.get(0).getUsername());
                profile_personal_data.apply();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        DatabaseReference dbref = FirebaseDatabase.getInstance().getReference();
        DatabaseReference quer1 = dbref.child("userCareerinfo").child(firebaseAuth.getUid()).child("info");
        quer1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userCareerinfoslist.clear();
                if (dataSnapshot.exists()) {
                    userCareerinfo u = dataSnapshot.getValue(userCareerinfo.class);
                    userCareerinfoslist.add(u);

                    carrier.setSelection(userCareerinfoslist.get(0).getCareername());
                    carrier_location.setText(userCareerinfoslist.get(0).getCareerlocation());
                    user_fee.setText(userCareerinfoslist.get(0).getFee());
                    user_total_crystals.setText("Total Crystals: " + userCareerinfoslist.get(0).getTotal_crystals() + "\uD83D\uDC8E");
                    pDialog.dismiss();
                    //here i have to pass user total buy crystals
                    editor.putString("Total_Crystals", userCareerinfoslist.get(0).getTotal_crystals() + "");
                    editor.apply();
                } else {
                    user_total_crystals.setText("Total Crystals: 0 \uD83D\uDC8E");
                    carrier_location.setText("Select Location");
                    //user_fee.setHint("Select appointment fee");

                    editor.putString("Total_Crystals", "0");
                    editor.apply();
                    pDialog.dismiss();
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
                carrier_location.setText(place.getAddress());

            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.i("TAG", status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }
}
