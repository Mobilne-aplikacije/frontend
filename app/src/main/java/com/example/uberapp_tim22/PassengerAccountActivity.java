package com.example.uberapp_tim22;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import android.app.AlertDialog;
import android.app.Dialog;
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
import android.widget.Toast;

import com.example.uberapp_tim22.DTO.ChangePasswordDTO;
import com.example.uberapp_tim22.DTO.PassengerDTO;
import com.example.uberapp_tim22.DTO.PassengerUpdate;
import com.example.uberapp_tim22.DTO.ResponsePassengerDTO;
import com.example.uberapp_tim22.service.ServiceUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PassengerAccountActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private EditText editName, editSurname, editNumber, editAddress, editEmail;
    private Button saveBtn, changeBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_account);

        editName = findViewById(R.id.editTextPersonName);
        editSurname = findViewById(R.id.editTextPersonSurname);
        editNumber = findViewById(R.id.editTextPersonNumber);
        editAddress = findViewById(R.id.editTextPersonAddress);
        editEmail = findViewById(R.id.editTextPersonEmail);
        saveBtn = findViewById(R.id.personSaveBtn);
        changeBtn = findViewById(R.id.personChangeBtn);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("FAB Car");

        sharedPreferences = getSharedPreferences("preferences", MODE_PRIVATE);
        Long myId = sharedPreferences.getLong("pref_id", 0);

        getPassenger(String.valueOf(myId));

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePassenger(String.valueOf(myId));
            }
        });

        changeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupDialog();
            }
        });

    }

    private void getPassenger(String passengerId) {
        Call<PassengerDTO> call = ServiceUtils.passengerService.getPassenger(passengerId);

        call.enqueue(new Callback<PassengerDTO>() {
            @Override
            public void onResponse(Call<PassengerDTO> call, Response<PassengerDTO> response) {
                if (response.isSuccessful()) {
                    PassengerDTO passenger = response.body();
                    editName.setText(passenger.getName());
                    editSurname.setText(passenger.getSurname());
                    editNumber.setText(passenger.getTelephoneNumber());
                    editAddress.setText(passenger.getAddress());
                    editEmail.setText(passenger.getEmail());
                } else {
                    onFailure(call, new Throwable("API call failed with status code: " + response.code()));
                }
            }

            @Override
            public void onFailure(Call<PassengerDTO> call, Throwable t) {
                Log.e("PassangerRideHistory", "API call failed: " + t.getMessage());
                Toast.makeText(PassengerAccountActivity.this, "API call failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updatePassenger(String passengerId) {
        PassengerUpdate passengerUpdate = new PassengerUpdate(editName.getText().toString(), editSurname.getText().toString(), "picture", editNumber.getText().toString(), editEmail.getText().toString(), editAddress.getText().toString());
        Call<PassengerDTO> call = ServiceUtils.passengerService.updatePassenger(passengerId, passengerUpdate);

        call.enqueue(new Callback<PassengerDTO>() {
            @Override
            public void onResponse(Call<PassengerDTO> call, Response<PassengerDTO> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(PassengerAccountActivity.this, "Successfully updated Passenger", Toast.LENGTH_SHORT).show();
                } else {
                    onFailure(call, new Throwable("API call failed with status code: " + response.code()));
                }
            }

            @Override
            public void onFailure(Call<PassengerDTO> call, Throwable t) {
                Log.e("PassangerRideHistory", "API call failed: " + t.getMessage());
                Toast.makeText(PassengerAccountActivity.this, "API call failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showPopupDialog() {
        DialogFragment dialogFragment = new ChangePopupDialogFragment();
        dialogFragment.show(getSupportFragmentManager(), "ChangePopupDialogFragment");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.menuOrder) {
            Intent intent = new Intent(this, PassengerMainActivity.class);
            startActivity(intent);
            return true;
        }
        if (itemId == R.id.menuHistory) {
            Intent intent = new Intent(this, PassangerRideHistory.class);
            startActivity(intent);
            return true;
        }
        if (itemId == R.id.menuInbox) {
            Intent intent = new Intent(this, PassengerInboxActivity.class);
            startActivity(intent);
            return true;
        }

        if (itemId == R.id.menuStatistic) {
            Intent intent = new Intent(this, PassengerReportsActivity.class);
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.login_menu, menu);
        return super.onCreateOptionsMenu(menu);
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
                            changePassword(String.valueOf(myId), oldPass, newPass);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
            return builder.create();
        }

        private void changePassword(String passengerId, String oldPassword, String newPassword) {
            ChangePasswordDTO changePasswordDTO = new ChangePasswordDTO(newPassword, oldPassword);
            Call<ResponsePassengerDTO> call = ServiceUtils.userService.changePassword(passengerId, changePasswordDTO);

            call.enqueue(new Callback<ResponsePassengerDTO>() {
                @Override
                public void onResponse(Call<ResponsePassengerDTO> call, Response<ResponsePassengerDTO> response) {
                    if (response.isSuccessful()) {
                    } else {
                        onFailure(call, new Throwable("API call failed with status code: " + response.code()));
                    }
                }

                @Override
                public void onFailure(Call<ResponsePassengerDTO> call, Throwable t) {
                    Log.e("PassangerRideHistory", "API call failed: " + t.getMessage());
//                    Toast.makeText(getContext(), "API call failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

}