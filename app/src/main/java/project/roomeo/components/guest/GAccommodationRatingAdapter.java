package project.roomeo.components.guest;

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
import project.roomeo.components.admin.RatingViewHolder;
import project.roomeo.models.Rating;
import project.roomeo.service.ServiceUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GAccommodationRatingAdapter extends RecyclerView.Adapter<RatingViewHolder> {

    public List<Rating> ratingList;
    private FragmentManager fragmentManager;
    private Long hostId;
    private Long guestId;

    public GAccommodationRatingAdapter(List<Rating> ratingList, FragmentManager fragmentManager) {
        this.ratingList = ratingList;
        this.fragmentManager = fragmentManager;
    }

    public GAccommodationRatingAdapter(List<Rating> ratingList, Long hostId, Long guestId) {
        this.ratingList = ratingList;
        this.hostId = hostId;
        this.guestId = guestId;
    }
    @NonNull
    @Override
    public RatingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.host_g_ratings_item, parent, false);
        return new RatingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RatingViewHolder holder, int position) {
        Rating request = ratingList.get(position);

        holder.bindData(request);
        if (request.getGuestId()==guestId.intValue()) {
            holder.acceptButton.setVisibility(View.VISIBLE);
        } else {
            holder.acceptButton.setVisibility(View.GONE);
        }

        holder.acceptButton.setOnClickListener(view -> {
            String ratingId = request.getId().toString();

            Call<Void> call = ServiceUtils.ratingService.deleteRating(Long.valueOf(ratingId));
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {

                    if (response.isSuccessful()) {
                        GAccommodationRatingsFragment fragment = new GAccommodationRatingsFragment(hostId, Long.valueOf(request.getAccommodationId()));
                        ((GuestMainActivity) view.getContext()).loadFragment(fragment);
                        Toast.makeText(view.getContext(), "Rating deleted.", Toast.LENGTH_SHORT).show();
                    } else {
                        onFailure(call, new Throwable("API call failed with status code: " + response.code()));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
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