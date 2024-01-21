package project.roomeo.components.admin;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import project.roomeo.R;
import project.roomeo.models.Guest;
import project.roomeo.models.Rating;
import project.roomeo.models.Report;
import project.roomeo.service.ServiceUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RatingReportRequestAdapter extends RecyclerView.Adapter<RatingReportViewHolder> {

    private List<Report> reportList;
    private FragmentManager fragmentManager;
    private Rating rating;

    public RatingReportRequestAdapter(List<Report> reportList, FragmentManager fragmentManager) {
        this.reportList = reportList;
        this.fragmentManager = fragmentManager;
    }

    public RatingReportRequestAdapter(List<Report> reportList) {
        this.reportList = reportList;
    }

    @NonNull
    @Override
    public RatingReportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rating_report_request_item, parent, false);
        return new RatingReportViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RatingReportViewHolder holder, int position) {
        Report request = reportList.get(position);

        holder.bindData(request);

        holder.acceptButton.setOnClickListener(view -> {
            Long reportId = request.getId();

            Call<Void> call = ServiceUtils.reportService.deleteReport(reportId);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                    if (response.isSuccessful()) {

                        Call<Rating> callR = ServiceUtils.ratingService.getRating(String.valueOf(request.getRatingId()));

                        callR.enqueue(new Callback<Rating>() {
                            @Override
                            public void onResponse(Call<Rating> call, Response<Rating> response) {
                                if (response.isSuccessful()) {
                                    rating = response.body();
                                    Log.e("RATING del", rating.toString());

                                    Call<Void> callD = ServiceUtils.ratingService.deleteRating(rating.getId());
                                    callD.enqueue(new Callback<Void>() {
                                        @Override
                                        public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                                            if (response.isSuccessful()) {
                                                RatingReportRequestsFragment fragment = new RatingReportRequestsFragment();
                                                ((AdminMainActivity) view.getContext()).loadFragment(fragment);
                                                Toast.makeText(view.getContext(), "Rating request accepted.", Toast.LENGTH_SHORT).show();
                                            } else {
                                                onFailure(callD, new Throwable("API call failed with status code: " + response.code()));
                                            }
                                        }

                                        @Override
                                        public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                                            Log.e("RatingAdapter", "API call failed: " + t.getMessage());

                                        }
                                    });
                                } else {
                                    onFailure(callR, new Throwable("API call failed with status code: " + response.code()));
                                }
                            }

                            @Override
                            public void onFailure(Call<Rating> call, Throwable t) {
                                Log.e("Host", "API call failed: " + t.getMessage());
                            }
                        });
                        RatingReportRequestsFragment fragment = new RatingReportRequestsFragment();
                        ((AdminMainActivity) view.getContext()).loadFragment(fragment);


                    } else {
                        onFailure(call, new Throwable("API call failed with status code: " + response.code()));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                    Log.e("RatingReportRequestsFragment", "API call failed: " + t.getMessage());

                }
            });
        });

        holder.declineButton.setOnClickListener(view -> {
            Long reportId = request.getId();

            Call<Void> call = ServiceUtils.reportService.deleteReport(reportId);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                    if (response.isSuccessful()) {
                        RatingReportRequestsFragment fragment = new RatingReportRequestsFragment();
                        ((AdminMainActivity) view.getContext()).loadFragment(fragment);
                        Toast.makeText(view.getContext(), "Rating request rejected.", Toast.LENGTH_SHORT).show();
                    } else {
                        onFailure(call, new Throwable("API call failed with status code: " + response.code()));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                    Log.e("RatingReportRequestsFragment", "API call failed: " + t.getMessage());

                }
            });
        });
    }


    @Override
    public int getItemCount() {
        return reportList != null ? reportList.size() : 0;
    }

}
