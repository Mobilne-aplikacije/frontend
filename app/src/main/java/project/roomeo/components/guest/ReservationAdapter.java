package project.roomeo.components.guest;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import project.roomeo.R;
import project.roomeo.components.host.AccommodationViewHolder;
import project.roomeo.components.host.HostAccommodationFragment;
import project.roomeo.components.host.HostMainActivity;
import project.roomeo.components.host.HostReservationsFragment;
import project.roomeo.models.Accommodation;
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reservation_item, parent, false);
        return new ReservationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReservationViewHolder holder, int position) {
        Reservation request = reservationList.get(position);

        holder.bindData(request);
        holder.button.setOnClickListener(v -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
            try {
                Date startDate = sdf.parse(request.getStartDate());
                Date currentDate = new Date();
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(currentDate);
                calendar.add(Calendar.DAY_OF_YEAR, 3);
                Date currentDatePlus3Days = calendar.getTime();

                if (startDate.after(currentDatePlus3Days)) {

                    Call<Reservation> call = ServiceUtils.reservationService.declineReservationRequest(request.getId().toString());
                    call.enqueue(new Callback<Reservation>() {
                        @Override
                        public void onResponse(@NonNull Call<Reservation> call, @NonNull Response<Reservation> response) {

                            if (response.isSuccessful()) {
                                Log.e("Top", "top");

                            } else {
                                onFailure(call, new Throwable("API call failed with status code: " + response.code()));
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<Reservation> call, @NonNull Throwable t) {
                            Log.e("ReservationAdapter", "API call failed: " + t.getMessage());

                        }
                    });

                    builder.setMessage("uspesno ste otkazali")
                            .setCancelable(false)
                            .setPositiveButton("OK", (dialog, id) -> {
                                GuestReservationsFragment fragment = new GuestReservationsFragment();
                                ((GuestMainActivity) v.getContext()).loadFragment(fragment);
                            });


                    AlertDialog alert = builder.create();
                    alert.show();
                } else {
                    builder.setMessage("Reservations can be canceled at least 3 days before the start date.")
                            .setCancelable(false)
                            .setPositiveButton("OK", (dialog, id) -> {
                                dialog.dismiss();
                            });


                    AlertDialog alert = builder.create();
                    alert.show();
                }


            } catch (ParseException e) {
                throw new RuntimeException(e);
            }

        });
    }

    @Override
    public int getItemCount() {
        return reservationList != null ? reservationList.size() : 0;
    }
}
