package project.roomeo.components.guest;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

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
    private Button favorites;

    public GuestHomeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_guest_home, container, false);

        // Set up Date Range Picker functionality
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
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("preferences", Context.MODE_PRIVATE);
        String myEmail = sharedPreferences.getString("pref_email", "");
        myId = sharedPreferences.getLong("pref_id", 0L);
        Log.e("PROVERA ID", "provera id: " + myId);


        recyclerView = view.findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        getAccommodationList();
        favorites = view.findViewById(R.id.favorites);
        favorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GuestFavoritesFragment fragment = new GuestFavoritesFragment();
                ((GuestMainActivity) v.getContext()).loadFragment(fragment);
            }
        });


        return view;
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
                            if (list.get(i).getStatus()== AccommodationRequestStatus.ACCEPTED){
                                listAccepted.add(list.get(i));
                            }
                        }
                        accommodationAdapter = new AccommodationAdapter(listAccepted,false, Long.valueOf(myId), requireContext());
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
}
