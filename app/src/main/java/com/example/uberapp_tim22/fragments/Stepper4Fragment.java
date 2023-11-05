package com.example.uberapp_tim22.fragments;

import android.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.auth0.android.jwt.JWT;
import com.example.uberapp_tim22.DTO.EmailDTO;
import com.example.uberapp_tim22.DTO.IdAndEmailDTO;
import com.example.uberapp_tim22.DTO.RequestLoginDTO;
import com.example.uberapp_tim22.DTO.ResponseLoginDTO;
import com.example.uberapp_tim22.DriverMainActivity;
import com.example.uberapp_tim22.PassengerMainActivity;
import com.example.uberapp_tim22.R;
import com.example.uberapp_tim22.UserLoginActivity;
import com.example.uberapp_tim22.service.ServiceUtils;
import com.google.android.material.textfield.TextInputEditText;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Stepper4Fragment extends Fragment {

    private TextInputEditText emailEditText;
    private LinearLayout layoutEmailList;
    private Bundle bundle = new Bundle();
    private Button buttonAdd, nextButton, backButton;
    private List<IdAndEmailDTO> passengers = new ArrayList<>();
    private boolean attributeExists;
    private LinearLayout lastAddedItemLayout;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor spEditor;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_stepper4, container, false);
        emailEditText = view.findViewById(R.id.EmailEditText);
        layoutEmailList = view.findViewById(R.id.layout_email_list);
        buttonAdd = view.findViewById(R.id.buttonAddEmail);
        nextButton = view.findViewById(R.id.fragmentStepper4NextBtn);
        backButton = view.findViewById(R.id.fragmentStepper4BackBtn);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("preferences", Context.MODE_PRIVATE);
        String myEmail = sharedPreferences.getString("pref_email", "");
        Long myId = sharedPreferences.getLong("pref_id", 0);
        passengers.add(new IdAndEmailDTO(myId, myEmail));

        bundle = getArguments();

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addAddressToList();
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Waiting for others conformation")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Fragment fragment = new Stepper5Fragment();
                                FragmentManager fragmentManager = getFragmentManager();
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction.replace(R.id.fragmentStepper2, fragment);
                                bundle.putSerializable("passengers", (Serializable) passengers);
                                bundle.putLong("myId", myId);
                                bundle.putLong("myIdd", myId);
                                fragment.setArguments(bundle);
                                fragmentTransaction.addToBackStack(null);
                                fragmentTransaction.commit();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

        return view;
    }


    private void checkIfExist(String email, OnCheckExistListener listener) {

        EmailDTO emailDTO = new EmailDTO(email);
        Call<IdAndEmailDTO> call = ServiceUtils.passengerService.getPassengerIdAndEmail(emailDTO);
        call.enqueue(new Callback<IdAndEmailDTO>() {

            @Override
            public void onResponse(@NonNull Call<IdAndEmailDTO> call, @NonNull Response<IdAndEmailDTO> response) {

                if (!response.isSuccessful()) {
                    listener.onExist(false);
                    return;
                }

                if (response.code() == 204) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        Toast.makeText(getContext(), "Email not confirmed!", Toast.LENGTH_SHORT).show();
                    }
                    listener.onExist(false);
                    return;
                }

                attributeExists = false;
                IdAndEmailDTO idEmailResponse = response.body();
                for (IdAndEmailDTO passenger : passengers) {
                    if (passenger.getEmail().equals(idEmailResponse.getEmail())) {
                        attributeExists = true;
                        break;
                    }
                }

                if (attributeExists) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        listener.onExist(false);
                        Toast.makeText(getContext(), "Passenger already added", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    passengers.add(idEmailResponse);
                    listener.onExist(true);
                }
            }

            @Override
            public void onFailure(Call<IdAndEmailDTO> call, Throwable t) {
                Log.d("Adding Failed", t.getMessage());
            }
        });
    }

    interface OnCheckExistListener {
        void onExist(boolean exist);
    }


    private void addAddressToList() {
        String email = emailEditText.getText().toString();
        checkIfExist(email, new OnCheckExistListener() {
            @Override
            public void onExist(boolean exist) {
                if(!exist)return;
                if (!TextUtils.isEmpty(email)) {

                    LinearLayout itemLayout = new LinearLayout(getActivity());
                    itemLayout.setOrientation(LinearLayout.HORIZONTAL);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    itemLayout.setLayoutParams(layoutParams);

                    TextView textView = new TextView(getActivity());
                    textView.setText(email);
                    textView.setTextSize(18);
                    LinearLayout.LayoutParams textViewParams = new LinearLayout.LayoutParams(
                            0, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
                    textView.setLayoutParams(textViewParams);

                    ImageView deleteButton = new ImageView(getActivity());
                    deleteButton.setImageResource(R.drawable.ic_delete);
                    LinearLayout.LayoutParams deleteButtonParams = new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    deleteButton.setLayoutParams(deleteButtonParams);

                    deleteButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (itemLayout == lastAddedItemLayout) {
                                layoutEmailList.removeView(itemLayout);
                                passengers.remove(passengers.size()-1);
                                int childCount = layoutEmailList.getChildCount();
                                if (childCount > 0) {
                                    View lastChild = layoutEmailList.getChildAt(childCount - 1);
                                    if (lastChild instanceof LinearLayout) {
                                        lastAddedItemLayout = (LinearLayout) lastChild;
                                    }
                                } else {
                                    lastAddedItemLayout = null;
                                }
                            } else {
                                Toast.makeText(getActivity(), "You can only delete the last added item.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    itemLayout.addView(textView);
                    itemLayout.addView(deleteButton);

                    layoutEmailList.addView(itemLayout);
                    lastAddedItemLayout = itemLayout;
                    emailEditText.setText("");
                }
            }
        });
    }

//    @Override
//    public void onActivityCreated(Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        Toast.makeText(getActivity(), "onActivityCreated()", Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        Toast.makeText(getActivity(), "onAttach()", Toast.LENGTH_SHORT).show();
//    }
}
