package project.roomeo.components.guest;

import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import project.roomeo.R;
import project.roomeo.models.Accommodation;
import project.roomeo.models.Reservation;
import project.roomeo.service.ServiceUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RequestViewHolder extends RecyclerView.ViewHolder {
    public TextView accommodationName;
    public TextView accommodationAddress;
    public TextView startDate;
    public TextView endDate;
    public TextView peopleCount;
    public TextView status;
    public Button deleteRequestButton;
    public TextView price;
    private Accommodation accommodation;


    public RequestViewHolder(@NonNull View itemView) {
        super(itemView);
        accommodationName = itemView.findViewById(R.id.accommodationName);
        accommodationAddress = itemView.findViewById(R.id.accommodationAddress);
        startDate = itemView.findViewById(R.id.startDate);
        endDate = itemView.findViewById(R.id.endDate);
        peopleCount = itemView.findViewById(R.id.peopleCount);
        status = itemView.findViewById(R.id.status);
        deleteRequestButton = itemView.findViewById(R.id.deleteRequestButton);
        price = itemView.findViewById(R.id.price);
    }

    public void bindData(Reservation item) {
        endDate.setText("To:       "+ item.getEndDate());
        startDate.setText("From:  "+ item.getStartDate());
        price.setText(item.getPrice()+"$");

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

        peopleCount.setText("People: " + item.getNumberOfPeople());

        status.setText("Status: " + item.getStatus().toString());
        switch (item.getStatus()) {
            case PENDING:
                status.setTextColor(Color.parseColor("#FF9800"));
                deleteRequestButton.setVisibility(View.VISIBLE);
                break;
            case ACCEPTED:
                status.setTextColor(Color.parseColor("#4CAF50"));
                deleteRequestButton.setVisibility(View.GONE);
                break;
            case REJECTED:
                status.setTextColor(Color.parseColor("#F44336"));
                deleteRequestButton.setVisibility(View.VISIBLE);
                break;
            case CANCELED:
                status.setTextColor(Color.parseColor("#9E9E9E"));
                deleteRequestButton.setVisibility(View.GONE);
                break;
        }
    }
}
