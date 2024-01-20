package project.roomeo.components.guest;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
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
import project.roomeo.components.host.HostMainActivity;
import project.roomeo.components.host.HostRatingAdapter;
import project.roomeo.components.host.HostRatingsFragment;
import project.roomeo.components.host.StepperFragment;
import project.roomeo.models.Rating;
import project.roomeo.models.Report;
import project.roomeo.models.enums.RatingStatus;
import project.roomeo.service.ServiceUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GHostRatingsFragment extends Fragment {

    private RecyclerView recyclerView;
    private GHostRatingAdapter ratingAdapter;
    private Long myId;
    private Long hostId;
    private Button reportHost;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_g_host_ratings, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("preferences", Context.MODE_PRIVATE);
        myId = sharedPreferences.getLong("pref_id", 0L);
        Log.e("PROVERA ID", "provera id: " + myId);

        reportHost= view.findViewById(R.id.reportHost);
        reportHost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Report report = new Report(null,hostId.intValue(),null);
                Call<Report> call = ServiceUtils.reportService.addReport(report);
                call.enqueue(new Callback<Report>() {
                    @Override
                    public void onResponse(@NonNull Call<Report> call, @NonNull Response<Report> response) {
                        if (response.isSuccessful()) {
                            GHostRatingsFragment fragment = new GHostRatingsFragment(hostId);
                            ((GuestMainActivity) view.getContext()).loadFragment(fragment);
                        } else {
                            onFailure(call, new Throwable("API call failed with status code: " + response.code()));
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Report> call, @NonNull Throwable t) {
                        Log.e("Report", "API call failed: " + t.getMessage());

                    }
                }); }
        });


        return view;
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getHostRatingsList(hostId);
    }
    private void getHostRatingsList(Long hostId) {
    //promeni
        Call<List<Rating>> call = ServiceUtils.ratingService.getAllHostRatings(hostId.toString());

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
                        ratingAdapter = new GHostRatingAdapter(acceptedRatings);
                        recyclerView.setAdapter(ratingAdapter);

                    }
                } else {
                    onFailure(call, new Throwable("API call failed with status code: " + response.code()));
                }
            }

            @Override
            public void onFailure(Call<List<Rating>> call, Throwable t) {
                Log.e("GHostRatingsFragment", "API call failed: " + t.getMessage());
            }
        });
    }

    public GHostRatingsFragment(Long hostId) {
        this.hostId = hostId;
    }
}