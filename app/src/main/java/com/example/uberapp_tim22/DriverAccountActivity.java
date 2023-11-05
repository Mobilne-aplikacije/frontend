package com.example.uberapp_tim22;

import static android.app.PendingIntent.getActivity;
import static com.example.uberapp_tim22.service.ServiceUtils.driverService;
import static com.example.uberapp_tim22.service.ServiceUtils.userService;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.DialogFragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.uberapp_tim22.DTO.ChangePasswordDTO;
import com.example.uberapp_tim22.DTO.DriverDTO;
import com.example.uberapp_tim22.DTO.DriverUpdate;
import com.example.uberapp_tim22.DTO.PassengerDTO;
import com.example.uberapp_tim22.DTO.PassengerUpdate;
import com.example.uberapp_tim22.DTO.ResponseDriverDTO;
import com.example.uberapp_tim22.DTO.ResponsePassengerDTO;
import com.example.uberapp_tim22.model.Driver;
import com.example.uberapp_tim22.service.ServiceUtils;
import com.google.android.material.navigation.NavigationView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import android.app.FragmentManager;
import android.app.FragmentTransaction;

import org.w3c.dom.Text;

public class DriverAccountActivity extends AppCompatActivity {

//    DrawerLayout drawerLayout;
//    NavigationView navigationView;
//    Button current_ride;
    private SharedPreferences sharedPreferences;
    private EditText editName, editSurname, editNumber, editAddress, editEmail,editCarType;
    private Button saveBtn, changeBtn;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_account);

        editName = findViewById(R.id.editTextTextPersonName4);
        editSurname = findViewById(R.id.editTextTextPersonName3);
        editNumber = findViewById(R.id.editTextTextPersonName5);
        editAddress = findViewById(R.id.editTextTextPersonName6);
        editEmail = findViewById(R.id.editTextTextPersonName77);
        editCarType = findViewById(R.id.editTextTextPersonName7);
        saveBtn = findViewById(R.id.button9);
        changeBtn = findViewById(R.id.button10);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("FAB Car");

        sharedPreferences = getSharedPreferences("preferences", MODE_PRIVATE);
        Long myId = sharedPreferences.getLong("pref_id", 0);

        getDriver(String.valueOf(myId));

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDriver(String.valueOf(myId));
            }
        });
        changeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupDialog();
            }
        });

    }


    private void getDriver(String driverId) {
        Call<DriverDTO> call = driverService.getDriver(driverId);

        call.enqueue(new Callback<DriverDTO>() {
            @Override
            public void onResponse(Call<DriverDTO> call, Response<DriverDTO> response) {
                if (response.isSuccessful()) {
                    DriverDTO driverDTO = response.body();
                    editName.setText(driverDTO.getName());
                    editSurname.setText(driverDTO.getSurname());
                    editNumber.setText(driverDTO.getTelephoneNumber());
                    editAddress.setText(driverDTO.getAddress());
                    editEmail.setText(driverDTO.getEmail());
                  //  editCarType.setText(driverDTO.ge);
                } else {
                    onFailure(call, new Throwable("API call failed with status code: " + response.code()));
                }
            }

            @Override
            public void onFailure(Call<DriverDTO> call, Throwable t) {
                Log.e("DriverAccountActivity", "API call failed: " + t.getMessage());
                Toast.makeText(DriverAccountActivity.this, "API call failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateDriver(String driverId) {
        DriverUpdate driverUpdate = new DriverUpdate(editName.getText().toString(), editSurname.getText().toString(), "picture", editNumber.getText().toString(), editEmail.getText().toString(), editAddress.getText().toString());
        Call<DriverDTO> call = driverService.updateDriver2(driverId, driverUpdate);

        call.enqueue(new Callback<DriverDTO>() {
            @Override
            public void onResponse(Call<DriverDTO> call, Response<DriverDTO> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(DriverAccountActivity.this, "Successfully updated driver!", Toast.LENGTH_SHORT).show();
                } else {
                    onFailure(call, new Throwable("API call failed with status code: " + response.code()));
                }
            }

            @Override
            public void onFailure(Call<DriverDTO> call, Throwable t) {
                Log.e("DriverAccountActivity", "API call failed: " + t.getMessage());
                Toast.makeText(DriverAccountActivity.this, "API call failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showPopupDialog() {
        DialogFragment dialogFragment = new ChangePopupDialogFragment();
        dialogFragment.show(getSupportFragmentManager(), "ChangePopupDialogFragment");
    }
    public static class ChangePopupDialogFragment extends DialogFragment {

        private EditText oldPassword, newPassword;
        private SharedPreferences sharedPreferences;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = requireActivity().getLayoutInflater();
            View view = inflater.inflate(R.layout.dialog_change_popup, null);

            oldPassword = view.findViewById(R.id.oldPassword);
            newPassword = view.findViewById(R.id.newPassword);

            sharedPreferences = getActivity().getSharedPreferences("preferences", MODE_PRIVATE);
            Long myId = sharedPreferences.getLong("pref_id", 0);

            builder.setView(view)
                    .setTitle("Change Details")
                    .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String oldPass = oldPassword.getText().toString();
                            String newPass = newPassword.getText().toString();
                            changePassword(String.valueOf("5"), oldPass, newPass);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
            return builder.create();
        }

        private void changePassword(String driverId, String oldPassword, String newPassword) {
            ChangePasswordDTO changePasswordDTO = new ChangePasswordDTO(newPassword, newPassword);
            Call<ResponsePassengerDTO> call = ServiceUtils.userService.changePassword(driverId, changePasswordDTO);

            call.enqueue(new Callback<ResponsePassengerDTO>() {
                @Override
                public void onResponse(Call<ResponsePassengerDTO> call, Response<ResponsePassengerDTO> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(getContext(), "Successfully updated Password", Toast.LENGTH_SHORT).show();
                    } else {
                        onFailure(call, new Throwable("API call failed with status code: " + response.code()));
                    }
                }

                @Override
                public void onFailure(Call<ResponsePassengerDTO> call, Throwable t) {
                    Log.e("PassangerRideHistory", "API call failed: " + t.getMessage());
                    Toast.makeText(getContext(), "API call failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
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
            Intent intent = new Intent(DriverAccountActivity.this, DriverAccountActivity.class);
            startActivity(intent);
        }
        if(id == R.id.item2){
            Intent intent = new Intent(DriverAccountActivity.this, DriverInboxActivity.class);
            startActivity(intent);
        }
        if(id == R.id.item3){
            Intent intent = new Intent(DriverAccountActivity.this, DriverRideHistory.class);
            startActivity(intent);
        }
        if(id == R.id.item4){
            Intent intent = new Intent(DriverAccountActivity.this, DriverMainActivity.class);
            startActivity(intent);
        }
        if(id == R.id.item5){
            Intent intent = new Intent(DriverAccountActivity.this, UserLoginActivity.class);
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


}