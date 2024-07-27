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
import project.roomeo.models.Reservation;
import project.roomeo.service.ServiceUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PastReservationAdapter extends RecyclerView.Adapter<PastReservationViewHolder> {
    private List<Reservation> reservationList;
    private boolean pending;

    public PastReservationAdapter(List<Reservation> reservationList) {
        this(reservationList, false);
    }

    public PastReservationAdapter(List<Reservation> reservationList, boolean pending) {
        this.reservationList = reservationList;
        this.pending = pending;
    }

    @NonNull
    @Override
    public PastReservationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.past_reservation_item_host, parent, false);
        return new PastReservationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PastReservationViewHolder holder, int position) {
        Reservation request = reservationList.get(position);

        holder.bindData(request);

    }


    @Override
    public int getItemCount() {
        return reservationList != null ? reservationList.size() : 0;
    }
}
