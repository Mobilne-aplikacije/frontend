package project.roomeo.components.guest;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import project.roomeo.R;
import project.roomeo.components.host.AccommodationViewHolder;
import project.roomeo.components.host.HostAccommodationFragment;
import project.roomeo.components.host.HostMainActivity;
import project.roomeo.models.Accommodation;
import project.roomeo.models.Reservation;

public class ReservationAdapter  extends RecyclerView.Adapter<ReservationViewHolder> {
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

    }


    @Override
    public int getItemCount() {
        return reservationList != null ? reservationList.size() : 0;
    }
}
