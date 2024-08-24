package project.roomeo.components.admin;

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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import project.roomeo.DTO.RequestResetPasswordDTO;
import project.roomeo.R;
import project.roomeo.components.UserLoginActivity;
import project.roomeo.models.Admin;
import project.roomeo.service.ServiceUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminProfileFragment extends Fragment {
    private Long myId;
    private EditText nameEditText;
    private EditText phoneEditText;
    private EditText addressEditText;
    private EditText emailEditText;
    private Button updateButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_admin_profile, container, false);

        nameEditText = view.findViewById(R.id.nameText);
        phoneEditText = view.findViewById(R.id.phoneText);
        addressEditText = view.findViewById(R.id.addressText);
        emailEditText = view.findViewById(R.id.emailText);
        updateButton = view.findViewById(R.id.updateButton);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("preferences", Context.MODE_PRIVATE);
        String myEmail = sharedPreferences.getString("pref_email", "");
        myId = sharedPreferences.getLong("pref_id", 0L);
        Log.e("PROVERA ID", "provera id: " + myId);

        fetchAdminData();

        return view;
    }

    private void fetchAdminData() {
        Call<Admin> call = ServiceUtils.adminService.getAdmin(myId.toString());

        call.enqueue(new Callback<Admin>() {
            @Override
            public void onResponse(Call<Admin> call, Response<Admin> response) {
                if (response.isSuccessful()) {
                    Admin admin = response.body();
                    if (admin != null) {
                        updateUI(admin);
                        setupEditing(admin);
                    }
                } else {
                    onFailure(call, new Throwable("API call failed with status code: " + response.code()));
                }
            }

            @Override
            public void onFailure(Call<Admin> call, Throwable t) {
                Log.e("AdminProfileFragment", "API call failed: " + t.getMessage());
            }
        });
    }

    private void updateUI(Admin admin) {
        nameEditText.setText(admin.getFirstName() + " " + admin.getLastName());
        phoneEditText.setText(admin.getPhoneNumber());
        addressEditText.setText(admin.getAddress());
        emailEditText.setText(admin.getEmail());
    }

    private void setupEditing(Admin admin) {
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String newEmail = emailEditText.getText().toString();
                String newPhone = phoneEditText.getText().toString();
                String newAddress = addressEditText.getText().toString();
                String newName = nameEditText.getText().toString();

                if (!newEmail.equals(admin.getEmail()) || !newPhone.equals(admin.getPhoneNumber()) ||
                        !newAddress.equals(admin.getAddress()) || !newName.equals(admin.getFirstName() + " " + admin.getLastName())) {
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

        updateButton.setOnClickListener(v -> updateAdminData());
    }

    private void updateAdminData() {
        String newEmail = emailEditText.getText().toString();
        String newPhone = phoneEditText.getText().toString();
        String newFirstname = nameEditText.getText().toString().split(" ")[0];
        String newLastname = nameEditText.getText().toString().split(" ")[1];
        String newAddress = addressEditText.getText().toString();

        Admin updatedAdmin = new Admin();
        updatedAdmin.setEmail(newEmail);
        updatedAdmin.setPhoneNumber(newPhone);
        updatedAdmin.setAddress(newAddress);
        updatedAdmin.setFirstName(newFirstname);
        updatedAdmin.setLastName(newLastname);

        Call<Admin> call = ServiceUtils.adminService.updateAdmin(myId.toString(), updatedAdmin);

        call.enqueue(new Callback<Admin>() {
            @Override
            public void onResponse(Call<Admin> call, Response<Admin> response) {
                if (response.isSuccessful()) {
                    showUpdateConfirmationDialog();
                } else {
                    Log.e("AdminProfileFragment", "Update failed with status code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Admin> call, Throwable t) {
                Log.e("AdminProfileFragment", "Update failed: " + t.getMessage());
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

        LinearLayout resetPasswordButton = view.findViewById(R.id.resetPasswordButton);
        resetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showResetPasswordConfirmationDialog();
            }
        });
    }

    private void showDeleteConfirmationDialog(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
        builder.setMessage("Are you sure you want to delete your account?")
                .setCancelable(false)
                .setPositiveButton("Yes", (dialog, id) -> {
                    deleteAccount();
                })
                .setNegativeButton("No", (dialog, id) -> dialog.cancel());

        AlertDialog alert = builder.create();
        alert.show();
    }

    private void deleteAccount() {
        Call<Void> call = ServiceUtils.userService.deleteAccount(myId);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Account successfully deleted!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getContext(), UserLoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                } else {
                    Toast.makeText(getContext(), "Failed to delete account. Please try again.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getContext(), "An error occurred. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showResetPasswordConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Are you sure you want to reset your password?")
                .setCancelable(false)
                .setPositiveButton("Yes", (dialog, id) -> {
                    showResetPasswordDialog();
                })
                .setNegativeButton("No", (dialog, id) -> dialog.cancel());

        AlertDialog alert = builder.create();
        alert.show();
    }

    private void showResetPasswordDialog() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View dialogView = inflater.inflate(R.layout.dialog_reset_password, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(dialogView);

        EditText oldPassword = dialogView.findViewById(R.id.oldPassword);
        EditText newPassword = dialogView.findViewById(R.id.newPassword);
        EditText confirmPassword = dialogView.findViewById(R.id.confirmPassword);

        builder.setPositiveButton("Reset", (dialog, which) -> {
            String oldPass = oldPassword.getText().toString();
            String newPass = newPassword.getText().toString();
            String confirmPass = confirmPassword.getText().toString();

            if (newPass.equals(confirmPass)) {
                resetPassword(oldPass, newPass);
            } else {
                Toast.makeText(getContext(), "New passwords do not match!", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void resetPassword(String oldPassword, String newPassword) {
        RequestResetPasswordDTO resetPasswordDTO = new RequestResetPasswordDTO(oldPassword, newPassword);
        Call<Void> call = ServiceUtils.userService.resetPassword(myId, resetPasswordDTO);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Password successfully reset!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Password reset failed. Please try again.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getContext(), "An error occurred. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
