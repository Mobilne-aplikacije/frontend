package com.example.uberapp_tim22;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.uberapp_tim22.tools.ReviewerTools;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity {

    public Timer timer = new Timer();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkConnectivity();

        //TODO PROVERITI LOKACIJU
//        checkLocationStatus();
        int SPLASH_TIME_OUT = 5000;
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, UserLoginActivity.class));
                finish();
            }
        }, SPLASH_TIME_OUT);
    }

    private void checkConnectivity() {
        int status = ReviewerTools.getConnectivityStatus(getApplicationContext());
        if (status != ReviewerTools.TYPE_WIFI && status != ReviewerTools.TYPE_MOBILE) {
            Toast.makeText(getApplicationContext(), "Device is not connected to the internet", Toast.LENGTH_SHORT).show();

            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    finishAffinity();
                }
            }, 1000);
        }
    }

//    public boolean isLocationEnabled() {
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) return false;
//        return true;
//    }

}