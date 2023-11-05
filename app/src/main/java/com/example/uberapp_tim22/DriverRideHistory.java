package com.example.uberapp_tim22;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.example.uberapp_tim22.DTO.IdAndEmailDTO;
import com.example.uberapp_tim22.DTO.ResponseRideDTO;
import com.example.uberapp_tim22.adapters.RideListAdapter;
import com.example.uberapp_tim22.service.ServiceUtils;
import com.example.uberapp_tim22.tools.ShakeDetector;
import com.example.uberapp_tim22.tools.ShakePack;

public class DriverRideHistory extends AppCompatActivity implements RideListAdapter.RideItemClickListener, SensorEventListener {

    private RecyclerView rideListRecyclerView;
    private SensorManager sensorManager;
    private ShakePack shakePack = new ShakePack(12);
    private RideListAdapter rideListAdapter;
    private SharedPreferences sharedPreferences;
    private boolean opadajuce = true;
    private List<ResponseRideDTO> rides;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_ride_history);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("FAB Car");
        initSensorManager();

        rideListRecyclerView = findViewById(R.id.rideListRecyclerView);
        rideListAdapter = new RideListAdapter(this);
//        sharedPreferences = getSharedPreferences("preferences", MODE_PRIVATE);
//        Long myId = sharedPreferences.getLong("pref_id", 0);

        rideListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        rideListRecyclerView.setAdapter(rideListAdapter);

        getDriverRides("5"); //promeniti


//        ShakeDetector shakeDetector = new ShakeDetector(this);
//        shakeDetector.setOnShakeListener(new ShakeDetector.OnShakeListener() {
//            @Override
//            public void onShake() {
//               if (opadajuce){
//                   Collections.sort(rides, new Comparator<ResponseRideDTO>() {
//                       @Override
//                       public int compare(ResponseRideDTO o1, ResponseRideDTO o2) {
//                           return o1.getEndTime().compareTo(o2.getEndTime());
//                       }
//                   });
//
//                   //redosled od najnovije
//               }else{
//                   //redosled od najstarije
//                   Collections.sort(rides, new Comparator<ResponseRideDTO>() {
//                       @Override
//                       public int compare(ResponseRideDTO o1, ResponseRideDTO o2) {
//                           return o2.getEndTime().compareTo(o1.getEndTime());
//                       }
//                   });
//               }
//
//                rideListAdapter.setRideList(rides);
//
//                }
//        });



    }
    private void initSensorManager() {
        sensorManager = (SensorManager) this.getSystemService(Context.SENSOR_SERVICE);
    }

    private void getDriverRides(String driverId) {
        Call<List<ResponseRideDTO>> call = ServiceUtils.driverService.getDriverRides(driverId);

        call.enqueue(new Callback<List<ResponseRideDTO>>() {
            @Override
            public void onResponse(Call<List<ResponseRideDTO>> call, Response<List<ResponseRideDTO>> response) {
                if (response.isSuccessful()) {
                    List<ResponseRideDTO> rideList = response.body();
                    if (rideList != null) {
                        rideListAdapter.setRideList(rideList);
                        rides = rideList;
                        Log.i("123",rideListAdapter.toString());
                    }
                } else {
                    onFailure(call, new Throwable("API call failed with status code: " + response.code()));
                }
            }

            @Override
            public void onFailure(Call<List<ResponseRideDTO>> call, Throwable t) {
                Log.e("DriverRideHistory", "API call failed: " + t.getMessage());
                Toast.makeText(DriverRideHistory.this, "API call failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onRideItemClick(ResponseRideDTO ride) {
        showPopup(ride);
    }

    public String passengersToString(List<IdAndEmailDTO> passengers){
        String passengerString = "";
        for(IdAndEmailDTO passenger : passengers){
            passengerString = passengerString + passenger.getEmail() + " ";
        }

        return passengerString;
    }

    private void showPopup(ResponseRideDTO ride) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(DriverRideHistory.this);
        LayoutInflater inflater = LayoutInflater.from(DriverRideHistory.this);
        View dialogView = inflater.inflate(R.layout.dialog_ride_details, null);
        dialogBuilder.setView(dialogView);

        TextView messageTextView = dialogView.findViewById(R.id.messageTextView);
        String message = "ID: " + ride.getId() + "\n" +
                "Driver: " + ride.getDriver().getEmail() + "\n" +
                "Passengers: " + passengersToString(ride.getPassengers()) + "\n" +
                "Rejection: " + (ride.getRejection() != null ? ride.getRejection().getReason() : "") + "\n" +
                "Total cost: " + ride.getTotalCost() + "\n" +
                "Start Time: " + ride.getStartTime() + "\n" +
                "End Time: " + ride.getEndTime();
        messageTextView.setText(message);
        messageTextView.setTextSize(18);

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
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
            Intent intent = new Intent(DriverRideHistory.this, DriverAccountActivity.class);
            startActivity(intent);
        }
        if(id == R.id.item2){
            Intent intent = new Intent(DriverRideHistory.this, DriverInboxActivity.class);
            startActivity(intent);
        }
        if(id == R.id.item3){
            Intent intent = new Intent(DriverRideHistory.this, DriverRideHistory.class);
            startActivity(intent);
        }
        if(id == R.id.item4){
            Intent intent = new Intent(DriverRideHistory.this, DriverMainActivity.class);
            startActivity(intent);
        }
        if(id == R.id.item5){
            Intent intent = new Intent(DriverRideHistory.this, UserLoginActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void deletePreferences(){
        SharedPreferences sharedPreferences = getSharedPreferences("preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor spEditor = sharedPreferences.edit();
        spEditor.clear().commit();
    }


    @Override
    protected void onStart() {
        super.onStart();
        Toast.makeText(this, "onStart()", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(
                this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL
        );
        Toast.makeText(this, "onResume()",Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
        Toast.makeText(this, "onPause()",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            shakePack.update(sensorEvent.values);
            if (shakePack.isShaking()) {
                onShake();
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    private void onShake() {
        Collections.reverse(rides);
        rideListAdapter.setRideList(rides);
        //((EasyListAdapter)this.listView.getAdapter()).notifyDataSetChanged();
        Toast.makeText(this, "Shaking detected, reversing the list", Toast.LENGTH_SHORT).show();
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