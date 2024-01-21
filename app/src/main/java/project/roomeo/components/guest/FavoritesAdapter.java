package project.roomeo.components.guest;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import project.roomeo.R;
import project.roomeo.components.admin.AdminMainActivity;
import project.roomeo.components.admin.UserReportRequestsFragment;
import project.roomeo.components.host.HostMainActivity;
import project.roomeo.components.host.HostRatingsFragment;
import project.roomeo.models.Accommodation;
import project.roomeo.models.Report;
import project.roomeo.service.ServiceUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesViewHolder> {
    private List<Accommodation> accommodationList;
    private Long myId;

    public FavoritesAdapter(List<Accommodation> accommodationList, Long myId) {
        this.accommodationList = accommodationList;
        this.myId = myId;
    }

    @NonNull
    @Override
    public FavoritesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.favorite_item, parent, false);
        return new FavoritesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoritesViewHolder holder, int position) {
        Accommodation request = accommodationList.get(position);

        holder.bindData(request);

        holder.removeFromFav.setOnClickListener(view -> {

                Call<Void> call = ServiceUtils.guestService.removeFavorite(myId,request.getId());
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                        if (response.isSuccessful()) {
                            GuestFavoritesFragment fragment = new GuestFavoritesFragment();
                            ((GuestMainActivity) view.getContext()).loadFragment(fragment);
                        } else {
                            onFailure(call, new Throwable("API call failed with status code: " + response.code()));
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                        Log.e("Report", "API call failed: " + t.getMessage());

                    }
                });

        });
        holder.details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GuestAccommodationFragment fragment = new GuestAccommodationFragment(false,myId);
                fragment.setAccommodationRequest(request);
                ((GuestMainActivity) v.getContext()).loadFragment(fragment);
            }
        });
    }


    @Override
    public int getItemCount() {
        return accommodationList != null ? accommodationList.size() : 0;
    }

}
