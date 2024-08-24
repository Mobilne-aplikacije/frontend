package project.roomeo.components.admin;

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

import com.google.android.material.bottomnavigation.BottomNavigationView;

import project.roomeo.DTO.TokenDTO;
import project.roomeo.R;
import project.roomeo.components.UserLoginActivity;

public class AdminMainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    AdminProfileFragment profileFragment;
    UserReportRequestsFragment reportedUsersFragment;
    AdminRatingsFragment adminRatingsFragment;
    AccommodationRequestsFragment accommodationsApprovalFragment;
    Fragment currentFragment;

    private Long myId;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);

        SharedPreferences sharedPreferences = getSharedPreferences("preferences", Context.MODE_PRIVATE);
        myId = sharedPreferences.getLong("pref_id", 0L);

        bottomNavigationView = findViewById(R.id.bottom_nav_admin);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.bottom_navbar_home);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Initialize fragments
        profileFragment = new AdminProfileFragment();
        reportedUsersFragment = new UserReportRequestsFragment();
        adminRatingsFragment = new AdminRatingsFragment();
        accommodationsApprovalFragment = new AccommodationRequestsFragment();
        currentFragment = profileFragment;
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
        switch (item.getItemId()) {
            case R.id.bottom_navbar_profile:
                currentFragment = profileFragment;
                break;
            case R.id.bottom_navbar_reported_users:
                currentFragment = reportedUsersFragment;
                break;
            case R.id.bottom_navbar_reported_comments:
                currentFragment = adminRatingsFragment;
                break;
            case R.id.bottom_navbar_accommodations_approval:
                currentFragment = accommodationsApprovalFragment;
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_admin, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        // Handle menu item clicks here
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
