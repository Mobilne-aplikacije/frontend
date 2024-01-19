package project.roomeo.components.admin;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import project.roomeo.R;
import project.roomeo.components.host.AccommodationRatingAdapter;
import project.roomeo.components.host.HostRatingAdapter;
import project.roomeo.models.Accommodation;
import project.roomeo.models.Guest;
import project.roomeo.models.Host;
import project.roomeo.models.Rating;
import project.roomeo.service.ServiceUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RatingViewHolder extends RecyclerView.ViewHolder {

    public TextView rating;
    public TextView comment;
    public TextView guestName;
    public TextView ratingDate;
    public Button acceptButton;
    public Button declineButton;
    public Guest guest;

    public RatingViewHolder(View itemView){
        super(itemView);
        rating = itemView.findViewById(R.id.rating);
        comment = itemView.findViewById(R.id.comment);
        guestName = itemView.findViewById(R.id.guest);
        ratingDate = itemView.findViewById(R.id.date);

        acceptButton = itemView.findViewById(R.id.accept);
        declineButton = itemView.findViewById(R.id.decline);
        }

    @SuppressLint("SetTextI18n")
    public void bindData(Rating item) {
        rating.setText(String.valueOf(item.getRating()));
        comment.setText(item.getComment());
        ratingDate.setText(String.valueOf(item.getRatingDate()));

        Call<Guest> call = ServiceUtils.guestService.getGuest(String.valueOf(item.getGuestId()));

        call.enqueue(new Callback<Guest>() {
            @Override
            public void onResponse(Call<Guest> call, Response<Guest> response) {
                if (response.isSuccessful()) {
                    guest = response.body();
                    Log.i("guest",guest.toString());
                    guestName.setText(guest.getFirstName()+" "+guest.getLastName());
                } else {
                    onFailure(call, new Throwable("API call failed with status code: " + response.code()));
                }
            }

            @Override
            public void onFailure(Call<Guest> call, Throwable t) {
                Log.e("Host", "API call failed: " + t.getMessage());
            }
        });
    }

}
