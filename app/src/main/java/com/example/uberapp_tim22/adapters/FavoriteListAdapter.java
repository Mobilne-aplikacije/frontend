package com.example.uberapp_tim22.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uberapp_tim22.DTO.ResponseRideDTO;
import com.example.uberapp_tim22.R;

import java.util.List;

public class FavoriteListAdapter extends RecyclerView.Adapter<FavoriteListAdapter.RideViewHolder>{

    private List<ResponseRideDTO> rideList;
    private RideItemClickListener rideItemClickListener;

    public FavoriteListAdapter(RideItemClickListener rideItemClickListener) {
        this.rideItemClickListener = rideItemClickListener;
    }

    public void setRideList(List<ResponseRideDTO> rideList) {

        rideList.remove(0);
        rideList.remove(0);
        this.rideList = rideList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RideViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ride, parent, false);
        return new RideViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RideViewHolder holder, int position) {
        ResponseRideDTO ride = rideList.get(position);
        holder.bind(ride);
    }

    @Override
    public int getItemCount() {
        return rideList != null ? rideList.size() : 0;
    }

    class RideViewHolder extends RecyclerView.ViewHolder {

        private TextView rideTitleTextView;
        private TextView rideDetailsTextView;

        RideViewHolder(@NonNull View itemView) {
            super(itemView);
            rideTitleTextView = itemView.findViewById(R.id.rideTitleTextView);
            rideDetailsTextView = itemView.findViewById(R.id.rideDetailsTextView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        ResponseRideDTO ride = rideList.get(position);
                        rideItemClickListener.onRideItemClick(ride);
                    }
                }
            });
        }

        void bind(ResponseRideDTO ride) {
            rideTitleTextView.setText("Route " + ride.getId());

            String startTime = ride.getLocations().get(0).getDeparture().getAddress();

            String endTime = ride.getLocations().get(0).getDestination().getAddress();

            rideDetailsTextView.setText(startTime + " - " + endTime);
        }
    }

    public interface RideItemClickListener {
        void onRideItemClick(ResponseRideDTO ride);
    }
}