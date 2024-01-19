package project.roomeo.components.admin;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import project.roomeo.R;
import project.roomeo.models.Accommodation;
import project.roomeo.models.Rating;
import project.roomeo.service.ServiceUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RatingRequestsFragment extends Fragment {
    private RecyclerView recyclerView;
    private RatingAdapter ratingAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rating_requests, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);

        // Postavljanje layout manager-a (npr. LinearLayoutManager)
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Kreiranje adapter-a i povezivanje sa RecyclerView-om
        getRatingRequestsList(); // Inicijalizacija liste

        return view;
    }

    private void getRatingRequestsList() {

        Call<List<Rating>> call = ServiceUtils.ratingService.getAll();

        call.enqueue(new Callback<List<Rating>>() {
            @Override
            public void onResponse(Call<List<Rating>> call, Response<List<Rating>> response) {
                if (response.isSuccessful()) {
                    List<Rating> list = response.body();
                    if (list != null) {
                        ratingAdapter = new RatingAdapter(list);
                        recyclerView.setAdapter(ratingAdapter);

                    }
                } else {
                    onFailure(call, new Throwable("API call failed with status code: " + response.code()));
                }
            }

            @Override
            public void onFailure(Call<List<Rating>> call, Throwable t) {
                Log.e("RatingRequestsFragment", "API call failed: " + t.getMessage());
            }
        });
    }
}