package project.roomeo.components;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import project.roomeo.R;
import project.roomeo.tools.ReviewerTools;

public class SplashActivity extends AppCompatActivity {

    public Timer timer = new Timer();
    int SPLASH_TIME_OUT = 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.spash_screen);
    }
    @Override
    protected void onResume() {
        super.onResume();
        checkConnectivity();
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, Login.class));
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
}