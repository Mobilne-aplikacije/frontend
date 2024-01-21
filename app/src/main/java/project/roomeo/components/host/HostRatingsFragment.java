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
import android.widget.TextView;

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

public class HostRatingsFragment extends Fragment {
    private RecyclerView recyclerView;
    private HostRatingAdapter ratingAdapter;
    private Long myId;
    public TextView averageRate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_host_ratings, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("preferences", Context.MODE_PRIVATE);
        myId = sharedPreferences.getLong("pref_id", 0L);
        Log.e("PROVERA ID", "provera id: " + myId);

        getHostRatingsList();

        return view;
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        averageRate = getView().findViewById(R.id.averageRate);

        Call<Double> call = ServiceUtils.hostService.getHostAverageRate(myId.toString());
        call.enqueue(new Callback<Double>() {
            @Override
            public void onResponse(Call<Double> call, Response<Double> response) {
                if (response.isSuccessful()) {
                    Double average = response.body();
                    if (average != null) {
                        averageRate.setText(average.toString());
                        Log.i("prosekk",  String.valueOf(average));

                    }
                } else {
                    onFailure(call, new Throwable("API call failed with status code: " + response.code()));
                }
            }

            @Override
            public void onFailure(Call<Double> call, Throwable t) {
                Log.e("Rating", "API call failed: " + t.getMessage());
            }
        });
    }


    private void getHostRatingsList() {

        Call<List<Rating>> call = ServiceUtils.ratingService.getAllHostRatings(myId.toString());

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
                        ratingAdapter = new HostRatingAdapter(acceptedRatings);
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

}