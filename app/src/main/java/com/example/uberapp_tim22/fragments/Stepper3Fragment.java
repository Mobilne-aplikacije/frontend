package com.example.uberapp_tim22.fragments;


import android.app.AlertDialog;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.widget.Button;
import android.widget.RadioButton;

import com.example.uberapp_tim22.DTO.AssumptionDTO;
import com.example.uberapp_tim22.DTO.LocationDTO;
import com.example.uberapp_tim22.R;
import com.example.uberapp_tim22.model.enums.VehicleName;

import java.util.List;

public class Stepper3Fragment extends Fragment {

    private View view;
    private Button nextButton;
    private RadioButton standardTypeRB, luxuryTypeRB, vanTypeRB, petTransportYesRB, petTransportNoRB, babyTransportYesRB, babyTransportNoRB;
    private VehicleName vehicleName;
    private boolean petTransport;
    private boolean babyTransport;
    private Bundle bundle = new Bundle();
    private List<LocationDTO> locations;
    private String date;
    private AssumptionDTO assumptionDTO;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_stepper3, container, false);
        standardTypeRB = view.findViewById(R.id.fragmentStepper3StandardTypeRB);
        luxuryTypeRB = view.findViewById(R.id.fragmentStepper3LuxuryTypeRB);
        vanTypeRB = view.findViewById(R.id.fragmentStepper3VanTypeRB);
        petTransportYesRB = view.findViewById(R.id.fragmentStepper3PetTransportYesRB);
        petTransportNoRB = view.findViewById(R.id.fragmentStepper3PetTransportNoRB);
        babyTransportYesRB = view.findViewById(R.id.fragmentStepper3BabyTransportYesRB);
        babyTransportYesRB = view.findViewById(R.id.fragmentStepper3BabyTransportYesRB);
        nextButton = view.findViewById(R.id.fragmentStepper3NextBtn);


        bundle = getArguments();
        date = getArguments().getString("date");
        locations = (List<LocationDTO>) getArguments().getSerializable("locations");

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (standardTypeRB.isChecked()) {
                    vehicleName = VehicleName.STANDARD;
                } else if (luxuryTypeRB.isChecked()) {
                    vehicleName = VehicleName.LUXURY;
                } else if (vanTypeRB.isChecked()){
                    vehicleName = VehicleName.VAN;
                }

                if (petTransportYesRB.isChecked()) {
                    petTransport = true;
                } else if (petTransportNoRB.isChecked()) {
                    petTransport = false;
                }

                if (babyTransportYesRB.isChecked()) {
                    babyTransport = true;
                } else if (babyTransportNoRB.isChecked()) {
                    babyTransport = false;
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                String price = "Price: " + "100";
                String time = "Time: " + "100";
                builder.setTitle("Confirmation")
                        .setMessage(price + ", " + time)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                bundle.putSerializable("vehicleName", vehicleName);
                                bundle.putBoolean("petTransport", petTransport);
                                bundle.putBoolean("babyTransport", babyTransport);
                                Fragment fragment = new Stepper4Fragment();
                                FragmentManager fragmentManager = getFragmentManager();
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                fragment.setArguments(bundle);
                                fragmentTransaction.replace(R.id.fragmentStepper2, fragment);
                                fragmentTransaction.addToBackStack(null);
                                fragmentTransaction.commit();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });

        Button backButton = view.findViewById(R.id.fragmentStepper3BackBtn);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

        return view;
    }

}