package project.roomeo.components.guest;

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

    public RequestAdapter(List<Reservation> reservationList) {
        this.reservationList = reservationList;
    }

    @NonNull
    @Override
    public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.request_item_guest, parent, false);
        return new RequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestViewHolder holder, int position) {
        Reservation request = reservationList.get(position);

        holder.bindData(request);
        holder.deleteRequestButton.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
            builder.setMessage("Are you sure you want to delete this request?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", (dialog, id) -> {
                        deleteRequest(request, position);
                    })
                    .setNegativeButton("No", (dialog, id) -> dialog.cancel());

            AlertDialog alert = builder.create();
            alert.show();
        });
    }

    @Override
    public int getItemCount() {
        return reservationList != null ? reservationList.size() : 0;
    }

    public void updateList(List<Reservation> newList) {
        reservationList = newList;
        notifyDataSetChanged();
    }

    private void deleteRequest(Reservation request, int position) {
        Call<Void> call = ServiceUtils.reservationService.deleteReservationRequest(request.getId().toString());

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    reservationList.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, reservationList.size());

                    Log.i("RequestAdapter", "Request deleted successfully");
                } else {
                    Log.e("RequestAdapter", "Failed to delete request: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("RequestAdapter", "API call failed: " + t.getMessage());
            }
        });
    }
}
