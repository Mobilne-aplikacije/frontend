package com.example.uberapp_tim22.DTO.fragments;

import android.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.uberapp_tim22.DTO.NewLocationDTO;
import com.example.uberapp_tim22.R;
import com.example.uberapp_tim22.fragments.Stepper2Fragment;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Stepper1Fragment extends Fragment {

    private View view;
    private Button secondButton;
    private Bundle bundle = new Bundle();
    private Button stepperDateBtn, fragmentStepper1TimeBtn, fragmentStepper1NextBtn;
    private RadioGroup fragmentStepper1RG;
    private RadioButton fragmentStepper1NowRB, fragmentStepper1ScheduleRB;
    private EditText departureAddressEditText, destinationAddressEditText;
    private TextView fragmentStepper1TextView;
    private int hour, minute;
    private LocalDateTime dateTime;
    private Date currentTime;
    private List<NewLocationDTO> locations;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_stepper1, container, false);

        fragmentStepper1NextBtn=(Button) view.findViewById(R.id.fragmentStepper1NextBtn);
        fragmentStepper1TimeBtn = (Button) view.findViewById(R.id.fragmentStepper1TimeBtn);
        fragmentStepper1RG = (RadioGroup) view.findViewById(R.id.fragmentStepper1RG);
        fragmentStepper1NowRB = (RadioButton) view.findViewById(R.id.fragmentStepper1NowRB);
        fragmentStepper1ScheduleRB = (RadioButton) view.findViewById(R.id.fragmentStepper1ScheduleRB);
        fragmentStepper1TextView = (TextView) view.findViewById(R.id.fragmentStepper1TextView);

        departureAddressEditText = (EditText) view.findViewById(R.id.departureAddressEditText);
        destinationAddressEditText = (EditText) view.findViewById(R.id.destinationAddressEditText);

        fragmentStepper1NextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (!fragmentStepper1ScheduleRB.isChecked()){
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            dateTime = LocalDateTime.now();
                        }
                    }

                    if (dateTime == null) {
                        showDialog("Please select a valid date and time");
                        return;
                    }

                    String departureAddress = departureAddressEditText.getText().toString();
                    if (departureAddress == null || departureAddress.isEmpty()) {
                        showDialog("Please enter a valid departure address");
                        return;
                    }

                    String destinationAddress = destinationAddressEditText.getText().toString();
                    if (destinationAddress == null || destinationAddress.isEmpty()) {
                        showDialog("Please enter a valid destination address");
                        return;
                    }

                    Toast.makeText(getActivity(), dateTime.toString(), Toast.LENGTH_SHORT).show();
                    bundle.putString("departure", departureAddress);
                    bundle.putString("destination", destinationAddress);
                    bundle.putString("date", dateTime.toString());
                    bundle.putSerializable("locations", (Serializable) locations);

                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction fragmentTransition = fm.beginTransaction();
                    com.example.uberapp_tim22.fragments.Stepper2Fragment fragmentStepper2 = new Stepper2Fragment();
                    fragmentStepper2.setArguments(bundle);
                    fragmentTransition.replace(R.id.fragmentStepper2, fragmentStepper2);
                    fragmentTransition.addToBackStack(null);
                    fragmentTransition.commit();
                } catch (Exception e) {
                    e.printStackTrace();
                    showDialog("An error occurred");
                }
            }
        });

        fragmentStepper1RG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (fragmentStepper1ScheduleRB.isChecked()) {
                    fragmentStepper1TimeBtn.setEnabled(true);
                        dateTime = null;
                }
                if (fragmentStepper1NowRB.isChecked()) {
                    fragmentStepper1TimeBtn.setEnabled(false);
                    fragmentStepper1TimeBtn.setTextSize(10);
                    fragmentStepper1TimeBtn.setText("SELECT TIME");
                    fragmentStepper1TextView.setText("");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        dateTime = LocalDateTime.now();
                    }
                }
            }
        });

        fragmentStepper1TimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popTimePicker(v);
            }
        });

        return view;
    }


    @SuppressWarnings("unused")
    public void popTimePicker(View view)
    {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener()
        {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute)
            {
                hour = selectedHour;
                minute = selectedMinute;

                currentTime = Calendar.getInstance().getTime();
                if((hour < currentTime.getHours())){
                    fragmentStepper1TimeBtn.setTextSize(10);
                    fragmentStepper1TimeBtn.setText("SELECT TIME");
                    fragmentStepper1TextView.setText("Cannot select date in the past");
                    dateTime = null;

                }
                else if(hour == currentTime.getHours() && (minute <= currentTime.getMinutes())){
                    fragmentStepper1TimeBtn.setTextSize(10);
                    fragmentStepper1TimeBtn.setText("SELECT TIME");
                    fragmentStepper1TextView.setText("Cannot select date in the past");
                    dateTime = null;

                }
                else if((hour - currentTime.getHours()) > 5){
                    fragmentStepper1TimeBtn.setTextSize(10);
                    fragmentStepper1TimeBtn.setText("SELECT TIME");
                    fragmentStepper1TextView.setText("Exceeded 5 hours limit");
                    dateTime = null;
                }
                else {
                    fragmentStepper1TimeBtn.setText(String.format(Locale.getDefault(), "%02d:%02d", hour, minute));

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        String timeString = String.format(Locale.getDefault(), "%02d:%02d", hour, minute);
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
                        LocalTime time = LocalTime.parse(timeString, formatter);
                        dateTime = LocalDateTime.of(LocalDate.now(), time);                  }

                    fragmentStepper1TimeBtn.setTextSize(17);
                    fragmentStepper1TextView.setText("");
                }
            }
        };

        int style = AlertDialog.THEME_HOLO_DARK;

        TimePickerDialog timePickerDialog = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            timePickerDialog = new TimePickerDialog(getContext(), style, onTimeSetListener, hour, minute, true);
        }

        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();
    }

    private void showDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void setLocations(List<NewLocationDTO> locations) {
        this.locations = locations;
    }
}