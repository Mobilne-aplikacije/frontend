package project.roomeo.components.guest;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import project.roomeo.R;
import project.roomeo.components.host.AccommodationAdapter;
import project.roomeo.components.host.HostAccommodationsFragment;
import project.roomeo.components.host.HostMainActivity;
import project.roomeo.components.host.HostPendingAccommodationsFragment;
import project.roomeo.components.host.StepperFragment;
import project.roomeo.models.Accommodation;
import project.roomeo.models.Reservation;
import project.roomeo.models.enums.AccommodationRequestStatus;
import project.roomeo.models.enums.ReservationRequestStatus;
import project.roomeo.service.ServiceUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GuestReservationsFragment extends Fragment {
    private RecyclerView recyclerView;
    private ReservationAdapter reservationAdapter;
    private Long myId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_guest_reservations, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("preferences", Context.MODE_PRIVATE);
        String myEmail = sharedPreferences.getString("pref_email", "");
        myId = sharedPreferences.getLong("pref_id", 0L);
        Log.e("PROVERA ID", "provera id: " + myId);

        getReservationList();

        return view;
    }

    private void getReservationList() {
        Call<List<Reservation>> call = ServiceUtils.reservationService.getAllReservations();

        call.enqueue(new Callback<List<Reservation>>() {
            @Override
            public void onResponse(Call<List<Reservation>> call, Response<List<Reservation>> response) {
                if (response.isSuccessful()) {
                    List<Reservation> list = response.body();
                    if (list != null) {
                        List<Reservation> listAccepted = new ArrayList<Reservation>();
                        for (int i = 0; i < list.size(); i++) {
                            if (list.get(i).getStatus()== ReservationRequestStatus.ACCEPTED){
                                listAccepted.add(list.get(i));
                            }
                        }
                        reservationAdapter = new ReservationAdapter(listAccepted);
                        recyclerView.setAdapter(reservationAdapter);
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
}