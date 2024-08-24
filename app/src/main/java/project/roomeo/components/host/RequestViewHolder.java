package project.roomeo.components.host;

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
    private Accommodation accommodation;
    public Button buttonAccept;
    public Button buttonDecline;
    public TextView price;
    public TextView requestedBy;



    public RequestViewHolder(View itemView) {
        super(itemView);
        accommodationName = itemView.findViewById(R.id.accommodationName);
        accommodationAddress = itemView.findViewById(R.id.accommodationAddress);
        startDate = itemView.findViewById(R.id.startDate);
        endDate = itemView.findViewById(R.id.endDate);
        peopleCount = itemView.findViewById(R.id.peopleCount);
        buttonAccept = itemView.findViewById(R.id.buttonAccept);
        buttonDecline = itemView.findViewById(R.id.buttonDecline);
        price = itemView.findViewById(R.id.price);
        requestedBy = itemView.findViewById(R.id.requestedBy);

    }

    public void bindData(Reservation item) {
        endDate.setText("To:       "+ item.getEndDate());
        startDate.setText("From:  "+ item.getStartDate());
        price.setText(item.getPrice()+"$");
        peopleCount.setText("People: " + item.getNumberOfPeople());

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