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
import project.roomeo.models.Rating;
import project.roomeo.service.ServiceUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RatingRequestAdapter extends RecyclerView.Adapter<RatingViewHolder> {

    private List<Rating> ratingList;
    private FragmentManager fragmentManager;

    public RatingRequestAdapter(List<Rating> ratingList, FragmentManager fragmentManager) {
        this.ratingList = ratingList;
        this.fragmentManager = fragmentManager;
    }

    public RatingRequestAdapter(List<Rating> ratingList) {
        this.ratingList = ratingList;
    }

    @NonNull
    @Override
    public RatingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rating_requests_item, parent, false);
        return new RatingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RatingViewHolder holder, int position) {
        Rating request = ratingList.get(position);

        holder.bindData(request);

        holder.acceptButton.setOnClickListener(view -> {
            String ratingId = request.getId().toString();
            Call<Rating> call = ServiceUtils.ratingService.acceptRatingRequest(ratingId);
            call.enqueue(new Callback<Rating>() {
                @Override
                public void onResponse(@NonNull Call<Rating> call, @NonNull Response<Rating> response) {

                    if (response.isSuccessful()) {
                        RatingRequestsFragment fragment = new RatingRequestsFragment();
                        ((AdminMainActivity) view.getContext()).loadFragment(fragment);
                        Toast.makeText(view.getContext(), "Request accepted.", Toast.LENGTH_SHORT).show();

                    } else {
                        onFailure(call, new Throwable("API call failed with status code: " + response.code()));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Rating> call, @NonNull Throwable t) {
                    Log.e("RatingAdapter", "API call failed: " + t.getMessage());

                }
            });
        });

        holder.declineButton.setOnClickListener(view -> {
            String ratingId = request.getId().toString();

            Call<Rating> call = ServiceUtils.ratingService.rejectRatingRequest(ratingId);
            call.enqueue(new Callback<Rating>() {
                @Override
                public void onResponse(@NonNull Call<Rating> call, @NonNull Response<Rating> response) {
                    if (response.isSuccessful()) {
                        RatingRequestsFragment fragment = new RatingRequestsFragment();
                        ((AdminMainActivity) view.getContext()).loadFragment(fragment);
                        Toast.makeText(view.getContext(), "Request rejected.", Toast.LENGTH_SHORT).show();

                    } else {
                        onFailure(call, new Throwable("API call failed with status code: " + response.code()));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Rating> call, @NonNull Throwable t) {
                    Log.e("RatingAdapter", "API call failed: " + t.getMessage());

                }
            });
        });
    }


    @Override
    public int getItemCount() {
        return ratingList != null ? ratingList.size() : 0;
    }

}
