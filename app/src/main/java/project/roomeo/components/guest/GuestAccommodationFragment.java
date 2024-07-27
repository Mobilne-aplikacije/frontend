package project.roomeo.components.guest;

import android.app.DatePickerDialog;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textview.MaterialTextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import project.roomeo.R;
import project.roomeo.components.ImageSliderAdapter;
import project.roomeo.components.host.AccommodationRatingsFragment;
import project.roomeo.components.host.HostAccommodationFragment;
import project.roomeo.components.host.HostAccommodationsFragment;
import project.roomeo.components.host.HostMainActivity;
import project.roomeo.components.host.HostRatingsFragment;
import project.roomeo.models.Accommodation;
import project.roomeo.models.EcoFriendlyAmenity;
import project.roomeo.models.Rating;
import project.roomeo.models.Reservation;
import project.roomeo.models.enums.RatingType;
import project.roomeo.models.enums.ReservationRequestStatus;
import project.roomeo.service.ServiceUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GuestAccommodationFragment extends Fragment {

    private Accommodation accommodation;
    public TextView name;
    public TextView description;
    public TextView location;
    public TextView wifi;
    public TextView type;
    public TextView kitchen;
    public TextView airConditioner;
    public TextView parking;
    public TextView payment;
    public TextView pricee;
    public TextView minGuest;
    public TextView maxGuest;
    private boolean pending;
    public TextView averageRate;
    public Long myId;
    public ImageView placeImage;
    private ViewPager2 viewPager;

    public GuestAccommodationFragment() {
        this.pending = false;
    }

    public GuestAccommodationFragment(boolean pending, Long myId) {
        this.pending = pending;
        this.myId = myId;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (pending) {
            return inflater.inflate(R.layout.fragment_host_pending_accommodation, container, false);
        } else {
            View view = inflater.inflate(R.layout.fragment_guest_accommodation, container, false);

            return view;
        }
    }

    public void setAccommodationRequest(Accommodation request) {
        this.accommodation = request;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        name = getView().findViewById(R.id.name);
        description = getView().findViewById(R.id.description);
        location = getView().findViewById(R.id.location);
        wifi = getView().findViewById(R.id.wifi);
        kitchen = getView().findViewById(R.id.kitchen);
        airConditioner = getView().findViewById(R.id.airConditioner);
        parking = getView().findViewById(R.id.parking);
        pricee = getView().findViewById(R.id.price);
        minGuest = getView().findViewById(R.id.minGuest);
        averageRate = getView().findViewById(R.id.averageRate);
        placeImage = getView().findViewById(R.id.placeImage);
        viewPager = view.findViewById(R.id.viewPager);

        String photosString = accommodation.getPhotos();
        List<String> imageUrls = new ArrayList<>(Arrays.asList(photosString.split(";")));

        ImageSliderAdapter adapter = new ImageSliderAdapter(getContext(), imageUrls);
        viewPager.setAdapter(adapter);

        name.setText(accommodation.getName());
        averageRate.setText(String.valueOf(accommodation.getRate()));
        description.setText(accommodation.getDescription());
        location.setText(accommodation.getLocation());
        if (accommodation.isWifi()) {
            wifi.setText("Yes");
        } else {
            wifi.setText("No");
        }
        if (accommodation.isKitchen()) {
            kitchen.setText("Yes");
        } else {
            kitchen.setText("No");
        }
        if (accommodation.isAirConditioner()) {
            airConditioner.setText("Yes");
        } else {
            airConditioner.setText("No");
        }
        if (accommodation.isParking()) {
            parking.setText("Yes");
        } else {
            parking.setText("No");
        }
        pricee.setText(String.valueOf(accommodation.getPrice()*8) + "$");
        minGuest.setText("Number of guests: " + String.valueOf(accommodation.getMinGuest()) + "-" + accommodation.getMaxGuest());

        LinearLayout ecoLayout = getView().findViewById(R.id.ecoLayout);
        ecoLayout.removeAllViews();
        for (EcoFriendlyAmenity ecoAmenity : accommodation.getEcoFriendlyAmenities()) {
            MaterialTextView textView = new MaterialTextView(requireContext());
            textView.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            textView.setText(addSpacesToCamelCase(ecoAmenity.getName().toString()));
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            textView.setPadding((int) getResources().getDimension(R.dimen.padding_8dp), 0, 0, 0);

            ecoLayout.addView(textView);
        }

        Button changeDatesButton = getView().findViewById(R.id.change);
        changeDatesButton.setOnClickListener(v -> openDatePicker());

        Button reserve = getView().findViewById(R.id.reserve);
        reserve.setOnClickListener(v -> {
            Reservation reservation = new Reservation(accommodation.getId().intValue(), "07/25/2024", "07/27/2024", ReservationRequestStatus.PENDING, this.myId.intValue(), accommodation.getPrice());

            Call<Reservation> call2 = ServiceUtils.reservationService.addReservation(reservation);

            call2.enqueue(new Callback<Reservation>() {
                @Override
                public void onResponse(Call<Reservation> call, Response<Reservation> response) {
                    if (!response.isSuccessful()) return;
                    Log.d("Success", "Successfully added reservation");
                }

                @Override
                public void onFailure(Call<Reservation> call, Throwable t) {
                    Log.d("FAIL", t.getMessage());
                }
            });

            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
            builder.setMessage("Are you sure you want to book the " + accommodation.getName() + " from 26.11. to 30.11?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", (dialog, id) -> {
                        GuestHomeFragment fragment = new GuestHomeFragment();
                        ((GuestMainActivity) v.getContext()).loadFragment(fragment);
                    }).setNegativeButton("No", (dialog, id) -> {
                        dialog.dismiss();
                    });
            ;

            AlertDialog alert = builder.create();
            alert.show();
        });

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


    private void openDatePicker() {
        // Konvertujte dostupne datume u listu Long za upotrebu sa MaterialDatePicker
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

        // Kreirajte interval za dostupne datume
        long minDate = Collections.min(availableDatesMillis);
        long maxDate = Collections.max(availableDatesMillis);

        // Kreirajte MaterialDatePicker za opseg datuma
        MaterialDatePicker.Builder<androidx.core.util.Pair<Long, Long>> builder = MaterialDatePicker.Builder.dateRangePicker();
        builder.setSelection(new Pair<>(minDate, maxDate));
        builder.setCalendarConstraints(new CalendarConstraints.Builder()
                .setStart(minDate)
                .setEnd(maxDate)
                .build());

        MaterialDatePicker<androidx.core.util.Pair<Long, Long>> datePicker = builder.build();

        datePicker.addOnPositiveButtonClickListener(selection -> {
            long startDateMillis = selection.first;
            long endDateMillis = selection.second;

            Date startDate = new Date(startDateMillis);
            Date endDate = new Date(endDateMillis);

            SimpleDateFormat outputFormat = new SimpleDateFormat("MM/dd/yyyy");
            String startDateString = outputFormat.format(startDate);
            String endDateString = outputFormat.format(endDate);

            if (isDateRangeAvailable(startDateString, endDateString)) {
                // Handle the selected date range
                Toast.makeText(getContext(), "Selected Date Range: " + startDateString + " to " + endDateString, Toast.LENGTH_SHORT).show();
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


    private String addSpacesToCamelCase(String text) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (Character.isUpperCase(c) && i > 0) {
                builder.append(" ");
            }
            builder.append(c);
        }
        return builder.toString();
    }

}

