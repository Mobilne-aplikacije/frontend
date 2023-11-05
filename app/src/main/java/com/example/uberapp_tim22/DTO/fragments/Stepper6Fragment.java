package com.example.uberapp_tim22.DTO.fragments;

import androidx.fragment.app.Fragment;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


import androidx.annotation.Nullable;

import com.example.uberapp_tim22.R;
import com.example.uberapp_tim22.fragments.DrawRouteFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;

public class Stepper6Fragment extends Fragment {

    public interface DrawRouteCallback {
        void onAddMarker(LatLng location, String title, float hue);
    }

    private Button btn;
    private DrawRouteFragment drawRouteFragment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stepper6, container, false);

        btn = view.findViewById(R.id.fragmentStepper6Btn);
        double driverVehicleLatitude = getArguments().getDouble("driverVehicleLatitude");
        double driverVehicleLongitude = getArguments().getDouble("driverVehicleLongitude");
        String address = getArguments().getString("driverVehicleAddress");

        drawRouteFragment = (DrawRouteFragment) getChildFragmentManager().findFragmentById(R.id.map_container);
        drawRouteFragment = new DrawRouteFragment(); // Create a new instance of DrawRouteFragment
        getChildFragmentManager().beginTransaction()
                .replace(R.id.map_container, drawRouteFragment)
                .commit();


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Lat", String.valueOf(driverVehicleLatitude));
                Log.i("Long", String.valueOf(driverVehicleLongitude));
                Log.i("Address", String.valueOf(address));

                // Add a new marker using the DrawRouteFragment's method
                LatLng newMarkerLocation = new LatLng(driverVehicleLatitude, driverVehicleLongitude);
                drawRouteFragment.addMarker(newMarkerLocation, "New Marker", BitmapDescriptorFactory.HUE_GREEN);
            }
        });

        // ... Rest of the code ...

        return view;
    }


}
