package project.roomeo.components.host;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import project.roomeo.R;
import project.roomeo.models.Accommodation;
import project.roomeo.models.enums.AccommodationRequestStatus;
import project.roomeo.models.enums.AccommodationType;
import project.roomeo.models.enums.BookingMethod;
import project.roomeo.models.enums.Payment;

public class Step2Fragment extends Fragment {

    private CheckBox checkBoxWifi;
    private CheckBox checkBoxKitchen;
    private CheckBox checkBoxAirConditioner;
    private CheckBox checkBoxParking;
    private EditText editTextPrice;
    private CheckBox checkBoxAutomaticReservation;
    private Spinner dropdownButton, dropdownButton2;
    private Accommodation accommodation;

    public void setAccommodation(Accommodation accommodation) {
        this.accommodation = accommodation;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_step2, container, false);

        // Inicijalizacija elemenata
        checkBoxWifi = view.findViewById(R.id.checkBoxWifi);
        checkBoxKitchen = view.findViewById(R.id.checkBoxKitchen);
        checkBoxAirConditioner = view.findViewById(R.id.checkBoxAirConditioner);
        checkBoxParking = view.findViewById(R.id.checkBoxParking);
        editTextPrice = view.findViewById(R.id.editTextPrice);
        checkBoxAutomaticReservation = view.findViewById(R.id.checkBoxAutomaticReservation);
        dropdownButton = view.findViewById(R.id.dropdownButton);
        dropdownButton2 = view.findViewById(R.id.dropdownButton2);

        // Prvi Spinner (dropdownButton)
        List<String> options = new ArrayList<>();
        options.add("Studio");
        options.add("Room");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, options);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropdownButton.setAdapter(adapter);

        // Drugi Spinner (dropdownButton2)
        List<String> options2 = new ArrayList<>();
        options2.add("Price per person");
        options2.add("Price per room");
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, options2);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropdownButton2.setAdapter(adapter2);


        TextView dateRangeTextView = view.findViewById(R.id.datePickerEditText);

        MaterialDatePicker<Pair<Long, Long>> picker = MaterialDatePicker.Builder.dateRangePicker().build();
        picker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Pair<Long, Long>>() {
            @Override
            public void onPositiveButtonClick(Pair<Long, Long> selection) {
                // Handle the selected date range here
                SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
                String startDate = dateFormat.format(selection.first);
                String endDate = dateFormat.format(selection.second);
                String dateRange = startDate + " - " + endDate;
                dateRangeTextView.setText(dateRange);
            }
        });

        dateRangeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picker.show(getParentFragmentManager(), picker.toString());
            }
        });

        Button next = view.findViewById(R.id.nextButton);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateData()) {
                    Step3Fragment fragment = new Step3Fragment();
                    fragment.setAccommodation(getData());
                    ((HostMainActivity) v.getContext()).loadFragment(fragment);

                }
            }
        });

        return view;
    }

    public boolean validateData() {
        String price = editTextPrice.getText().toString();

        if (TextUtils.isEmpty(price)) {
            editTextPrice.setError("Price is required");
            return false;
        }
        return true;
    }

    public Accommodation getData() {

        boolean wifiChecked = checkBoxWifi.isChecked();
        boolean kitchenChecked = checkBoxKitchen.isChecked();
        boolean airConditionerChecked = checkBoxAirConditioner.isChecked();
        boolean parkingChecked = checkBoxParking.isChecked();
        String price = editTextPrice.getText().toString();
        boolean automaticReservationChecked = checkBoxAutomaticReservation.isChecked();
        String dropdownValue1 = dropdownButton.getSelectedItem().toString();
        String dropdownValue2 = dropdownButton2.getSelectedItem().toString();

        accommodation.setHostId(8);
        accommodation.setStatus(AccommodationRequestStatus.PENDING);

        accommodation.setWifi(wifiChecked);
        accommodation.setKitchen(kitchenChecked);
        accommodation.setAirConditioner(airConditionerChecked);
        accommodation.setParking(parkingChecked);
        accommodation.setPrice(Integer.parseInt(price));
        accommodation.setPayment(Payment.PerAccommodation);
        accommodation.setBookingMethod(BookingMethod.AUTOMATIC);
        accommodation.setType(AccommodationType.ROOM);

        return accommodation;
    }
}
