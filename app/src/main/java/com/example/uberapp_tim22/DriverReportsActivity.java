package com.example.uberapp_tim22;

import androidx.annotation.ColorInt;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.uberapp_tim22.DTO.ResponseRideDTO;
import com.example.uberapp_tim22.adapters.RideListAdapter;
import com.example.uberapp_tim22.model.BarChartView;
import com.example.uberapp_tim22.model.Ride;
import com.example.uberapp_tim22.service.ServiceUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DriverReportsActivity extends AppCompatActivity {

    private RideListAdapter rideListAdapter;
    private RecyclerView rideListRecyclerView;
    private Button prikaz;
    private List<ResponseRideDTO> rides;
    private Date from;
    private Date to;
    private BarChartView barChart;
    private BarChartView barChart2;
    private BarChartView barChart3;
    private int cs1,cs2,cs3;
    private int avg1,avg2,avg3;

    private Button selectDateButton;
    private TextView selectedDateTextView;
    private Button selectDateButtonTo;
    private TextView selectedDateTextViewTo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_reports);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("FAB Car");


        rides = new ArrayList<ResponseRideDTO>();

//        sharedPreferences = getSharedPreferences("preferences", MODE_PRIVATE);
//        Long myId = sharedPreferences.getLong("pref_id", 0);
//        rideListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//        rideListRecyclerView.setAdapter(rideListAdapter);


        prikaz = findViewById(R.id.prikaz);
        prikaz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup();
            }
        });

        selectDateButton = findViewById(R.id.select_date_button);
        selectedDateTextView = findViewById(R.id.selected_date_text_view);

        selectDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });

        selectDateButtonTo = findViewById(R.id.select_date_button2);
        selectedDateTextViewTo = findViewById(R.id.selected_date_text_view2);

        selectDateButtonTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog2();
            }
        });


    }

    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int selectedYear, int selectedMonth, int selectedDay) {
                        Calendar selectedCalendar = Calendar.getInstance();
                        selectedCalendar.set(selectedYear, selectedMonth, selectedDay);

                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

                        from = selectedCalendar.getTime();
                        String dateString = dateFormat.format(from);
                        selectedDateTextView.setText("Selected Date: " + dateString);
                    }
                }, year, month, day);

        datePickerDialog.show();
    }
    private void showDatePickerDialog2() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int selectedYear, int selectedMonth, int selectedDay) {
                        Calendar selectedCalendar = Calendar.getInstance();
                        selectedCalendar.set(selectedYear, selectedMonth, selectedDay);

                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

                        to = selectedCalendar.getTime();
                        String dateString = dateFormat.format(to);
                        selectedDateTextViewTo.setText("Selected Date: " + dateString);
                    }
                }, year, month, day);

        datePickerDialog.show();
    }
    private void getDriverRides(String driverId) {
        Call<List<ResponseRideDTO>> call = ServiceUtils.driverService.getDriverRides("5");

        call.enqueue(new Callback<List<ResponseRideDTO>>() {
            @Override
            public void onResponse(Call<List<ResponseRideDTO>> call, Response<List<ResponseRideDTO>> response) {
                if (response.isSuccessful()) {
                    List<ResponseRideDTO> rideList = response.body();
                    rides.clear();
                    for (ResponseRideDTO r: rideList) {
                        if (r.getStartTime().after(from) && r.getStartTime().before(to)){
                            rides.add(r);
                        }
                        }
                    Log.i("voznjiceeee", String.valueOf(rides.size()));

                    if (rides.size()>0){
                    initializeCharts();}

                } else {
                    onFailure(call, new Throwable("API call failed with status code: " + response.code()));
                }
            }

            @Override
            public void onFailure(Call<List<ResponseRideDTO>> call, Throwable t) {
                Log.e("DriverRideHistory", "API call failed: " + t.getMessage());
                Toast.makeText(DriverReportsActivity.this, "API call failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initializeCharts() {
        barChart3 = findViewById(R.id.barChart3);
        getChart3();
        barChart2 = findViewById(R.id.barChart2);
        getChart2();
        barChart = findViewById(R.id.barChart1);
        getChart1();

        TableLayout tableLayout = findViewById(R.id.tableLayout);
        TableRow row1 = new TableRow(this);
        TableRow row2 = new TableRow(this);
        TableRow row3 = new TableRow(this);

        TextView cell0 = new TextView(this);
        cell0.setText("");
        row1.addView(cell0);

        TextView cell1 = new TextView(this);
        cell1.setText("KM");
        row1.addView(cell1);

        TextView cell2 = new TextView(this);
        cell2.setText("Money");
        row1.addView(cell2);

        TextView cell3 = new TextView(this);
        cell3.setText("Rides");
        row1.addView(cell3);

        TextView cell20 = new TextView(this);
        cell20.setText("Cumulative sum");
        row2.addView(cell20);
        TextView cell21 = new TextView(this);
        cell21.setText(String.valueOf(cs1));
        row2.addView(cell21);
        TextView cell22 = new TextView(this);
        cell22.setText(String.valueOf(cs2));
        row2.addView(cell22);
        TextView cell23 = new TextView(this);
        cell23.setText(String.valueOf(cs3));
        row2.addView(cell23);

        TextView cell30 = new TextView(this);
        cell30.setText("Average");
        row3.addView(cell30);
        TextView cell31 = new TextView(this);
        cell31.setText(String.valueOf(avg1));
        row3.addView(cell31);
        TextView cell32 = new TextView(this);
        cell32.setText(String.valueOf(avg2));
        row3.addView(cell32);
        TextView cell33 = new TextView(this);
        cell33.setText(String.valueOf(avg3));
        row3.addView(cell33);


        tableLayout.addView(row1);
        tableLayout.addView(row2);
        tableLayout.addView(row3);
    }

    private void showPopup() {

        getDriverRides("5");
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
            Intent intent = new Intent(DriverReportsActivity.this, DriverAccountActivity.class);
            startActivity(intent);
        }
        if(id == R.id.item2){
            Intent intent = new Intent(DriverReportsActivity.this, DriverInboxActivity.class);
            startActivity(intent);
        }
        if(id == R.id.item3){
            Intent intent = new Intent(DriverReportsActivity.this, DriverRideHistory.class);
            startActivity(intent);
        }
        if(id == R.id.item4){
            Intent intent = new Intent(DriverReportsActivity.this, DriverMainActivity.class);
            startActivity(intent);
        }
        if(id == R.id.item5){
            Intent intent = new Intent(DriverReportsActivity.this, UserLoginActivity.class);
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

    public void getChart1(){
        List<Date> dates = new ArrayList<>();

        for (ResponseRideDTO r:rides){
            dates.add(r.getStartTime());
        }

        Collections.sort(dates);
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        List<String> labels = new ArrayList<>();

        for (Date date : dates) {
            String label = sdf.format(date);
            labels.add(label);
        }

        List<String> labelss = labels;
        List<Integer> values = new ArrayList<>();

        cs1=0;
        avg1=0;

        for (ResponseRideDTO r:rides){
            values.add(r.getTotalCost()+300);
            cs1+=r.getTotalCost()+300;
        }
        avg1=cs1/values.size();
        barChart.setData(labelss, values);
        //barChart.setBackgroundColor(@ColorInt );
    }

    public void getChart2(){
        List<Date> dates = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();

        for (ResponseRideDTO r:rides){
            dates.add(r.getStartTime());
        }

        Collections.sort(dates);
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        List<String> labels = new ArrayList<>();

        for (Date date : dates) {
            String label = sdf.format(date);
            labels.add(label);
        }

        List<String> labelss = labels;
        List<Integer> values = new ArrayList<>();

        cs2=0;
        avg2=0;

        for (ResponseRideDTO r:rides){
            values.add(r.getTotalCost());
            cs2+=r.getTotalCost();
        }
        avg2=cs2/values.size();
        //List<Integer> values = Arrays.asList(1, 1, 2, 1);
        //todo ako je isti dan
//        barChart2=null;
        barChart2.setData(labelss, values);
    }
    public void getChart3(){
        List<Date> dates = new ArrayList<>();

        for (ResponseRideDTO r:rides){
            dates.add(r.getStartTime());
        }

        Collections.sort(dates);
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        List<String> labels = new ArrayList<>();

        for (Date date : dates) {
            String label = sdf.format(date);
            labels.add(label);
        }

        List<String> labelss = labels;
        List<Integer> values = new ArrayList<>();

        cs3=0;
        avg3=0;

        for (ResponseRideDTO r:rides){
            values.add(1);
            cs3+=1;
        }
        avg3=cs3/values.size();
        //List<Integer> values = Arrays.asList(1, 1, 2, 1);
        //todo ako je isti dan
//        barChart3=null;
        barChart3.setData(labelss, values);
    }
}