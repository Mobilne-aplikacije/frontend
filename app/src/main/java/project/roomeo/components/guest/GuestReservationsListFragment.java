package project.roomeo.components.guest;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.datepicker.MaterialDatePicker;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import project.roomeo.R;
import project.roomeo.models.Reservation;
import project.roomeo.models.enums.ReservationRequestStatus;
import project.roomeo.service.ServiceUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GuestReservationsListFragment extends Fragment {

    private RecyclerView recyclerView;
    private ReservationAdapter reservationAdapter;
    private Long myId;
    private List<Reservation> originalReservations;
    private AutoCompleteTextView searchAccommodationEditText;
    private EditText datePickerEditText;
    private TextView clearButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_guest_reservations_list, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        searchAccommodationEditText = view.findViewById(R.id.searchAccommodationEditText);
        datePickerEditText = view.findViewById(R.id.datePickerEditText);
        clearButton = view.findViewById(R.id.clearButton);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("preferences", Context.MODE_PRIVATE);
        String myEmail = sharedPreferences.getString("pref_email", "");
        myId = sharedPreferences.getLong("pref_id", 0L);
        Log.e("PROVERA ID", "provera id: " + myId);

        getReservationList();
        setupSearch();
        setupDatePicker();
        setupClearButton();

        return view;
    }

    private void getReservationList() {
        Call<List<Reservation>> call = ServiceUtils.reservationService.getGuestReservationAndAccommodationName(this.myId);

        call.enqueue(new Callback<List<Reservation>>() {
            @Override
            public void onResponse(Call<List<Reservation>> call, Response<List<Reservation>> response) {
                if (response.isSuccessful()) {
                    List<Reservation> list = response.body();
                    if (list != null) {
                        LocalDate today = LocalDate.now();
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

                        List<Reservation> listConfirmed = list.stream()
                                .filter(reservation -> reservation.getStatus() == ReservationRequestStatus.ACCEPTED)
                                .collect(Collectors.toList());

                        reservationAdapter = new ReservationAdapter(listConfirmed);
                        recyclerView.setAdapter(reservationAdapter);
                        originalReservations = listConfirmed;

                    }
                } else {
                    onFailure(call, new Throwable("API call failed with status code: " + response.code()));
                }
            }

            @Override
            public void onFailure(Call<List<Reservation>> call, Throwable t) {
                Log.e("AccommodationRequestsFragment", "API call failed: " + t.getMessage());
            }
        });
    }
    private void setupClearButton() {
        clearButton.setOnClickListener(v -> {
            searchAccommodationEditText.setText("");
            datePickerEditText.setText("");
            updateAdapter(originalReservations);
        });
    }

    private void setupSearch() {
        searchAccommodationEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filter();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void setupDatePicker() {
        datePickerEditText.setOnClickListener(v -> {
            MaterialDatePicker.Builder<Pair<Long, Long>> builder = MaterialDatePicker.Builder.dateRangePicker();
            builder.setTitleText("Select Date Range");
            final MaterialDatePicker<Pair<Long, Long>> picker = builder.build();

            picker.addOnPositiveButtonClickListener(selection -> {
                Long startDateLong = selection.first;
                Long endDateLong = selection.second;

                String startDate = formatDate(startDateLong);
                String endDate = formatDate(endDateLong);

                datePickerEditText.setText(startDate + " - " + endDate);
                filter();
            });

            picker.show(getParentFragmentManager(), picker.toString());
        });
    }

    private String formatDate(Long dateInMillis) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        return formatter.format(LocalDate.ofEpochDay(dateInMillis / (24 * 60 * 60 * 1000)));
    }

    private void filter() {
        String searchText = searchAccommodationEditText.getText().toString().toLowerCase();
        String selectedDateRange = datePickerEditText.getText().toString();
        List<Reservation> filteredList = new ArrayList<>();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

        String[] dates = selectedDateRange.split(" - ");
        LocalDate startRange = null;
        LocalDate endRange = null;
        if(dates.length > 1){
            startRange = dates.length > 0 ? LocalDate.parse(dates[0], formatter) : null;
            endRange = dates.length > 1 ? LocalDate.parse(dates[1], formatter) : null;
            System.out.println("START DATE: " + startRange);
            System.out.println("END DATE: " + endRange);
        }

        for (Reservation reservation : originalReservations) {
            System.out.println("RESERVATION: \n" + reservation.getAccommodationName() + "\n");
            boolean matchesSearchText = true;
            boolean matchesDate = true;
            if(searchText != ""){
                matchesSearchText = reservation.getAccommodationName() != null
                        && reservation.getAccommodationName().toLowerCase().contains(searchText);
            }
            matchesDate = (startRange == null && endRange == null) ||
                    (startRange != null && endRange != null &&
                            !LocalDate.parse(reservation.getEndDate(), formatter).isBefore(startRange) &&
                            !LocalDate.parse(reservation.getStartDate(), formatter).isAfter(endRange));

            if (matchesSearchText && matchesDate) {
                filteredList.add(reservation);
            }
        }

        updateAdapter(filteredList);
    }

    private void updateAdapter(List<Reservation> list) {
        reservationAdapter.updateList(list);
    }
}
