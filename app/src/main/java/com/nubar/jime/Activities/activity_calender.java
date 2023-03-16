package com.nubar.jime.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

import com.nubar.jime.CalenderFragements.AvailabilityStatus;
import com.nubar.jime.CalenderFragements.daysOfWeek;
import com.nubar.jime.R;

public class activity_calender extends AppCompatActivity implements daysOfWeek.OnGreenFragmentListener {
    daysOfWeek daysOfWeek;
    AvailabilityStatus availabilityStatus;
    private static final String BLUE_TAG = "blue";
    private static final String GREEN_TAG = "green";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender);

        // add fragments
        FragmentManager fragmentManager = getSupportFragmentManager();

        daysOfWeek = new daysOfWeek();
        fragmentManager.beginTransaction().add(R.id.green_fragment_container, daysOfWeek, BLUE_TAG).commit();


        availabilityStatus = new AvailabilityStatus();
        fragmentManager.beginTransaction().add(R.id.blue_fragment_container, availabilityStatus, GREEN_TAG).commit();

    }

    @Override
    public void messageFromGreenFragment(String text) {
        availabilityStatus.youveGotMail(text);
    }
}