package project.roomeo.components.host;

import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import project.roomeo.R;
import project.roomeo.components.admin.AdminMainActivity;
import project.roomeo.components.admin.RatingRequestsFragment;
import project.roomeo.components.guest.GuestMainActivity;
import project.roomeo.components.guest.GuestNotificationsFragment;
import project.roomeo.models.Rating;
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
        Reservation request = reservationList.get(position);

        holder.bindData(request);
        holder.buttonAccept.setOnClickListener(v -> {

            Call<Reservation> call = ServiceUtils.reservationService.acceptReservationRequest(request.getId().toString());
            call.enqueue(new Callback<Reservation>() {
                @Override
                public void onResponse(@NonNull Call<Reservation> call, @NonNull Response<Reservation> response) {

                    if (response.isSuccessful()) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                        builder.setMessage("uspesno ste prihv")
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
                    Log.e("ReservationAdapter", "API call failed: " + t.getMessage());

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
                        builder.setMessage("uspesno ste odbili")
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
                    Log.e("ReservationAdapter", "API call failed: " + t.getMessage());

                }
            });
        });
    }


    @Override
    public int getItemCount() {
        return reservationList != null ? reservationList.size() : 0;
    }
}
