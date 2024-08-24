package project.roomeo.components.guest;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.slider.RangeSlider;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import project.roomeo.R;
import project.roomeo.components.guest.AccommodationAdapter;
import project.roomeo.components.host.HostAccommodationsFragment;
import project.roomeo.components.host.HostMainActivity;
import project.roomeo.models.Accommodation;
import project.roomeo.models.enums.AccommodationRequestStatus;
import project.roomeo.service.ServiceUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GuestHomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private AccommodationAdapter accommodationAdapter;
    private Long myId;
    private AutoCompleteTextView searchLocationEditText;
    private EditText numberOfGuestsEditText;
    private EditText datePickerEditText;
    private TextView searchButton;
    private TextView sortButton;
    private List<Accommodation> currentAccommodationList = new ArrayList<>();
    private SharedPreferences sharedPreferences;


    public GuestHomeFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_guest_home, container, false);

        TextView dateRangeTextView = view.findViewById(R.id.datePickerEditText);

        MaterialDatePicker<Pair<Long, Long>> picker = MaterialDatePicker.Builder.dateRangePicker().build();
        picker.addOnPositiveButtonClickListener(selection -> {

            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
            String startDate = dateFormat.format(selection.first);
            String endDate = dateFormat.format(selection.second);
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
            String dateRange = startDate + " - " + endDate;
            dateRangeTextView.setText(dateRange);
        });

        dateRangeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picker.show(getParentFragmentManager(), picker.toString());
            }
        });
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("preferences", Context.MODE_PRIVATE);
        this.sharedPreferences = sharedPreferences;
        String myEmail = sharedPreferences.getString("pref_email", "");
        myId = sharedPreferences.getLong("pref_id", 0L);
        Log.e("PROVERA ID", "provera id: " + myId);


        recyclerView = view.findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        searchLocationEditText = view.findViewById(R.id.searchLocationEditText);
        numberOfGuestsEditText = view.findViewById(R.id.numberOfGuestsEditText);
        searchButton = view.findViewById(R.id.searchButton);
        datePickerEditText = view.findViewById(R.id.datePickerEditText);
        sortButton = view.findViewById(R.id.sortText);

        setupAutoCompleteTextView();

        searchButton.setOnClickListener(v -> {
            String location = searchLocationEditText.getText().toString();
            String numberOfGuestsString = numberOfGuestsEditText.getText().toString();
            String dateRange = datePickerEditText.getText().toString();


            int numberOfGuests = 0;
            if (!numberOfGuestsString.isEmpty()) {
                numberOfGuests = Integer.parseInt(numberOfGuestsString);
            }
            getSearchedAccommodations(location, numberOfGuests, dateRange);

        });

        sortButton.setOnClickListener(v -> showSortOptions());

        TextView filterButton = view.findViewById(R.id.filterText);
        filterButton.setOnClickListener(v -> showFilterOptions());


        getAccommodationList();


        return view;
    }


    private void setupAutoCompleteTextView() {
        String[] locations = {"Novi Sad", "Beograd", "Nis"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, locations);
        searchLocationEditText.setAdapter(adapter);
        searchLocationEditText.setThreshold(1);
    }

    private void getAccommodationList() {
        Call<List<Accommodation>> call = ServiceUtils.guestService.getAllAccommodations();

        call.enqueue(new Callback<List<Accommodation>>() {
            @Override
            public void onResponse(Call<List<Accommodation>> call, Response<List<Accommodation>> response) {
                if (response.isSuccessful()) {
                    List<Accommodation> list = response.body();
                    if (list != null) {
                        List<Accommodation> listAccepted = new ArrayList<Accommodation>();
                        for (int i = 0; i < list.size(); i++) {
                            if (list.get(i).getStatus() == AccommodationRequestStatus.ACCEPTED) {
                                listAccepted.add(list.get(i));
                            }
                        }
                        currentAccommodationList = listAccepted;
                        accommodationAdapter = new AccommodationAdapter(listAccepted, false, Long.valueOf(myId), requireContext());
                        recyclerView.setAdapter(accommodationAdapter);
                    }
                } else {
                    onFailure(call, new Throwable("API call failed with status code: " + response.code()));
                }
            }

            @Override
            public void onFailure(Call<List<Accommodation>> call, Throwable t) {
                Log.e("AccommodationRequestsFragment", "API call failed: " + t.getMessage());
            }
        });
    }

    private void getSearchedAccommodations(String location, int numberOfGuests, String dateRange) {

        String startDate = "";
        String endDate = "";
        if (!dateRange.isEmpty()) {
            String[] dates = dateRange.split(" - ");
            startDate = dates[0];
            endDate = dates[1];
        }

        Call<List<Accommodation>> call = ServiceUtils.guestService.getSearchedAccommodations(location, numberOfGuests, startDate, endDate);
        String finalStartDate = startDate;
        String finalEndDate = endDate;
        call.enqueue(new Callback<List<Accommodation>>() {
            @Override
            public void onResponse(Call<List<Accommodation>> call, Response<List<Accommodation>> response) {
                if (response.isSuccessful()) {
                    List<Accommodation> list = response.body();
                    if (list != null) {
                        // Filter based on guest number
                        List<Accommodation> filteredList = new ArrayList<>();
                        for (Accommodation accommodation : list) {
                            if (accommodation.getMinGuest() <= numberOfGuests && accommodation.getMaxGuest() >= numberOfGuests) {
                                filteredList.add(accommodation);
                            }
                        }
                        currentAccommodationList = filteredList;
                        accommodationAdapter = new AccommodationAdapter(filteredList, false, Long.valueOf(myId), requireContext());
                        recyclerView.setAdapter(accommodationAdapter);
                    }
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("search_start_date", finalStartDate);
                    editor.putString("search_end_date", finalEndDate);
                    editor.putInt("searched_people_number", numberOfGuests);
                    editor.apply();
                } else {
                    onFailure(call, new Throwable("API call failed with status code: " + response.code()));
                }
            }

            @Override
            public void onFailure(Call<List<Accommodation>> call, Throwable t) {
                Log.e("AccommodationRequestsFragment", "API call failed: " + t.getMessage());
            }
        });
    }



    private void showSortOptions() {
        String[] sortOptions = {"Price", "Rating"};

        new AlertDialog.Builder(getContext())
                .setTitle("Sort By")
                .setItems(sortOptions, (dialog, which) -> {
                    if (which == 0) {
                        sortAccommodationsByPrice();
                    } else if (which == 1) {
                        sortAccommodationsByRating();
                    }
                })
                .show();
    }

    private void sortAccommodationsByPrice() {
        if (currentAccommodationList != null) {
            currentAccommodationList.sort(Comparator.comparingDouble(Accommodation::getPrice));
            accommodationAdapter.notifyDataSetChanged();
        }
    }

    private void sortAccommodationsByRating() {
        if (currentAccommodationList != null) {
            currentAccommodationList.sort((a1, a2) -> Double.compare(a2.getRate(), a1.getRate()));
            accommodationAdapter.notifyDataSetChanged();
        }
    }

    private void showFilterOptions() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Filter Accommodations");

        View filterView = getLayoutInflater().inflate(R.layout.dialog_filter, null);

        CheckBox wifiCheckbox = filterView.findViewById(R.id.checkbox_wifi);
        CheckBox kitchenCheckbox = filterView.findViewById(R.id.checkbox_kitchen);
        CheckBox acCheckbox = filterView.findViewById(R.id.checkbox_ac);
        CheckBox parkingCheckbox = filterView.findViewById(R.id.checkbox_parking);
        RangeSlider priceRangeSeekBar = filterView.findViewById(R.id.price_range_seekbar);

        wifiCheckbox.setChecked(sharedPreferences.getBoolean("wifi", false));
        kitchenCheckbox.setChecked(sharedPreferences.getBoolean("kitchen", false));
        acCheckbox.setChecked(sharedPreferences.getBoolean("ac", false));
        parkingCheckbox.setChecked(sharedPreferences.getBoolean("parking", false));

        int minPrice = Integer.MAX_VALUE;
        int maxPrice = Integer.MIN_VALUE;
        for (Accommodation accommodation : currentAccommodationList) {
            int price = accommodation.getPrice();
            if (price < minPrice) {
                minPrice = price;
            }
            if (price > maxPrice) {
                maxPrice = price;
            }
        }

        // Set the range slider's min and max values
        Set<String> selectedTypes = sharedPreferences.getStringSet("selectedTypes", new HashSet<>());
        priceRangeSeekBar.setValueFrom(minPrice);
        priceRangeSeekBar.setValueTo(maxPrice);
        priceRangeSeekBar.setValues((float) minPrice, (float) maxPrice);

        // Accommodation types horizontal checkmark list
        LinearLayout typesLayout = filterView.findViewById(R.id.typesLayout);
        String[] accommodationTypes = getResources().getStringArray(R.array.accommodation_types);
        List<CheckBox> typeCheckBoxes = new ArrayList<>();

        for (String type : accommodationTypes) {
            CheckBox checkBox = new CheckBox(getContext());
            checkBox.setText(type);
            if (selectedTypes.contains(type)) {
                checkBox.setChecked(true);
            }
            typesLayout.addView(checkBox);
            typeCheckBoxes.add(checkBox);
        }

        builder.setView(filterView);

        builder.setPositiveButton("Apply", (dialog, which) -> {
            boolean wifi = wifiCheckbox.isChecked();
            boolean kitchen = kitchenCheckbox.isChecked();
            boolean ac = acCheckbox.isChecked();
            boolean parking = parkingCheckbox.isChecked();
            List<Float> values = priceRangeSeekBar.getValues();
            int selectedMinPrice = values.get(0).intValue();
            int selectedMaxPrice = values.get(1).intValue();

            // Get selected types
            List<String> selectedTypes1 = new ArrayList<>();
            for (CheckBox checkBox : typeCheckBoxes) {
                if (checkBox.isChecked()) {
                    selectedTypes1.add(checkBox.getText().toString());
                }
            }

            applyFilters(wifi, kitchen, ac, parking, selectedTypes1, selectedMinPrice, selectedMaxPrice);
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        builder.show();
    }

    private void applyFilters(boolean wifi, boolean kitchen, boolean ac, boolean parking, List<String> selectedTypes, int minPrice, int maxPrice) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("wifi", wifi);
        editor.putBoolean("kitchen", kitchen);
        editor.putBoolean("ac", ac);
        editor.putBoolean("parking", parking);
        editor.putInt("minPrice", minPrice);
        editor.putInt("maxPrice", maxPrice);
        editor.putStringSet("selectedTypes", new HashSet<>(selectedTypes));
        editor.apply(); // Osiguraj da se promene pravilno sačuvaju

        List<Accommodation> filteredList = new ArrayList<>();

        for (Accommodation accommodation : currentAccommodationList) {
            if ((wifi && !accommodation.isWifi()) ||
                    (kitchen && !accommodation.isKitchen()) ||
                    (ac && !accommodation.isAirConditioner()) ||
                    (parking && !accommodation.isParking())) {
                continue;
            }

            // Filter based on selected types
            if (!selectedTypes.isEmpty() && !selectedTypes.contains(accommodation.getType().toString())) {
                continue;
            }

            if (accommodation.getPrice() < minPrice || accommodation.getPrice() > maxPrice) {
                continue;
            }

            filteredList.add(accommodation);
        }

        accommodationAdapter = new AccommodationAdapter(filteredList, false, Long.valueOf(myId), requireContext());
        recyclerView.setAdapter(accommodationAdapter);
    }



}
