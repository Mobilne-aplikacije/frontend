package project.roomeo.components.guest;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import androidx.core.util.Pair;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import project.roomeo.R;
import project.roomeo.components.ImageSliderAdapter;
import project.roomeo.models.Accommodation;
import project.roomeo.models.Rating;
import project.roomeo.models.Reservation;
import project.roomeo.models.enums.ReservationRequestStatus;
import project.roomeo.service.ServiceUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GuestAccommodationFragment extends Fragment implements OnMapReadyCallback {

    private Accommodation accommodation;
    public TextView name;
    public TextView description;
    public TextView totalPrice;
    public TextView reservationFromDate;
    public TextView reservationNumberOfPeople;
    public TextView reservationToDate;
    private ImageView wifiIcon;
    private ImageView kitchenIcon;
    private ImageView airConditionerIcon;
    private ImageView parkingIcon;
    public TextView price;
    public TextView guestCapacity;
    public RatingBar averageRate;
    public Long myId;
    private ViewPager2 viewPager;
    private MapView mapView;
    private TextView reviewCount;
    private CalendarView calendarView;
    private Button reserve;
    private int numberOfPeople = -1;
    private String reserveFrom = "";
    private String reserveTo = "";



    public GuestAccommodationFragment() {
    }

    public GuestAccommodationFragment(boolean pending, Long myId) {
        this.myId = myId;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_guest_accommodation, container, false);
        return view;
    }

    public void setAccommodationRequest(Accommodation request) {
        this.accommodation = request;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        name = getView().findViewById(R.id.accommodationTitle);
        description = getView().findViewById(R.id.accommodationDescription);
        wifiIcon = getView().findViewById(R.id.wifi_icon);
        kitchenIcon = getView().findViewById(R.id.kitchen_icon);
        airConditionerIcon = getView().findViewById(R.id.air_conditioner_icon);
        parkingIcon = getView().findViewById(R.id.parking_icon);
        reservationFromDate = getView().findViewById(R.id.reservation_from_date);
        reservationToDate = getView().findViewById(R.id.reservation_to_date);
        reservationNumberOfPeople = getView().findViewById(R.id.number_of_people);

        price = getView().findViewById(R.id.price);
        guestCapacity = getView().findViewById(R.id.guest_capacity);
        averageRate = getView().findViewById(R.id.ratingBar);
        viewPager = view.findViewById(R.id.viewPager);
        totalPrice = view.findViewById(R.id.total_price_text);
        reviewCount = getView().findViewById(R.id.reviewCount);
        mapView = view.findViewById(R.id.mapContainer);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        calendarView = getView().findViewById(R.id.calendarView);

        setupCalendarView();

        // Image Slider setup
        String photosString = accommodation.getPhotos();
        List<String> imageUrls = new ArrayList<>(Arrays.asList(photosString.split(";")));

        ImageSliderAdapter adapter = new ImageSliderAdapter(getContext(), imageUrls);
        viewPager.setAdapter(adapter);

        // Set TextViews with Accommodation details
        name.setText(accommodation.getName());
        averageRate.setRating((float) calculateAverageRate(accommodation.getRatings()));
        description.setText(accommodation.getDescription());
        setFacilityIcon(wifiIcon, accommodation.isWifi());
        setFacilityIcon(kitchenIcon, accommodation.isKitchen());
        setFacilityIcon(airConditionerIcon, accommodation.isAirConditioner());
        setFacilityIcon(parkingIcon, accommodation.isParking());
        price.setText(String.format("$%s / night", accommodation.getPrice()));
        guestCapacity.setText(String.format("Capacity: %s - %s people", accommodation.getMinGuest(), accommodation.getMaxGuest()));
        reviewCount.setText(String.format("(%s Reviews)", Integer.toString(accommodation.getRatings().size())));
        // Button actions
        Button changeDatesButton = getView().findViewById(R.id.change_dates_button);
        changeDatesButton.setOnClickListener(v -> openDatePicker());

        reserve = getView().findViewById(R.id.reserve_button);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if(sharedPreferences.getString("search_start_date", "") != "" && sharedPreferences.getString("search_end_date", "") != "" && sharedPreferences.getInt("searched_people_number", 0) != 0){
            reserveFrom = sharedPreferences.getString("search_start_date", "");
            reserveTo = sharedPreferences.getString("search_end_date", "");
            reservationToDate.setText(String.format("To: %s",  reserveTo));
            reservationFromDate.setText(String.format("From: %s",  reserveFrom));
            totalPrice.setText(String.format("Total price (%d nights): $%d", calculateNights(reserveFrom, reserveTo), calculatePrice(reserveFrom, reserveTo, sharedPreferences.getInt("searched_people_number", 0))));
            reserve.setVisibility(View.VISIBLE);
            reservationNumberOfPeople.setText("Number of people:" + Integer.toString(sharedPreferences.getInt("searched_people_number", 0)));
            numberOfPeople = sharedPreferences.getInt("searched_people_number", 0);
            editor.putString("reservation_start_date", sharedPreferences.getString("search_start_date", ""));
            editor.putString("reservation_end_date", sharedPreferences.getString("search_end_date", ""));
            editor.apply();
        }else{
            reservationToDate.setText("");
            reservationFromDate.setText("");
            totalPrice.setText("");
            reserve.setVisibility(View.GONE);
            reservationNumberOfPeople.setText("");
        }
        reserve.setOnClickListener(v -> reserveAccommodation());

        Button setPeopleButton = getView().findViewById(R.id.set_people_button);
        setPeopleButton.setOnClickListener(v -> openPeoplePicker());

        // Back press handler
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Fragment fragment = new GuestHomeFragment();
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.guest_content, fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
    }

    private void setFacilityIcon(ImageView iconView, boolean available) {
        int iconResource = available ? R.drawable.ic_checkmark : R.drawable.ic_cross;
        iconView.setImageResource(iconResource);
    }

    private void openDatePicker() {
        List<Long> availableDatesMillis = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        Calendar calendar = Calendar.getInstance();

        for (String dateString : accommodation.getAvailability()) {
            try {
                Date date = sdf.parse(dateString);
                if (date != null) {
                    availableDatesMillis.add(date.getTime());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        long minDate = Collections.min(availableDatesMillis);
        long maxDate = Collections.max(availableDatesMillis);

        MaterialDatePicker.Builder<Pair<Long, Long>> builder = MaterialDatePicker.Builder.dateRangePicker();
        builder.setSelection(new Pair<>(minDate, maxDate));
        builder.setCalendarConstraints(new CalendarConstraints.Builder()
                .setStart(minDate)
                .setEnd(maxDate)
                .build());

        MaterialDatePicker<Pair<Long, Long>> datePicker = builder.build();

        datePicker.addOnPositiveButtonClickListener(selection -> {
            long startDateMillis = selection.first;
            long endDateMillis = selection.second;

            Date startDate = new Date(startDateMillis);
            Date endDate = new Date(endDateMillis);

            SimpleDateFormat outputFormat = new SimpleDateFormat("MM/dd/yyyy");
            String startDateString = outputFormat.format(startDate);
            String endDateString = outputFormat.format(endDate);

            if (startDate.equals(endDate)) {
                Toast.makeText(getContext(), "Start and End date cannot be the same.", Toast.LENGTH_SHORT).show();
                return;
            }
            Calendar today = Calendar.getInstance();
            today.set(Calendar.HOUR_OF_DAY, 0);
            today.set(Calendar.MINUTE, 0);
            today.set(Calendar.SECOND, 0);
            today.set(Calendar.MILLISECOND, 0);

            Calendar selectedStartDate = Calendar.getInstance();
            selectedStartDate.setTimeInMillis(selection.first);

            Calendar selectedEndDate = Calendar.getInstance();
            selectedEndDate.setTimeInMillis(selection.second);

            if (selectedStartDate.before(today) || selectedEndDate.before(today)) {
                Toast.makeText(getContext(), "Selected dates cannot be in the past.", Toast.LENGTH_SHORT).show();
                return;
            }
            if (isDateRangeAvailable(startDateString, endDateString)) {
                Toast.makeText(getContext(), "Selected Date Range: " + startDateString + " to " + endDateString, Toast.LENGTH_SHORT).show();
                reservationToDate.setText(String.format("To: %s",  endDateString));
                reservationFromDate.setText(String.format("From: %s",  startDateString));
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("preferences", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("reservation_start_date", startDateString);
                editor.putString("reservation_end_date", endDateString);
                reserveFrom = startDateString;
                reserveTo = endDateString;
                editor.apply();
                if(numberOfPeople != -1){
                    totalPrice.setText(String.format("Total price (%d nights): $%d", calculateNights(startDateString, endDateString), calculatePrice(startDateString, endDateString, numberOfPeople)));
                    reserve.setVisibility(View.VISIBLE);
                }


            } else {
                Toast.makeText(getContext(), "Selected date range is not available", Toast.LENGTH_SHORT).show();
            }
        });

        datePicker.show(getParentFragmentManager(), "DATE_PICKER");
    }

    private boolean isDateRangeAvailable(String startDate, String endDate) {
        List<String> availability = accommodation.getAvailability();
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

        try {
            Date start = sdf.parse(startDate);
            Date end = sdf.parse(endDate);

            if (start == null || end == null) {
                return false;
            }

            calendar.setTime(start);
            while (calendar.getTime().before(end) || calendar.getTime().equals(end)) {
                String dateToCheck = sdf.format(calendar.getTime());
                if (!availability.contains(dateToCheck)) {
                    return false;
                }
                calendar.add(Calendar.DAY_OF_MONTH, 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    private void reserveAccommodation() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Reservation reservation = new Reservation(
                accommodation.getId().intValue(),
                sharedPreferences.getString("reservation_start_date", "11/25/2024"),
                sharedPreferences.getString("reservation_end_date", "11/27/2024"),
                ReservationRequestStatus.PENDING,
                this.myId.intValue(),
                calculatePrice(reserveFrom, reserveTo, numberOfPeople),
                numberOfPeople);

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setMessage("Are you sure you want to book the " + accommodation.getName() + " from 26.11. to 30.11?")
                .setCancelable(false)
                .setPositiveButton("Yes", (dialog, id) -> {
                    GuestHomeFragment fragment = new GuestHomeFragment();
                    ((GuestMainActivity) requireContext()).loadFragment(fragment);
                    Call<Reservation> call = ServiceUtils.reservationService.addReservation(reservation);
                    call.enqueue(new Callback<Reservation>() {
                        @Override
                        public void onResponse(Call<Reservation> call, Response<Reservation> response) {
                            if (!response.isSuccessful()) return;
                            Log.d("Success", "Successfully added reservation");
                           // Toast.makeText(getContext(), "Successfully added reservation", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(Call<Reservation> call, Throwable t) {
                            Log.d("FAIL", t.getMessage());
                        }
                    });
                })
                .setNegativeButton("No", (dialog, id) -> dialog.dismiss());
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Prikaz lokacije na mapi
        // Ovdje je potrebno dodati kod za prikazivanje lokacije smeštaja
        // Učitaj lokaciju smeštaja i postavi marker
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    private double calculateAverageRate(List<Rating> ratings) {
        if (ratings == null || ratings.isEmpty()) {
            return 0;
        }

        double sum = 0;
        for (Rating rating : ratings) {
            sum += rating.getRating();
        }
        return sum / ratings.size();
    }

    private void setupCalendarView() {
        CalendarView calendarView = getView().findViewById(R.id.calendarView);

        List<Long> availableDatesMillis = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        List<String> availableDates = accommodation.getAvailability();

        for (String dateString : accommodation.getAvailability()) {
            try {
                Date date = sdf.parse(dateString);
                if (date != null) {
                    availableDatesMillis.add(date.getTime());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (!availableDatesMillis.isEmpty()) {
            calendarView.setMinDate(Collections.min(availableDatesMillis));
            calendarView.setMaxDate(Collections.max(availableDatesMillis));
        }

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                Calendar selectedDate = Calendar.getInstance();
                selectedDate.set(year, month, dayOfMonth);
                String selectedDateString = sdf.format(selectedDate.getTime());

                if (availableDates.contains(selectedDateString)) {
                    Toast.makeText(getContext(), "Date available!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Date NOT available!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private int calculateNights(String startDateString, String endDateString) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
            Date startDate = sdf.parse(startDateString);
            Date endDate = sdf.parse(endDateString);

            long differenceInMillis = endDate.getTime() - startDate.getTime();
            int nights = (int) (differenceInMillis / (1000 * 60 * 60 * 24));

            return nights;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    private int calculatePrice(String startDateString, String endDateString, int numberOfPeople) {
        int pricePerNight = accommodation.getPrice();
        int nights = calculateNights(startDateString, endDateString);

        return pricePerNight * nights * numberOfPeople;
    }

    private void openPeoplePicker() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_number_picker, null);

        NumberPicker numberPicker = dialogView.findViewById(R.id.number_picker);
        numberPicker.setMinValue(accommodation.getMinGuest());
        numberPicker.setMaxValue(accommodation.getMaxGuest());
        numberPicker.setValue(numberOfPeople != -1 ? numberOfPeople : accommodation.getMinGuest());

        builder.setView(dialogView)
                .setTitle("Select number of people")
                .setPositiveButton("OK", (dialog, id) -> {
                    numberOfPeople = numberPicker.getValue();
                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences("preferences", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    reservationNumberOfPeople.setText("Number of people: " + String.valueOf(numberOfPeople));
                    if ( reserveFrom != "" && reserveTo != "") {
                        totalPrice.setText(String.format("Total price (%d nights): $%d", calculateNights(reserveFrom,reserveTo), calculatePrice(reserveFrom, reserveTo, numberOfPeople)));
                        reserve.setVisibility(View.VISIBLE);
                    }
                })
                .setNegativeButton("Cancel", (dialog, id) -> dialog.dismiss());

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

}
