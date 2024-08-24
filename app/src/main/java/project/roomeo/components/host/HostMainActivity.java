package project.roomeo.components.host;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import project.roomeo.DTO.TokenDTO;
import project.roomeo.R;
import project.roomeo.components.Login;
import project.roomeo.components.UserLoginActivity;
import project.roomeo.components.guest.GuestHomeFragment;
import project.roomeo.models.Guest;

public class HostMainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    GuestHomeFragment homeFragment;
    HostProfileFragment profileFragment;
    HostNotificationsFragment notificationsFragment;
    HostReservationsFragment reservationsFragment;
    HostPastReservationsFragment pastReservationsFragment;
    HostAccommodationsFragment accommodationsFragment;
    HostReportsFragment hostReportsFragment;
    StepperFragment stepperFragment;
    Fragment currentFragment;

    Integer id = 1;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_main);

        bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.bottom_navbar_home);

        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);  // Sakrij prikazivanje naslova

        invalidateOptionsMenu();

        homeFragment = new GuestHomeFragment();
        profileFragment = new HostProfileFragment();
        notificationsFragment = new HostNotificationsFragment();
        reservationsFragment = new HostReservationsFragment();
        pastReservationsFragment = new HostPastReservationsFragment();
        accommodationsFragment = new HostAccommodationsFragment();
        hostReportsFragment = new HostReportsFragment();
        stepperFragment = new StepperFragment();
        currentFragment = homeFragment;
        loadFragment(currentFragment);

        ImageButton logoutButton = findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
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
                currentFragment = pastReservationsFragment;
                break;
            case R.id.bottom_navbar_history:
                currentFragment = reservationsFragment;
                break;
            case R.id.bottom_navbar_accomodations:
                currentFragment = accommodationsFragment;
                break;
        }
        if (currentFragment != null) {
            loadFragment(currentFragment);
        }
        return true;
    }

    public void loadFragment(Fragment fragment) {
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
        inflater.inflate(R.menu.menu_host, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.myAccommodations) {
            getSupportFragmentManager().beginTransaction().replace(R.id.guest_content, accommodationsFragment).commit();

        }if (itemId == R.id.Reports) {
            getSupportFragmentManager().beginTransaction().replace(R.id.guest_content, hostReportsFragment).commit();

        }
        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        SharedPreferences sharedPreferences = getSharedPreferences("preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor spEditor = sharedPreferences.edit();
        spEditor.clear();
        spEditor.apply();

        TokenDTO tokenDTO = TokenDTO.getInstance();
        tokenDTO.setAccessToken(null);
        tokenDTO.setRefreshToken(null);

        Intent intent = new Intent(this, UserLoginActivity.class);
        startActivity(intent);
        finish();
    }
}
