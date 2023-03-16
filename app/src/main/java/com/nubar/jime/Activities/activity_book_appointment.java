package com.nubar.jime.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.nubar.jime.CalenderFragements.AvailabilityStatus;
import com.nubar.jime.CalenderFragements.daysOfWeek;
import com.nubar.jime.R;
import com.nubar.jime.TakeAppointmentFragments.AppointAvailabilityStatus;
import com.nubar.jime.TakeAppointmentFragments.AppointdaysOfWeek;

public class activity_book_appointment extends AppCompatActivity implements AppointdaysOfWeek.OnGreenFragmentListener {
    AppointdaysOfWeek daysOfWeek;
    AppointAvailabilityStatus availabilityStatus;
    private static final String BLUE_TAG = "blue";
    private static final String GREEN_TAG = "green";
    public static String uid;
    public static String post_appointment_fee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_appointment);
        Intent intent = getIntent();
        uid = intent.getStringExtra("uid");
        post_appointment_fee = intent.getStringExtra("postAppointmentFee");

        // add fragments
        FragmentManager fragmentManager = getSupportFragmentManager();

        daysOfWeek = new AppointdaysOfWeek();
        fragmentManager.beginTransaction().add(R.id.green_fragment_container, daysOfWeek, BLUE_TAG).commit();


        availabilityStatus = new AppointAvailabilityStatus();
        fragmentManager.beginTransaction().add(R.id.blue_fragment_container, availabilityStatus, GREEN_TAG).commit();

    }

    @Override
    public void messageFromGreenFragment(String text) {
        availabilityStatus.youveGotMail(text);
    }
}