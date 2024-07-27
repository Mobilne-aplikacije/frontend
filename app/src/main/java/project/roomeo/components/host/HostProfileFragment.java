package project.roomeo.components.host;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

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

import project.roomeo.R;
import project.roomeo.components.UserLoginActivity;
import project.roomeo.models.Guest;
import project.roomeo.models.Host;
import project.roomeo.service.ServiceUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HostProfileFragment extends Fragment {
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

        fetchHostData();

        return view;
    }


    private void fetchHostData() {

        Call<Host> call = ServiceUtils.hostService.getHost(myId.toString());

        call.enqueue(new Callback<Host>() {
            @Override
            public void onResponse(Call<Host> call, Response<Host> response) {
                if (response.isSuccessful()) {
                    Host host = response.body();
                    if (host != null) {
                        updateUI(host);
                        setupEditing(host);
                    }
                } else {
                    onFailure(call, new Throwable("API call failed with status code: " + response.code()));
                }
            }

            @Override
            public void onFailure(Call<Host> call, Throwable t) {
                Log.e("HostProfileFragment", "API call failed: " + t.getMessage());
            }
        });
    }

    private void updateUI(Host host) {
        roleText.setText("Host");
    }

    private void setupEditing(Host host) {
        nameEditText.setText(host.getFirstName() +" "+host.getLastName());
        phoneEditText.setText(host.getPhoneNumber());
        addressEditText.setText(host.getAddress());
        emailEditText.setText(host.getEmail());

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

                if (!newEmail.equals(host.getEmail()) || !newPhone.equals(host.getPhoneNumber()) || !newAddress.equals(host.getAddress()) || !newName.equals(host.getFirstName()+" "+host.getLastName())) {
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

        updateButton.setOnClickListener(v -> updateHostData());
    }

    private void updateHostData() {
        String newEmail = emailEditText.getText().toString();
        String newPhone = phoneEditText.getText().toString();
        String newFirstname = nameEditText.getText().toString().split(" ")[0];
        String newLastname = nameEditText.getText().toString().split(" ")[1];
        String newAddress = addressEditText.getText().toString();

        Host updatedGuest = new Host();
        updatedGuest.setEmail(newEmail);
        updatedGuest.setPhoneNumber(newPhone);
        updatedGuest.setAddress(newAddress);
        updatedGuest.setFirstName(newFirstname);
        updatedGuest.setLastName(newLastname);
        showUpdateConfirmationDialog();

        Call<Host> call = ServiceUtils.hostService.updateHost(myId.toString(), updatedGuest);

        call.enqueue(new Callback<Host>() {
            @Override
            public void onResponse(Call<Host> call, Response<Host> response) {
                if (response.isSuccessful()) {
                    showUpdateConfirmationDialog();
                } else {
                    Log.e("HostProfileFragment", "Update failed with status code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Host> call, Throwable t) {
                Log.e("HostProfileFragment", "Update failed: " + t.getMessage());
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
        TextView deleteTextView = view.findViewById(R.id.deleteText);
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