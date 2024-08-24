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

public class RequestAdapter extends RecyclerView.Adapter<RequestViewHolder> {
    private List<Reservation> reservationList;
    private boolean pending;

    public RequestAdapter(List<Reservation> reservationList) {
        this(reservationList, false);
    }

    public RequestAdapter(List<Reservation> reservationList, boolean pending) {
        this.reservationList = reservationList;
        this.pending = pending;
    }

    @NonNull
    @Override
    public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.request_item_host, parent, false);
        return new RequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestViewHolder holder, int position) {
        Reservation request = reservationList.get(position);
        System.out.println(request.getAccommodationName());

        holder.bindData(request);
        holder.bindData(request);
        getGuestDetails((long) request.getGuestId(), guestDetails -> {
            String fullName = guestDetails.getFirstName() + " " + guestDetails.getLastName();

            getCancelledReservationsCount(request.getGuestId(), cancelledCount -> {
                String requestedByText = "Requested by: " + fullName + " (Number of Cancelations: " + cancelledCount + ")";
                holder.requestedBy.setText(requestedByText);
            });
        });
        holder.buttonAccept.setOnClickListener(v -> {
            System.out.println("KLIKNUT: " + request.getId());

            Call<Reservation> call = ServiceUtils.reservationService.acceptReservationRequest(request.getId().toString());
            call.enqueue(new Callback<Reservation>() {
                @Override
                public void onResponse(@NonNull Call<Reservation> call, @NonNull Response<Reservation> response) {

                    if (response.isSuccessful()) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                        builder.setMessage("Request accepted successfully.")
                                .setCancelable(false)
                                .setPositiveButton("OK", (dialog, id) -> {
                                    HostReservationsFragment fragment = new HostReservationsFragment();
                                    ((HostMainActivity) v.getContext()).loadFragment(fragment);
                                });

                        AlertDialog alert = builder.create();
                        alert.show();

                    } else {
                        onFailure(call, new Throwable("API call failed with status code: " + response.code()));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Reservation> call, @NonNull Throwable t) {
                    Log.e("RequestAdapter", "API call failed: " + t.getMessage());

                }
            });
        });

        holder.buttonDecline.setOnClickListener(v -> {

            Call<Reservation> call = ServiceUtils.reservationService.declineReservationRequest(request.getId().toString());
            call.enqueue(new Callback<Reservation>() {
                @Override
                public void onResponse(@NonNull Call<Reservation> call, @NonNull Response<Reservation> response) {

                    if (response.isSuccessful()) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                        builder.setMessage("Request declined successfully.")
                                .setCancelable(false)
                                .setPositiveButton("OK", (dialog, id) -> {
                                    HostReservationsFragment fragment = new HostReservationsFragment();
                                    ((HostMainActivity) v.getContext()).loadFragment(fragment);
                                });


                        AlertDialog alert = builder.create();
                        alert.show();

                    } else {
                        onFailure(call, new Throwable("API call failed with status code: " + response.code()));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<Reservation> call, @NonNull Throwable t) {
                    Log.e("RequestAdapter", "API call failed: " + t.getMessage());

                }
            });
        });
    }

    private void getGuestDetails(Long guestId, OnGuestDetailsReceivedListener listener) {
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

    private void getCancelledReservationsCount(int guestId, OnCancelledCountReceivedListener listener) {
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

    interface OnGuestDetailsReceivedListener {
        void onReceived(Guest guestDetails);
    }

    interface OnCancelledCountReceivedListener {
        void onReceived(int cancelledCount);
    }


    @Override
    public int getItemCount() {
        return reservationList != null ? reservationList.size() : 0;
    }

    public void updateList(List<Reservation> newList) {
        reservationList = newList;
        notifyDataSetChanged();
    }
}
