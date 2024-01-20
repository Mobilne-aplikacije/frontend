package project.roomeo.components.admin;

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
import project.roomeo.models.Report;
import project.roomeo.service.ServiceUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RatingReportRequestsFragment extends Fragment {

    private RecyclerView recyclerView;
    private RatingReportRequestAdapter ratingReportAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rating_report_requests, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);

        // Postavljanje layout manager-a (npr. LinearLayoutManager)
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Kreiranje adapter-a i povezivanje sa RecyclerView-om
        getRatingReportRequestsList(); // Inicijalizacija liste

        return view;
    }

    private void getRatingReportRequestsList() {

        Call<List<Report>> call = ServiceUtils.reportService.getAllReports();

        call.enqueue(new Callback<List<Report>>() {
            @Override
            public void onResponse(Call<List<Report>> call, Response<List<Report>> response) {
                if (response.isSuccessful()) {
                    List<Report> list = response.body();
                    if (list != null) {
                        List<Report> ratingReports = new ArrayList<>();
                        for (Report r:list){
                            if (r.getRatingId()!=null){
                                ratingReports.add(r);
                            }
                        }
                        ratingReportAdapter = new RatingReportRequestAdapter(ratingReports);
                        recyclerView.setAdapter(ratingReportAdapter);

                    }
                } else {
                    onFailure(call, new Throwable("API call failed with status code: " + response.code()));
                }
            }

            @Override
            public void onFailure(Call<List<Report>> call, Throwable t) {
                Log.e("RatingReportRequestsFragment", "API call failed: " + t.getMessage());
            }
        });
    }
}