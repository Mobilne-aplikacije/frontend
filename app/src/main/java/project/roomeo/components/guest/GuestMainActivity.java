package project.roomeo.components.guest;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import project.roomeo.R;
import project.roomeo.components.Login;

public class GuestMainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    GuestHomeFragment homeFragment;
    GuestProfileFragment profileFragment;
    GuestNotificationsFragment notificationsFragment;
    GuestReservationsFragment reservationsFragment;
    Fragment currentFragment;

    Integer id = 1;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_main);
        bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.bottom_navbar_home);

        homeFragment = new GuestHomeFragment();
        profileFragment = new GuestProfileFragment();
        notificationsFragment = new GuestNotificationsFragment();
        reservationsFragment = new GuestReservationsFragment();
        currentFragment = homeFragment;
        loadFragment(currentFragment);

        ImageButton logoutButton = findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle logout click here
                // For example, navigate to the login activity
                Intent intent = new Intent(GuestMainActivity.this, Login.class);
                startActivity(intent);
                finish(); // This finishes the current activity, preventing the user from coming back to it using the back button
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;
        //checkIfRideIsAvailable();
        switch (item.getItemId()) {
            case R.id.bottom_navbar_profile:
                currentFragment = profileFragment;
                break;
            case R.id.bottom_navbar_home:
                currentFragment = homeFragment;
                break;
            case R.id.bottom_navbar_inbox:
                currentFragment = notificationsFragment;
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
}
