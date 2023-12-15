package project.roomeo.components.admin;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import project.roomeo.R;
import project.roomeo.models.Accommodation;
import project.roomeo.service.ServiceUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccommodationAdapter extends RecyclerView.Adapter<AccommodationViewHolder> {
    private List<Accommodation> accommodationList;
    private FragmentManager fragmentManager;

    public AccommodationAdapter(List<Accommodation> accommodationList, FragmentManager fragmentManager) {
        this.accommodationList = accommodationList;
        this.fragmentManager = fragmentManager;
    }
    public AccommodationAdapter(List<Accommodation> accommodationList) {
        this.accommodationList = accommodationList;
    }

    @NonNull
    @Override
    public AccommodationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.accommodation_requests_item, parent, false);
        return new AccommodationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AccommodationViewHolder holder, int position) {
        Accommodation request = accommodationList.get(position);

        holder.bindData(request);

        holder.acceptButton.setOnClickListener(view -> {
            String accommodationId = request.getId().toString();
            Call<Accommodation> call = ServiceUtils.adminService.acceptAccommodationRequest(accommodationId);
            call.enqueue(new Callback<Accommodation>() {
                @Override
                public void onResponse(@NonNull Call<Accommodation> call, @NonNull Response<Accommodation> response) {

                    if (response.isSuccessful()) {
                        AccommodationRequestsFragment fragment = new AccommodationRequestsFragment();
                        ((AdminMainActivity) view.getContext()).loadFragment(fragment);
                    } else {
                        onFailure(call, new Throwable("API call failed with status code: " + response.code()));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Accommodation> call, @NonNull Throwable t) {
                    Log.e("AccommodationAdapter", "API call failed: " + t.getMessage());

                }
            });
        });

        holder.declineButton.setOnClickListener(view -> {
            String accommodationId = request.getId().toString();

            Call<Accommodation> call = ServiceUtils.adminService.rejectAccommodationRequest(accommodationId);
            call.enqueue(new Callback<Accommodation>() {
                @Override
                public void onResponse(@NonNull Call<Accommodation> call, @NonNull Response<Accommodation> response) {
                   if (response.isSuccessful()) {
                        AccommodationRequestsFragment fragment = new AccommodationRequestsFragment();
                        ((AdminMainActivity) view.getContext()).loadFragment(fragment);
                    } else {
                        onFailure(call, new Throwable("API call failed with status code: " + response.code()));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Accommodation> call, @NonNull Throwable t) {
                    Log.e("AccommodationAdapter", "API call failed: " + t.getMessage());

                }
            });
        });
    }

    private void startNewActivityOrFragment() {
        FragmentManager fragmentManager = this.fragmentManager;
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.accommodation_requests_fragment, new AccommodationRequestsFragment());
        transaction.commit();
    }


    @Override
    public int getItemCount() {
        return accommodationList != null ? accommodationList.size() : 0;
    }
}
