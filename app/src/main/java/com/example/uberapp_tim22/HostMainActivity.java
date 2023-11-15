package com.example.uberapp_tim22;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ToggleButton;


public class HostMainActivity extends AppCompatActivity {

    Button acceptanceRide;
    Button currentRide;
    ToggleButton online;
    Button report;
    Button statistics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Booking");


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.example_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        if(id == R.id.item1){
            Intent intent = new Intent(this, HostAccountActivity.class);
            startActivity(intent);
        }
        if(id == R.id.item6){
            Intent intent = new Intent(this, AccomodationHostActivity.class);
            startActivity(intent);
        }
        if(id == R.id.item7){
            Intent intent = new Intent(this, HostReservationScreen.class);
            startActivity(intent);
        }
        if(id == R.id.item8){
            Intent intent = new Intent(this, NotificationHostScreen.class);
            startActivity(intent);
        }
        if(id == R.id.item4){
            Intent intent = new Intent(this, HostMainActivity.class);
            startActivity(intent);
        }
        if(id == R.id.item5){
            Intent intent = new Intent(this, UserLoginActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Toast.makeText(this, "onStart()", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean wifi = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        Log.i("wwww", String.valueOf(gps));
        Log.i("wqqqq", String.valueOf(wifi));
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode,
//                                           String permissions[], int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        switch (requestCode) {
//            case MY_PERMISSIONS_REQUEST_LOCATION: {
//                // If request is cancelled, the result arrays are empty.
//                if (grantResults.length > 0
//                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//
//                    // permission was granted, yay! Do the
//                    // location-related task you need to do.
//                    if (ContextCompat.checkSelfPermission(this,
//                            android.Manifest.permission.ACCESS_FINE_LOCATION)
//                            == PackageManager.PERMISSION_GRANTED) {
//                        Toast.makeText(this, "LOCATION GRANTED", Toast.LENGTH_SHORT).show();
//                    }
//
//                } else if (grantResults.length > 0
//                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
//
//                    if (ContextCompat.checkSelfPermission(this,
//                            Manifest.permission.ACCESS_COARSE_LOCATION)
//                            == PackageManager.PERMISSION_GRANTED) {
//                        Toast.makeText(this, "LOCATION GRANTED", Toast.LENGTH_SHORT).show();
//
//                    }
//
//                }
//            }
//
//        }
//    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

    }

    @Override
    protected void onPause() {
        super.onPause();
        Toast.makeText(this, "onPause()",Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Toast.makeText(this, "onStop()",Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "onDestroy()",Toast.LENGTH_SHORT).show();
    }
}