package com.nubar.jime.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mahc.custombottomsheetbehavior.BottomSheetBehaviorGoogleMapsLike;
import com.mahc.custombottomsheetbehavior.MergedAppBarLayout;
import com.mahc.custombottomsheetbehavior.MergedAppBarLayoutBehavior;
import com.nubar.jime.Adapter.ItemPagerAdapter;
import com.nubar.jime.Classes.User;
import com.nubar.jime.R;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class activity_professional_person_details extends AppCompatActivity implements OnMapReadyCallback {
    SupportMapFragment supportMapFragment;
    GoogleMap map;
    String post_title, post_uid, post_latitude, post_longitude, post_description;
    TextView bottomsheet_post_title, bottomsheet_userName, bottmsheet_userEmail, bottomsheet_appointment_money,
            bottomsheet_post_location, bottomsheet_post_description;
    View bottomSheet;
    int[] mDrawables = {
            R.drawable.doodle,

    };
    double postlat, postlang;
    Geocoder geocoder;
    List<Address> addresses;
    FirebaseAuth firebaseAuth;
    ArrayList<User> post_editor_detail_list;
    Button button_view_timings;
    String post_appointment_fee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_professional_person_details);
        firebaseAuth = FirebaseAuth.getInstance();
        post_editor_detail_list = new ArrayList<>();
        get_intent_values();
        if (!Places.isInitialized()) {
            Places.initialize(this, "AIzaSyCgfbBqghK8_NNSCzw_LQalhmgwrm4TA2w");
        }
        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.Bgoogle_map);
        supportMapFragment.getMapAsync(activity_professional_person_details.this);


        //Image work here
        ItemPagerAdapter adapter = new ItemPagerAdapter(this, mDrawables);
        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(adapter);

        //bottom sheet work
        CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorlayout);
        bottomSheet = coordinatorLayout.findViewById(R.id.bottom_sheet);
        final BottomSheetBehaviorGoogleMapsLike behavior = BottomSheetBehaviorGoogleMapsLike.from(bottomSheet);

        MergedAppBarLayout mergedAppBarLayout = findViewById(R.id.mergedappbarlayout);
        MergedAppBarLayoutBehavior mergedAppBarLayoutBehavior = MergedAppBarLayoutBehavior.from(mergedAppBarLayout);
        mergedAppBarLayoutBehavior.setToolbarTitle(post_title);
        mergedAppBarLayoutBehavior.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                behavior.setState(BottomSheetBehaviorGoogleMapsLike.STATE_ANCHOR_POINT);
            }
        });
        bottom_sheet_bindings();
        get_post_editor_details();
        behavior.setState(BottomSheetBehaviorGoogleMapsLike.STATE_COLLAPSED);
        behavior.setCollapsible(true);
        bottomsheet_post_title.setText(post_title);

        geocoder = new Geocoder(this, Locale.getDefault());
        bottomsheet_post_description.setText(post_description);

        try {
            addresses = geocoder.getFromLocation(Double.parseDouble(post_latitude), Double.parseDouble(post_longitude), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            bottomsheet_post_location.setText(addresses.get(0).getAddressLine(0));
        } catch (IOException e) {
            bottomsheet_post_location.setText("N/A");
        }
        button_view_timings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity_professional_person_details.this, activity_book_appointment.class);
                intent.putExtra("uid", post_uid);
                intent.putExtra("postAppointmentFee", post_appointment_fee);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        postlat = Double.parseDouble(post_latitude);
        postlang = Double.parseDouble(post_longitude);
        LatLng latLng = new LatLng(postlat, postlang);
        map.addMarker(new MarkerOptions().position(latLng).title("Location"));
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
    }

    public void get_intent_values() {
        Intent intent = getIntent();
        post_title = intent.getStringExtra("post_title");
        post_uid = intent.getStringExtra("post_uid");
        post_latitude = intent.getStringExtra("post_latitude");
        post_longitude = intent.getStringExtra("post_longitude");
        post_description = intent.getStringExtra("post_description");
    }

    public void bottom_sheet_bindings() {
        bottomsheet_post_title = bottomSheet.findViewById(R.id.bottom_sheet_post_title);
        bottomsheet_userName = bottomSheet.findViewById(R.id.txtpostUsername);
        bottmsheet_userEmail = bottomSheet.findViewById(R.id.txtpostuseremail);
        bottomsheet_appointment_money = bottomSheet.findViewById(R.id.txtappointmentmoney);
        bottomsheet_post_location = bottomSheet.findViewById(R.id.txtpostaddress);
        bottomsheet_post_description = bottomSheet.findViewById(R.id.bottom_sheet_post_desciption);
        button_view_timings = bottomSheet.findViewById(R.id.btn_appointment_section);
    }

    public void get_post_editor_details() {
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        Query query = rootRef.child("Users").orderByChild("uid").equalTo(post_uid);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    post_editor_detail_list.clear();
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        User u = ds.getValue(User.class);
                        post_editor_detail_list.add(u);
                    }
                    bottmsheet_userEmail.setText(post_editor_detail_list.get(0).getEmail());
                    bottomsheet_userName.setText(post_editor_detail_list.get(0).getUsername());

                } else {
                    bottmsheet_userEmail.setText("N/A");
                    bottomsheet_userName.setText("N/A");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        DatabaseReference refff = FirebaseDatabase.getInstance().getReference();
        DatabaseReference query1 = refff.child("userCareerinfo").child(post_uid).child("info");
        query1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    post_appointment_fee = dataSnapshot.child("fee").getValue(String.class);
                    bottomsheet_appointment_money.setText(post_appointment_fee + "\uD83D\uDC8E");
                } else {
                    post_appointment_fee = "5";
                    bottomsheet_appointment_money.setText(post_appointment_fee + "\uD83D\uDC8E");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}