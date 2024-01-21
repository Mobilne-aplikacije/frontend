package project.roomeo.components.guest;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import project.roomeo.R;
import project.roomeo.models.Accommodation;
import project.roomeo.models.Report;
import project.roomeo.models.Reservation;
import project.roomeo.service.ServiceUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReservationViewHolder  extends RecyclerView.ViewHolder {
    public TextView accommodationName;
    public TextView accommodationAddress;
    public TextView startDate;
    public TextView endDate;
    private Accommodation accommodation;

    public ReservationViewHolder(View itemView) {
        super(itemView);
        // Inicijalizujte vaše komponente ovde
        accommodationName = itemView.findViewById(R.id.accommodationName);
        accommodationAddress = itemView.findViewById(R.id.accommodationAddress);
        startDate = itemView.findViewById(R.id.startDate);
        endDate = itemView.findViewById(R.id.endDate);
    }

    public void bindData(Reservation item) {
        endDate.setText(item.getEndDate());
        startDate.setText(item.getStartDate());

        Call<Accommodation> call = ServiceUtils.adminService.getAccommodation(String.valueOf(item.getAccommodationId()));
        call.enqueue(new Callback<Accommodation>() {
            @Override
            public void onResponse(@NonNull Call<Accommodation> call, @NonNull Response<Accommodation> response) {
                if (response.isSuccessful()) {
                    accommodation = response.body();
                    accommodationName.setText(accommodation.getName());
                    accommodationAddress.setText(accommodation.getLocation());

                } else {
                    onFailure(call, new Throwable("API call failed with status code: " + response.code()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<Accommodation> call, @NonNull Throwable t) {
                Log.e("AccommodationRatingsFragment", "API call failed: " + t.getMessage());

            }
        });

    }
}