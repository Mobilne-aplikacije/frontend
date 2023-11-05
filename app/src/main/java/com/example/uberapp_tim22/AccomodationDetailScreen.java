package com.example.uberapp_tim22;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

public class AccomodationDetailScreen extends AppCompatActivity {
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accomodation_detail_screen);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Booking");

        sharedPreferences = getSharedPreferences("preferences", MODE_PRIVATE);
        Long myId = sharedPreferences.getLong("pref_id", 0);

    }

    @Override
    protected void onStart() {
        super.onStart();
        Toast.makeText(this, "onStart()", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(this, "onResume()",Toast.LENGTH_SHORT).show();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.login_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.menuHistory) {
            Intent intent = new Intent(this, PassangerRideHistory.class);
            startActivity(intent);
            return true;
        }

        if (itemId == R.id.menuMain) {
            Intent intent = new Intent(this, PassengerMainActivity.class);
            startActivity(intent);
            return true;
        }

        if (itemId == R.id.menuAccount) {
            Intent intent = new Intent(this, PassengerAccountActivity.class);
            startActivity(intent);
            return true;
        }

        if (itemId == R.id.menuReservation) {
            Intent intent = new Intent(this, GuestReservationScreen.class);
            startActivity(intent);
            return true;
        }

        if (itemId == R.id.menuNotification) {
            Intent intent = new Intent(this, NotificationScreen.class);
            startActivity(intent);
            return true;
        }

        if (itemId == R.id.menuInbox) {
            Intent intent = new Intent(this, PassengerInboxActivity.class);
            startActivity(intent);
            return true;
        }

        if (itemId == R.id.menuStatistic) {
            Intent intent = new Intent(this, PassengerFavoriteRides.class);
            startActivity(intent);
            return true;
        }
        if (itemId == R.id.menuFav) {
            Intent intent = new Intent(this, PassengerFavoriteRides.class);
            startActivity(intent);
            return true;
        }

        if (itemId == R.id.menuLogOut) {
            deletePreferences();
            Intent intent = new Intent(this, UserLoginActivity.class);
            startActivity(intent);
            return true;

        }

        return super.onOptionsItemSelected(item);
    }

    private void deletePreferences(){
        SharedPreferences sharedPreferences = getSharedPreferences("preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor spEditor = sharedPreferences.edit();
        spEditor.clear().commit();
    }

}