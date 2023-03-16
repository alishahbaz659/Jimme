package com.nubar.jime.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.nubar.jime.R;

public class activity_splash extends AppCompatActivity {
    private int sleep_timer = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        logolauncher logolauncher = new logolauncher();
        logolauncher.start();
    }

    private class logolauncher extends Thread {
        public void run() {
            try {
                sleep(1000 * sleep_timer);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            startActivity(new Intent(activity_splash.this, activity_login.class));
            activity_splash.this.finish();
        }
    }

}