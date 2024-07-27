package project.roomeo.components.guest;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import project.roomeo.R;
import project.roomeo.components.UserLoginActivity;
import project.roomeo.models.Guest;
import project.roomeo.models.Reservation;
import project.roomeo.models.enums.ReservationRequestStatus;
import project.roomeo.service.ServiceUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class GuestProfileFragment extends Fragment {
    private Long myId;
    private EditText nameEditText;
    private EditText phoneEditText;
    private EditText addressEditText;
    private TextView roleText;
    private EditText emailEditText;
    private Button updateButton;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_guest_profile, container, false);

        nameEditText = view.findViewById(R.id.nameText);
        phoneEditText = view.findViewById(R.id.phoneText);
        addressEditText = view.findViewById(R.id.addressText);
        roleText = view.findViewById(R.id.roleText);
        emailEditText = view.findViewById(R.id.emailText);
        updateButton = view.findViewById(R.id.updateButton);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("preferences", Context.MODE_PRIVATE);
        String myEmail = sharedPreferences.getString("pref_email", "");
        myId = sharedPreferences.getLong("pref_id", 0L);
        Log.e("PROVERA ID", "provera id: " + myId);

        fetchGuestData();

        return view;
    }

    private void fetchGuestData() {

        Call<Guest> call = ServiceUtils.guestService.getGuest(myId.toString());

        call.enqueue(new Callback<Guest>() {
            @Override
            public void onResponse(Call<Guest> call, Response<Guest> response) {
                if (response.isSuccessful()) {
                    Guest guest = response.body();
                    if (guest != null) {
                        updateUI(guest);
                        setupEditing(guest);
                    }
                } else {
                    onFailure(call, new Throwable("API call failed with status code: " + response.code()));
                }
            }

            @Override
            public void onFailure(Call<Guest> call, Throwable t) {
                Log.e("GuestProfileFragment", "API call failed: " + t.getMessage());
            }
        });
    }

    private void updateUI(Guest guest) {
//        nameText.setText(guest.getFirstName() +" "+guest.getLastName());
        Log.i("ime ",guest.getLastName());
//        emailText.setText(guest.getEmail());
//        phoneText.setText(guest.getPhoneNumber());
//        addressText.setText(guest.getAddress());
        roleText.setText("Guest");
    }

    private void setupEditing(Guest guest) {
        nameEditText.setText(guest.getFirstName() +" "+guest.getLastName());
        phoneEditText.setText(guest.getPhoneNumber());
        addressEditText.setText(guest.getAddress());
        emailEditText.setText(guest.getEmail());

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Do nothing before text changes
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Do nothing when text is changing
            }

            @Override
            public void afterTextChanged(Editable s) {
                String newEmail = emailEditText.getText().toString();
                String newPhone = phoneEditText.getText().toString();
                String newAddress = addressEditText.getText().toString();
                String newName = nameEditText.getText().toString();

                if (!newEmail.equals(guest.getEmail()) || !newPhone.equals(guest.getPhoneNumber()) || !newAddress.equals(guest.getAddress()) || !newName.equals(guest.getFirstName()+" "+guest.getLastName())) {
                    updateButton.setVisibility(View.VISIBLE);
                } else {
                    updateButton.setVisibility(View.GONE);
                }
            }
        };

        emailEditText.addTextChangedListener(textWatcher);
        phoneEditText.addTextChangedListener(textWatcher);
        addressEditText.addTextChangedListener(textWatcher);
        nameEditText.addTextChangedListener(textWatcher);

        updateButton.setOnClickListener(v -> updateGuestData());
    }

    private void updateGuestData() {
        String newEmail = emailEditText.getText().toString();
        String newPhone = phoneEditText.getText().toString();
        String newFirstname = nameEditText.getText().toString().split(" ")[0];
        String newLastname = nameEditText.getText().toString().split(" ")[1];
        String newAddress = addressEditText.getText().toString();

        Guest updatedGuest = new Guest();
        updatedGuest.setEmail(newEmail);
        updatedGuest.setPhoneNumber(newPhone);
        updatedGuest.setAddress(newAddress);
        updatedGuest.setFirstName(newFirstname);
        updatedGuest.setLastName(newLastname);
        showUpdateConfirmationDialog();

        Call<Guest> call = ServiceUtils.guestService.updateGuest(myId.toString(), updatedGuest);

        call.enqueue(new Callback<Guest>() {
            @Override
            public void onResponse(Call<Guest> call, Response<Guest> response) {
                if (response.isSuccessful()) {
                    showUpdateConfirmationDialog();
                } else {
                    Log.e("GuestProfileFragment", "Update failed with status code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Guest> call, Throwable t) {
                Log.e("GuestProfileFragment", "Update failed: " + t.getMessage());
            }
        });
    }


    private void showUpdateConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Successfully updated your information.")
                .setCancelable(false)
                .setPositiveButton("OK", (dialog, id) -> {
                    updateButton.setVisibility(View.GONE);
                    dialog.dismiss();
                });

        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String deleteText = getString(R.string.delete_account);
        SpannableString spannableString = new SpannableString(deleteText);
        spannableString.setSpan(new UnderlineSpan(), 0, deleteText.length(), 0);
        TextView deleteTextView = view.findViewById(R.id.deleteText); // replace with your actual TextView ID
        deleteTextView.setText(spannableString);

        deleteTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteConfirmationDialog(v);
            }
        });
    }

    private void showDeleteConfirmationDialog(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
        builder.setMessage("Are you sure you want to delete your account?")
                .setCancelable(false)
                .setPositiveButton("Yes", (dialog, id) -> {
                    Intent intent = new Intent(v.getContext(), UserLoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                })
                .setNegativeButton("No", (dialog, id) -> dialog.cancel());



        AlertDialog alert = builder.create();
        alert.show();
    }
}