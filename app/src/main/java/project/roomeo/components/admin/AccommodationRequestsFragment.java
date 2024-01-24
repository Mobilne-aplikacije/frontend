package project.roomeo.components.admin;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import project.roomeo.R;
import project.roomeo.models.Accommodation;
import project.roomeo.service.ServiceUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccommodationRequestsFragment extends Fragment {
    private RecyclerView recyclerView;
    private AccommodationAdapter accommodationAdapter;

    FragmentActivity activity = getActivity();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_accommodation_requests, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);

        // Postavljanje layout manager-a (npr. LinearLayoutManager)
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Kreiranje adapter-a i povezivanje sa RecyclerView-om
        getAccommodationList(); // Inicijalizacija liste

        return view;
    }

    private void getAccommodationList() {

        Call<List<Accommodation>> call = ServiceUtils.adminService.getAllPendingAccommodations();

        call.enqueue(new Callback<List<Accommodation>>() {
            @Override
            public void onResponse(Call<List<Accommodation>> call, Response<List<Accommodation>> response) {
                if (response.isSuccessful()) {
                    List<Accommodation> list = response.body();
                    if (list != null) {
//                        Log.i("ADS", list.get(0).getType().toString());
                        accommodationAdapter = new AccommodationAdapter(list, requireContext());
//                        accommodationAdapter = new AccommodationAdapter(list, activity.getSupportFragmentManager());
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
