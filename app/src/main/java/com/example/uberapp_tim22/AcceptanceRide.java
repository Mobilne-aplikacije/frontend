package com.example.uberapp_tim22;

import static com.example.uberapp_tim22.service.ServiceUtils.driverService;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.uberapp_tim22.DTO.DriverDTO;
import com.example.uberapp_tim22.DTO.ResponseRideDTO;
import com.example.uberapp_tim22.DTO.ResponseRideNewDTO;
import com.example.uberapp_tim22.adapters.RideListAdapter;
import com.example.uberapp_tim22.service.ServiceUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AcceptanceRide extends AppCompatActivity implements RideListAdapter.RideItemClickListener{

    Button accept, decline;
    private RideListAdapter rideListAdapter;
    private RecyclerView rideListRecyclerView;
    private List<ResponseRideDTO> rides;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acceptance_ride);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("FAB Car");

//        accept = findViewById(R.id.button4);
//        decline = findViewById(R.id.button7);
        rideListRecyclerView = findViewById(R.id.rideListRecyclerView);
        rideListAdapter = new RideListAdapter(this);
//        sharedPreferences = getSharedPreferences("preferences", MODE_PRIVATE);
//        Long myId = sharedPreferences.getLong("pref_id", 0);

        rideListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        rideListRecyclerView.setAdapter(rideListAdapter);

        getDriverRides("5"); //promeniti

//        accept.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(AcceptanceRide.this, "Uspesno prihvacena voznja", Toast.LENGTH_SHORT).show();
//
//                acceptRide(String.valueOf("5"));
//
//            }
//        });
//
//        decline.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(AcceptanceRide.this, "Odbijena voznja", Toast.LENGTH_SHORT).show();
//                declineRide(String.valueOf("5"));
//
//            }
//        });
    }

    private void getDriverRides(String driverId) {
        Call<List<ResponseRideDTO>> call = ServiceUtils.driverService.getDriverPendingRides(driverId);

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
                Toast.makeText(AcceptanceRide.this, "API call failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void acceptRide(String rideId) {
        Toast.makeText(AcceptanceRide.this, "Uspesno prihvacena voznja", Toast.LENGTH_SHORT).show();

        Call<ResponseRideNewDTO> call = driverService.acceptRide(rideId);

        call.enqueue(new Callback<ResponseRideNewDTO>() {
            @Override
            public void onResponse(Call<ResponseRideNewDTO> call, Response<ResponseRideNewDTO> response) {
                if (response.isSuccessful()) {
                    ResponseRideNewDTO ride = response.body();
                    Log.e("AcceptanceRide", "uspesno prihvacena voznja ");
                } else {
                    onFailure(call, new Throwable("API call failed with status code: " + response.code()));
                }
            }
            @Override
            public void onFailure(Call<ResponseRideNewDTO> call, Throwable t) {
                Log.e("AcceptanceRide", "API call failed: " + t.getMessage());
                Toast.makeText(AcceptanceRide.this, "API call failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onRideItemClick(ResponseRideDTO ride) {
        showPopup(ride);
    }

    private void showPopup(ResponseRideDTO ride) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(AcceptanceRide.this);
        LayoutInflater inflater = LayoutInflater.from(AcceptanceRide.this);
        View dialogView = inflater.inflate(R.layout.acceptance, null);
        dialogBuilder.setView(dialogView);



//        TextView messageTextView = dialogView.findViewById(R.id.messageTextView);
//        String message = "ID: " + ride.getId() + "\n" +
//                "Driver: " + ride.getDriver().getEmail() + "\n" +
//                "Passengers: " + passengersToString(ride.getPassengers()) + "\n" +
//                "Rejection: " + (ride.getRejection() != null ? ride.getRejection().getReason() : "") + "\n" +
//                "Total cost: " + ride.getTotalCost() + "\n" +
//                "Start Time: " + ride.getStartTime() + "\n" +
//                "End Time: " + ride.getEndTime();
//        messageTextView.setText(message);
//        messageTextView.setTextSize(18);

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }

    private void declineRide(String rideId) {
        Toast.makeText(AcceptanceRide.this, "Uspesno odbijena voznja", Toast.LENGTH_SHORT).show();


        Call<ResponseRideNewDTO> call = driverService.withdrawRide(rideId);

        call.enqueue(new Callback<ResponseRideNewDTO>() {
            @Override
            public void onResponse(Call<ResponseRideNewDTO> call, Response<ResponseRideNewDTO> response) {
                if (response.isSuccessful()) {
                    ResponseRideNewDTO ride = response.body();
                    Log.e("AcceptanceRide", "uspesno odbijena voznjaaaa ");
                } else {
                    onFailure(call, new Throwable("API call failed with status code: " + response.code()));
                }
            }
            @Override
            public void onFailure(Call<ResponseRideNewDTO> call, Throwable t) {
                Log.e("AcceptanceRide", "API call failed: " + t.getMessage());
                Toast.makeText(AcceptanceRide.this, "API call failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
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
            Intent intent = new Intent(AcceptanceRide.this, DriverAccountActivity.class);
            startActivity(intent);
        }
        if(id == R.id.item2){
            Intent intent = new Intent(AcceptanceRide.this, DriverInboxActivity.class);
            startActivity(intent);
        }
        if(id == R.id.item3){
            Intent intent = new Intent(AcceptanceRide.this, DriverRideHistory.class);
            startActivity(intent);
        }
        if(id == R.id.item4){
            Intent intent = new Intent(AcceptanceRide.this, DriverMainActivity.class);
            startActivity(intent);
        }
        if(id == R.id.item5){
            Intent intent = new Intent(AcceptanceRide.this, UserLoginActivity.class);
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