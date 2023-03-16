package com.nubar.jime.CalenderFragements;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.nubar.jime.Activities.activity_login;
import com.nubar.jime.Classes.userAvailablitySchedule;
import com.nubar.jime.R;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class AvailabilityStatus extends Fragment {

    private TextView mTextView;
    AvailabilityStatus availabilityStatus;
    FragmentManager fragmentManager;
    Switch sevenToeight, eightTonine, nineToten, tenToeleven, elevenTotwelve, twelveToone,
            oneTotwo, twoTothree, threeTofour, fourTofive, fiveTosix, sixToseven;
    Button savebutton;
    String se, en, nt, te, et, to, ot, tt, tf, ff, fs, ss;
    String dow = "saturday";
    FirebaseAuth firebaseAuth;
    List<userAvailablitySchedule> dowlist;
    public SweetAlertDialog pDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout_availabilitystatus, container, false);
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
        savebutton = v.findViewById(R.id.saveSchedulebtn);
        fragmentManager = getActivity().getSupportFragmentManager();
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
        pDialog.setCancelable(false);
        get_status(dow);
        pDialog.show();

        savebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSwtitchstatus();
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("UserAvailabilitySchedule").child(firebaseAuth.getUid()).child(dow);
                userAvailablitySchedule userAvailablitySchedule = new userAvailablitySchedule(se, en, nt, te, et, to, ot, tt, tf, ff, fs, ss);
                ref.setValue(userAvailablitySchedule);
                new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Success")
                        .setContentText("Schedule updated successfully!")
                        .show();

            }
        });
    }


    public void getSwtitchstatus() {
        if (sevenToeight.isChecked()) {
            se = "1";
        } else {
            se = "0";
        }
        if (eightTonine.isChecked()) {
            en = "1";
        } else {
            en = "0";
        }
        if (nineToten.isChecked()) {
            nt = "1";
        } else {
            nt = "0";
        }
        if (tenToeleven.isChecked()) {
            te = "1";
        } else {
            te = "0";
        }
        if (elevenTotwelve.isChecked()) {
            et = "1";
        } else {
            et = "0";
        }
        if (twelveToone.isChecked()) {
            to = "1";
        } else {
            to = "0";
        }
        if (oneTotwo.isChecked()) {
            ot = "1";
        } else {
            ot = "0";
        }
        if (twoTothree.isChecked()) {
            tt = "1";
        } else {
            tt = "0";
        }
        if (threeTofour.isChecked()) {
            tf = "1";
        } else {
            tf = "0";
        }
        if (fourTofive.isChecked()) {
            ff = "1";
        } else {
            ff = "0";
        }
        if (fiveTosix.isChecked()) {
            fs = "1";
        } else {
            fs = "0";
        }
        if (sixToseven.isChecked()) {
            ss = "1";
        } else {
            ss = "0";
        }
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
        DatabaseReference query = rootRef.child("UserAvailabilitySchedule").child(firebaseAuth.getUid()).child(dayofweek);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dowlist.clear();
                if (dataSnapshot.exists()) {
                    userAvailablitySchedule u = dataSnapshot.getValue(userAvailablitySchedule.class);
                    dowlist.add(u);
                    if (dowlist.get(0).getSevenToeight().equals("1")) {
                        sevenToeight.setChecked(true);
                    } else {
                        sevenToeight.setChecked(false);
                    }
                    if (dowlist.get(0).getEightTonine().equals("1")) {
                        eightTonine.setChecked(true);
                    } else {
                        eightTonine.setChecked(false);
                    }
                    if (dowlist.get(0).getNineToten().equals("1")) {
                        nineToten.setChecked(true);
                    } else {
                        nineToten.setChecked(false);
                    }
                    if (dowlist.get(0).getTenToeleven().equals("1")) {
                        tenToeleven.setChecked(true);
                    } else {
                        tenToeleven.setChecked(false);
                    }
                    if (dowlist.get(0).getElevenTotwelve().equals("1")) {
                        elevenTotwelve.setChecked(true);
                    } else {
                        elevenTotwelve.setChecked(false);
                    }
                    if (dowlist.get(0).getTwelveToone().equals("1")) {
                        twelveToone.setChecked(true);
                    } else {
                        twelveToone.setChecked(false);
                    }
                    if (dowlist.get(0).getOneTotwo().equals("1")) {
                        oneTotwo.setChecked(true);
                    } else {
                        oneTotwo.setChecked(false);
                    }
                    if (dowlist.get(0).getTwoTothree().equals("1")) {
                        twoTothree.setChecked(true);
                    } else {
                        twoTothree.setChecked(false);
                    }
                    if (dowlist.get(0).getThreeTofour().equals("1")) {
                        threeTofour.setChecked(true);
                    } else {
                        threeTofour.setChecked(false);
                    }
                    if (dowlist.get(0).getFourTofive().equals("1")) {
                        fourTofive.setChecked(true);
                    } else {
                        fourTofive.setChecked(false);
                    }
                    if (dowlist.get(0).getFiveTosix().equals("1")) {
                        fiveTosix.setChecked(true);
                    } else {
                        fiveTosix.setChecked(false);
                    }
                    if (dowlist.get(0).getSixToseven().equals("1")) {
                        sixToseven.setChecked(true);
                    } else {
                        sixToseven.setChecked(false);
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
}
