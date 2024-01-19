package project.roomeo.components.host;

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

import java.util.ArrayList;
import java.util.List;

import project.roomeo.R;
import project.roomeo.components.admin.RatingRequestAdapter;
import project.roomeo.models.Rating;
import project.roomeo.models.enums.RatingStatus;
import project.roomeo.service.ServiceUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccommodationRatingsFragment extends Fragment {

    private RecyclerView recyclerView;
    private AccommodationRatingAdapter ratingAdapter;
    private Long myId;
    private Long accommodationId;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_accommodation_ratings, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("preferences", Context.MODE_PRIVATE);
        myId = sharedPreferences.getLong("pref_id", 0L);
        Log.e("PROVERA ID", "provera id: " + myId);

        return view;
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getAccommodationRatingsList(accommodationId);
    }

    private void getAccommodationRatingsList(Long accommodationId) {

        Call<List<Rating>> call = ServiceUtils.ratingService.getAllAccommodationRatings(accommodationId.toString());

        call.enqueue(new Callback<List<Rating>>() {
            @Override
            public void onResponse(Call<List<Rating>> call, Response<List<Rating>> response) {
                if (response.isSuccessful()) {
                    List<Rating> list = response.body();
                    if (list != null) {
                        List<Rating> acceptedRatings = new ArrayList<>();
                        for (Rating r : list) {
                            if (r.getStatus() == RatingStatus.ACCEPTED) {
                                acceptedRatings.add(r);
                            }
                        }
                        ratingAdapter = new AccommodationRatingAdapter(acceptedRatings);
                        recyclerView.setAdapter(ratingAdapter);

                    }
                } else {
                    onFailure(call, new Throwable("API call failed with status code: " + response.code()));
                }
            }

            @Override
            public void onFailure(Call<List<Rating>> call, Throwable t) {
                Log.e("HostRatingsFragment", "API call failed: " + t.getMessage());
            }
        });
    }

    public AccommodationRatingsFragment(Long accommodationId) {
        this.accommodationId = accommodationId;
}

}