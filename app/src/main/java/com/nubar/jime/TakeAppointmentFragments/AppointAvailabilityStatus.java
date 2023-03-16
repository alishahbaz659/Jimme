package com.nubar.jime.TakeAppointmentFragments;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.nubar.jime.Activities.activity_book_appointment;
import com.nubar.jime.Classes.User;
import com.nubar.jime.Classes.appointment_slot;
import com.nubar.jime.Classes.post;
import com.nubar.jime.Classes.userAvailablitySchedule;
import com.nubar.jime.R;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class AppointAvailabilityStatus extends Fragment {
    private TextView mTextView;
    AppointAvailabilityStatus appointAvailabilityStatus;
    FragmentManager fragmentManager;
    Switch sevenToeight, eightTonine, nineToten, tenToeleven, elevenTotwelve, twelveToone,
            oneTotwo, twoTothree, threeTofour, fourTofive, fiveTosix, sixToseven;
    Button savebutton;
    String se, en, nt, te, et, to, ot, tt, tf, ff, fs, ss;
    String dow = "saturday";
    FirebaseAuth firebaseAuth;
    List<userAvailablitySchedule> dowlist;
    public SweetAlertDialog pDialog;
    String uid, post_appointment_fee;
    Button btnseventoeight, btneighttonince, btnninetoten, btntentoeleven, btneleventptwelve,
            btntwelvetoone, btnontotwo, btntwotothree, btnthreetofour, btnfourtofive, btnfivetotsix,
            btnsixtoseven;
    SharedPreferences user_details;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_appoint_availabilitystatus, container, false);
        mTextView = v.findViewById(R.id.textview);
        sevenToeight = v.findViewById(R.id.sevenToeight);
        eightTonine = v.findViewById(R.id.eightTonine);
        nineToten = v.findViewById(R.id.nineToten);
        tenToeleven = v.findViewById(R.id.tenToeleven);
        elevenTotwelve = v.findViewById(R.id.elevenTotweleve);
        twelveToone = v.findViewById(R.id.twelveToone);
        oneTotwo = v.findViewById(R.id.oneTotwo);
        twoTothree = v.findViewById(R.id.twoTothree);
        threeTofour = v.findViewById(R.id.threeTofour);
        fourTofive = v.findViewById(R.id.fourTofive);
        fiveTosix = v.findViewById(R.id.fiveTosix);
        sixToseven = v.findViewById(R.id.sixeToseven);
        btnseventoeight = v.findViewById(R.id.btnste);
        btneighttonince = v.findViewById(R.id.btnetn);
        btnninetoten = v.findViewById(R.id.btnntt);
        btntentoeleven = v.findViewById(R.id.btntte);
        btneleventptwelve = v.findViewById(R.id.btnttt);
        btntwelvetoone = v.findViewById(R.id.btntto);
        btnontotwo = v.findViewById(R.id.ott);
        btntwotothree = v.findViewById(R.id.btntwotothree);
        btnthreetofour = v.findViewById(R.id.btnthreetofour);
        btnfourtofive = v.findViewById(R.id.btnfourtofive);
        btnfivetotsix = v.findViewById(R.id.btnfivetosix);
        btnsixtoseven = v.findViewById(R.id.btnsixtoseven);
        fragmentManager = getActivity().getSupportFragmentManager();
        uid = activity_book_appointment.uid;
        post_appointment_fee = activity_book_appointment.post_appointment_fee;
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        dowlist = new ArrayList<>();
        firebaseAuth = FirebaseAuth.getInstance();
        pDialog = new SweetAlertDialog(getContext(), SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#faa805"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(true);
        user_details = getActivity().getSharedPreferences("userpersonaldetails", getActivity().MODE_PRIVATE);

        get_status(dow);

        pDialog.show();
        btnseventoeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check_slot_availability("sevenToeight");
            }
        });
        btneighttonince.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check_slot_availability("eightTonine");
            }
        });
        btnninetoten.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check_slot_availability("nineToten");
            }
        });
        btntentoeleven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check_slot_availability("tenToeleven");
            }
        });
        btneleventptwelve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check_slot_availability("elevenTotwelve");
            }
        });
        btntwelvetoone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check_slot_availability("twelveToone");
            }
        });
        btnontotwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check_slot_availability("oneTotwo");
            }
        });
        btntwotothree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check_slot_availability("twoTothree");
            }
        });
        btnthreetofour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check_slot_availability("threeTofour");
            }
        });
        btnfourtofive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check_slot_availability("fourTofive");
            }
        });
        btnfivetotsix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check_slot_availability("fiveTosix");
            }
        });
        btnsixtoseven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check_slot_availability("sixToseven");
            }
        });
    }

    // This is a public method that the Activity can use to communicate
    // directly with this Fragment
    public void youveGotMail(String dayofweek) {
        dow = dayofweek;
        Fragment frg = null;
        frg = getActivity().getSupportFragmentManager().findFragmentById(R.id.blue_fragment_container);
        final FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.detach(frg);
        ft.attach(frg);
        ft.commit();
        pDialog.show();
        get_status(dayofweek);
    }

    public void get_status(String dayofweek) {
        pDialog.dismiss();
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference query = rootRef.child("UserAvailabilitySchedule").child(uid).child(dayofweek);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dowlist.clear();
                if (dataSnapshot.exists()) {
                    userAvailablitySchedule u = dataSnapshot.getValue(userAvailablitySchedule.class);
                    dowlist.add(u);
                    if (dowlist.get(0).getSevenToeight().equals("1")) {
                        sevenToeight.setChecked(true);
                        btnseventoeight.setEnabled(true);
                    } else {
                        sevenToeight.setChecked(false);
                        btnseventoeight.setEnabled(false);
                    }
                    if (dowlist.get(0).getEightTonine().equals("1")) {
                        eightTonine.setChecked(true);
                        btneighttonince.setEnabled(true);

                    } else {
                        eightTonine.setChecked(false);
                        btneighttonince.setEnabled(false);
                    }
                    if (dowlist.get(0).getNineToten().equals("1")) {
                        nineToten.setChecked(true);
                        btnninetoten.setEnabled(true);
                    } else {
                        nineToten.setChecked(false);
                        btnninetoten.setEnabled(false);
                    }
                    if (dowlist.get(0).getTenToeleven().equals("1")) {
                        tenToeleven.setChecked(true);
                        btntentoeleven.setEnabled(true);
                    } else {
                        tenToeleven.setChecked(false);
                        btntentoeleven.setEnabled(false);
                    }
                    if (dowlist.get(0).getElevenTotwelve().equals("1")) {
                        elevenTotwelve.setChecked(true);
                        btneleventptwelve.setEnabled(true);
                    } else {
                        elevenTotwelve.setChecked(false);
                        btneleventptwelve.setEnabled(false);
                    }
                    if (dowlist.get(0).getTwelveToone().equals("1")) {
                        twelveToone.setChecked(true);
                        btntwelvetoone.setEnabled(true);
                    } else {
                        twelveToone.setChecked(false);
                        btntwelvetoone.setEnabled(false);
                    }
                    if (dowlist.get(0).getOneTotwo().equals("1")) {
                        oneTotwo.setChecked(true);
                        btnontotwo.setEnabled(true);
                    } else {
                        oneTotwo.setChecked(false);
                        btnontotwo.setEnabled(false);
                    }
                    if (dowlist.get(0).getTwoTothree().equals("1")) {
                        twoTothree.setChecked(true);
                        btntwotothree.setEnabled(true);
                    } else {
                        twoTothree.setChecked(false);
                        btntwotothree.setEnabled(false);
                    }
                    if (dowlist.get(0).getThreeTofour().equals("1")) {
                        threeTofour.setChecked(true);
                        btnthreetofour.setEnabled(true);
                    } else {
                        threeTofour.setChecked(false);
                        btnthreetofour.setEnabled(false);
                    }
                    if (dowlist.get(0).getFourTofive().equals("1")) {
                        fourTofive.setChecked(true);
                        btnfourtofive.setEnabled(true);
                    } else {
                        fourTofive.setChecked(false);
                        btnfourtofive.setEnabled(false);
                    }
                    if (dowlist.get(0).getFiveTosix().equals("1")) {
                        fiveTosix.setChecked(true);
                        btnfivetotsix.setEnabled(true);
                    } else {
                        fiveTosix.setChecked(false);
                        btnfivetotsix.setEnabled(false);
                    }
                    if (dowlist.get(0).getSixToseven().equals("1")) {
                        sixToseven.setChecked(true);
                        btnsixtoseven.setEnabled(true);
                    } else {
                        sixToseven.setChecked(false);
                        btnsixtoseven.setEnabled(false);
                    }
                    pDialog.dismiss();
                } else {
                    sevenToeight.setChecked(false);
                    eightTonine.setChecked(false);
                    nineToten.setChecked(false);
                    tenToeleven.setChecked(false);
                    elevenTotwelve.setChecked(false);
                    twelveToone.setChecked(false);
                    oneTotwo.setChecked(false);
                    twoTothree.setChecked(false);
                    threeTofour.setChecked(false);
                    fourTofive.setChecked(false);
                    fiveTosix.setChecked(false);
                    sixToseven.setChecked(false);
                    pDialog.dismiss();
                }
                pDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public void pay_crystals(final String timing) {
        new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Pay" + post_appointment_fee + "\uD83D\uDC8E?")
                .setContentText("You have to pay " + post_appointment_fee + "\uD83D\uDC8E to take appointment!")
                .setConfirmText("No!")
                .setCancelText("Pay" + post_appointment_fee + "\uD83D\uDC8E")
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {

                        SharedPreferences myPrefs = getActivity().getSharedPreferences("userprofiledata", getActivity().MODE_PRIVATE);
                        int total_crystals = Integer.parseInt(myPrefs.getString("Total_Crystals", "0"));

                        if (total_crystals < 1) {
                            sweetAlertDialog.dismissWithAnimation();
                            new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Empty wallet")
                                    .setContentText("Please buy crystals to take appointment!")
                                    .show();
                        } else {
                            total_crystals -= Integer.parseInt(post_appointment_fee);
                            if (total_crystals < 0) {
                                sweetAlertDialog.dismissWithAnimation();
                                new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("No enough crystals")
                                        .setContentText("Please buy more crystals to take appointment!")
                                        .show();
                            } else {
                                update_crystals_db(total_crystals);
                                sweetAlertDialog.dismissWithAnimation();
                                //here show dialog
                                take_appointment_dialog(timing);
                            }
                        }
                    }
                }).show();
    }

    public void update_crystals_db(final int remaining_crystls) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("userCareerinfo");
        ref.child(firebaseAuth.getUid()).child("info").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    dataSnapshot.getRef().child("total_crystals").setValue(remaining_crystls + "");
                    SharedPreferences.Editor editor = getActivity().getSharedPreferences("userprofiledata", getActivity().MODE_PRIVATE).edit();
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

    public void take_appointment_dialog(String timeslot) {


        //here update person appointment details in database
        DatabaseReference dref1 = FirebaseDatabase.getInstance().getReference("AppointmentSlots").child(uid).child(dow).child(timeslot);

        dref1.setValue(user_details.getString("userEmail", ""));
        DatabaseReference dref2 = FirebaseDatabase.getInstance().getReference("AppointmentSlots").child(uid).child(dow).child("dayofweek");

        dref2.setValue(dow);
        new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("Success")
                .setContentText("Appointment slot reserved successfully!")
                .show();
    }

    public void check_slot_availability(final String time) {
        pDialog.show();
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        Query query = rootRef.child("AppointmentSlots").child(uid).child(dow).child(time);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    pDialog.dismiss();
                    new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Not Available")
                            .setContentText("Sorry this time slot is already reserved!")
                            .show();
                } else {
                    pDialog.dismiss();
                    pay_crystals(time);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }


}

