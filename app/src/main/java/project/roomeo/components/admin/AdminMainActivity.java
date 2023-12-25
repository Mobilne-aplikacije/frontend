package project.roomeo.components.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import project.roomeo.R;
import project.roomeo.components.Login;
import project.roomeo.components.guest.GuestReservationsFragment;

public class AdminMainActivity extends AppCompatActivity  implements BottomNavigationView.OnNavigationItemSelectedListener {

    AdminHomeFragment homeFragment;
    AdminProfileFragment profileFragment;
    UpdateRequestsFragment requestsFragment;
    GuestReservationsFragment reservationsFragment;
    AccommodationRequestsFragment accommodationRequestsFragment;
    AccommodationEditFragment accommodationEditFragment;
    Fragment currentFragment;

    Integer id = 1;
    private Long myId;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_main);

        SharedPreferences sharedPreferences = getSharedPreferences("preferences", Context.MODE_PRIVATE);
        String myEmail = sharedPreferences.getString("pref_email", "");
        myId = sharedPreferences.getLong("pref_id", 0L);

        bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.bottom_navbar_home);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);  // Sakrij prikazivanje naslova

//        invalidateOptionsMenu();

        homeFragment = new AdminHomeFragment();
        profileFragment = new AdminProfileFragment();
        requestsFragment = new UpdateRequestsFragment();
        reservationsFragment = new GuestReservationsFragment();
        accommodationRequestsFragment = new AccommodationRequestsFragment();
        accommodationEditFragment = new AccommodationEditFragment();
        currentFragment = homeFragment;
        loadFragment(currentFragment);

        ImageButton logoutButton = findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle logout click here
                // For example, navigate to the login activity
                deletePreferences();
                Intent intent = new Intent(AdminMainActivity.this, Login.class);
                startActivity(intent);
                finish(); // This finishes the current activity, preventing the user from coming back to it using the back button
            }
        });


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;
        switch (item.getItemId()) {
            case R.id.bottom_navbar_profile:
                currentFragment = profileFragment;
                break;
            case R.id.bottom_navbar_home:
                currentFragment = homeFragment;
                break;
            case R.id.bottom_navbar_inbox:
                currentFragment = requestsFragment;
                break;
            case R.id.bottom_navbar_history:
                currentFragment = reservationsFragment;
                break;
        }
        if (currentFragment != null) {
            loadFragment(currentFragment);
        }
        return true;
    }

    void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.guest_content, fragment).commit();
    }

    private void deletePreferences(){
        SharedPreferences sharedPreferences = getSharedPreferences("preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor spEditor = sharedPreferences.edit();
        spEditor.clear().commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_admin, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.acc_requests) {
            getSupportFragmentManager().beginTransaction().replace(R.id.guest_content, accommodationRequestsFragment).commit();
        }
        if (itemId == R.id.acc_edit) {
            getSupportFragmentManager().beginTransaction().replace(R.id.guest_content, accommodationEditFragment).commit();
        }
        return super.onOptionsItemSelected(item);
    }
}
