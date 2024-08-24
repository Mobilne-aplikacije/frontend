package project.roomeo.components.host;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import project.roomeo.R;
import project.roomeo.models.Guest;
import project.roomeo.models.Reservation;
import project.roomeo.service.ServiceUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReservationAdapter extends RecyclerView.Adapter<ReservationViewHolder> {
    private List<Reservation> reservationList;
    private boolean pending;

    public ReservationAdapter(List<Reservation> reservationList) {
        this(reservationList, false);
    }

    public ReservationAdapter(List<Reservation> reservationList, boolean pending) {
        this.reservationList = reservationList;
        this.pending = pending;
    }

    @NonNull
    @Override
    public ReservationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reservation_item_host, parent, false);
        return new ReservationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReservationViewHolder holder, int position) {
        Reservation reservation = reservationList.get(position);

        holder.accommodationName.setText(reservation.getAccommodationName());
        holder.startDate.setText(reservation.getStartDate());
        holder.endDate.setText(reservation.getEndDate());
        holder.price.setText(String.valueOf(reservation.getPrice()));
        holder.numberOfPeople.setText(String.valueOf(reservation.getNumberOfPeople()));

        holder.bindData(reservation);
        getGuestDetails((long) reservation.getGuestId(), guestDetails -> {
            String fullName = guestDetails.getFirstName() + " " + guestDetails.getLastName();

            getCancelledReservationsCount(reservation.getGuestId(), cancelledCount -> {
                String requestedByText = "Requested by: " + fullName + " (Number of Cancelations: " + cancelledCount + ")";
                holder.requestedBy.setText(requestedByText);
            });
        });

    }

    private void getGuestDetails(Long guestId, RequestAdapter.OnGuestDetailsReceivedListener listener) {
        Call<Guest> call = ServiceUtils.guestService.getGuest(String.valueOf(guestId));
        call.enqueue(new Callback<Guest>() {
            @Override
            public void onResponse(Call<Guest> call, Response<Guest> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listener.onReceived(response.body());
                } else {
                    Log.e("ReservationAdapter", "Failed to get guest details.");
                }
            }

            @Override
            public void onFailure(Call<Guest> call, Throwable t) {
                Log.e("ReservationAdapter", "API call failed: " + t.getMessage());
            }
        });
    }

    private void getCancelledReservationsCount(int guestId, RequestAdapter.OnCancelledCountReceivedListener listener) {
        Call<Integer> call = ServiceUtils.guestService.getCancelledReservationsCount((long) guestId);
        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listener.onReceived(response.body());
                } else {
                    Log.e("ReservationAdapter", "Failed to get cancelled reservations count.");
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Log.e("ReservationAdapter", "API call failed: " + t.getMessage());
            }
        });
    }

    public void updateList(List<Reservation> newList) {
        reservationList = newList;
        notifyDataSetChanged();
    }



    @Override
    public int getItemCount() {
        return reservationList != null ? reservationList.size() : 0;
    }
}
