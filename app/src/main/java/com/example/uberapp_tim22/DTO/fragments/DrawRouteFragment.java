package com.example.uberapp_tim22.DTO.fragments;

import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.uberapp_tim22.BuildConfig;
import com.example.uberapp_tim22.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.DirectionsStep;
import com.google.maps.model.EncodedPolyline;
//import com.google.maps.DirectionsApi;
//import com.google.maps.DirectionsApiRequest;
//import com.google.maps.GeoApiContext;
//import com.google.maps.model.DirectionsLeg;
//import com.google.maps.model.DirectionsResult;
//import com.google.maps.model.DirectionsRoute;
//import com.google.maps.model.DirectionsStep;
//import com.google.maps.model.EncodedPolyline;

import java.util.ArrayList;
import java.util.List;

public class DrawRouteFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private String TAG = "so47492459";
    private SupportMapFragment mMapFragment;
    private Marker departureMarker;
    private Marker destinationMarker;
    private Marker driverLocationMarker;
    private List<Marker> markers = new ArrayList<>();


    public static DrawRouteFragment newInstance() {

        DrawRouteFragment mpf = new DrawRouteFragment();

        return mpf;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapFragment = SupportMapFragment.newInstance();

        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.map_container, mMapFragment).commit();

        mMapFragment.getMapAsync(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup vg, Bundle data) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.map_layout, vg, false);

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.getUiSettings().setZoomControlsEnabled(true);
    }

    public void addDepartureMarker(LatLng loc) {

        if (departureMarker != null) {
            departureMarker.remove();
        }

        departureMarker = mMap.addMarker(new MarkerOptions()
                .title("DEPARTURE")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                .position(loc));
        departureMarker.setFlat(true);

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(loc).zoom(3).build();
    }

    public void addDestinationMarker(LatLng loc) {

        if (destinationMarker != null) {
            destinationMarker.remove();
        }

        destinationMarker = mMap.addMarker(new MarkerOptions()
                .title("DESTINATION")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                .position(loc));
        destinationMarker.setFlat(true);

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(loc).zoom(3).build();
    }

    public void addDriverLocationMarker(LatLng loc) {

        if (driverLocationMarker != null) {
            driverLocationMarker.remove();
        }

        driverLocationMarker = mMap.addMarker(new MarkerOptions()
                .title("DRIVER")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                .position(loc));
        driverLocationMarker.setFlat(true);

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(loc).zoom(3).build();
    }

    public void drawLine() {
        LatLng departureLatLng = departureMarker.getPosition();
        LatLng destinationLatLng = destinationMarker.getPosition();

        PolylineOptions polylineOptions = new PolylineOptions()
                .add(departureLatLng)
                .add(destinationLatLng)
                .color(Color.RED)
                .width(5)
                .geodesic(true);

        Polyline polyline = mMap.addPolyline(polylineOptions);
    }

    public void drawLines(){

        double x = (departureMarker.getPosition().latitude + destinationMarker.getPosition().latitude)/2;
        double y = (departureMarker.getPosition().longitude + destinationMarker.getPosition().longitude)/2;

        LatLng zaragoza = new LatLng(x,y);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(zaragoza, 6));


        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey("abc")
                .build();
        DirectionsApiRequest req = DirectionsApi.getDirections(context, departureMarker.getPosition().latitude + "," + departureMarker.getPosition().longitude,
                destinationMarker.getPosition().latitude + "," + destinationMarker.getPosition().longitude);

        List<LatLng> path = new ArrayList();

        try {
            DirectionsResult res = req.await();
            if (res.routes != null && res.routes.length > 0) {
                DirectionsRoute route = res.routes[0];

                if (route.legs != null) {
                    for (int i = 0; i < route.legs.length; i++) {
                        DirectionsLeg leg = route.legs[i];
                        if (leg.steps != null) {
                            for (int j = 0; j < leg.steps.length; j++) {
                                DirectionsStep step = leg.steps[j];
                                if (step.steps != null && step.steps.length > 0) {
                                    for (int k = 0; k < step.steps.length; k++) {
                                        DirectionsStep step1 = step.steps[k];
                                        EncodedPolyline points1 = step1.polyline;
                                        if (points1 != null) {
                                            List<com.google.maps.model.LatLng> coords1 = points1.decodePath();
                                            for (com.google.maps.model.LatLng coord1 : coords1) {
                                                path.add(new LatLng(coord1.lat, coord1.lng));
                                            }
                                        }
                                    }
                                } else {
                                    EncodedPolyline points = step.polyline;
                                    if (points != null) {
                                        List<com.google.maps.model.LatLng> coords = points.decodePath();
                                        for (com.google.maps.model.LatLng coord : coords) {
                                            path.add(new LatLng(coord.lat, coord.lng));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            Log.e(TAG, ex.getLocalizedMessage());
        }

        if (path.size() > 0) {
            PolylineOptions opts = new PolylineOptions().addAll(path).color(Color.BLUE).width(5);
            mMap.addPolyline(opts);
        }
    }

    public void addMarker(LatLng loc, String title, float hue) {
        Marker marker = mMap.addMarker(new MarkerOptions()
                .title(title)
                .icon(BitmapDescriptorFactory.defaultMarker(hue))
                .position(loc));
        marker.setFlat(true);
        markers.add(marker);
    }
}