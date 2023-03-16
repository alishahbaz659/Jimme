package com.nubar.jime;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.libraries.places.api.Places;
import com.google.android.material.snackbar.Snackbar;
import com.nubar.jime.Fragments.createAppointment;
import com.nubar.jime.Fragments.customers;
import com.nubar.jime.Fragments.profile;
import com.nubar.jime.Fragments.searchAppointment;
import com.nubar.jime.Fragments.setting;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    //my current location latlng
    double latitude1, longitude1;
    private static final int REQUEST_LOCATION = 1;
    LocationManager locationManager;
    SharedPreferences.Editor editor;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        if (!Places.isInitialized()) {
            Places.initialize(this, "AIzaSyCgfbBqghK8_NNSCzw_LQalhmgwrm4TA2w");
        }

        bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.appointnavigation);
        bottomNavigationView.setOnNavigationItemSelectedListener
                (new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Fragment selectedFragment = null;
                        switch (item.getItemId()) {
                            case R.id.aaction_item1:
                                selectedFragment = customers.newInstance();
                                break;
                            case R.id.aaction_item2:
                                selectedFragment = profile.newInstance();
                                break;
                            case R.id.aaction_item3:
                                selectedFragment = createAppointment.newInstance();
                                break;
                            case R.id.aaction_item4:
                                selectedFragment = searchAppointment.newInstance();
                                break;
                            case R.id.aaction_item5:
                                selectedFragment = setting.newInstance();
                                break;
                        }
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.frame_layout, selectedFragment);
                        transaction.commit();
                        return true;
                    }
                });

        //Manually displaying the first fragment - one time only
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, customers.newInstance());
        transaction.commit();

    }

    @Override
    protected void onStart() {
        super.onStart();
        checkpermission();
    }

    public void checkpermission() {

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        } else if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            OnGPS();
        } else {
            getLocation();
        }
    }


    @Override
    public void onRequestPermissionsResult(int permsRequestCode, String[] permissions, int[] grantResults) {

        if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // now, you have permission go ahead
            // TODO: something
            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                OnGPS();
            } else {
                getLocation();
            }

        } else {
            Toast.makeText(this, "Location not granted", Toast.LENGTH_SHORT).show();
        }
    }

    private void getLocation() {
        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        } else {
            Location locationGPS = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
            if (locationGPS != null) {
                double lat = locationGPS.getLatitude();
                double longi = locationGPS.getLongitude();
                latitude1 = lat;
                longitude1 = longi;
                editor = getSharedPreferences("locationData", MODE_PRIVATE).edit();
                editor.putString("latitude", String.valueOf(latitude1));
                editor.putString("longitude", String.valueOf(longitude1));
                editor.commit();
            } else {
                latitude1 = 0.0;
                longitude1 = 0.0;
                editor = getSharedPreferences("locationData", MODE_PRIVATE).edit();
                editor.putString("latitude", String.valueOf(latitude1));
                editor.putString("longitude", String.valueOf(longitude1));
                editor.commit();
            }
        }
    }

    private void OnGPS() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Enable GPS").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));

            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MainActivity.this, "Distance results will be not accurate", Toast.LENGTH_SHORT).show();
                dialog.cancel();
            }
        });
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}
