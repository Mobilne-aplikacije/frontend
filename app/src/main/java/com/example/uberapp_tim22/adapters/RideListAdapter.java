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

public class RideListAdapter extends RecyclerView.Adapter<RideListAdapter.RideViewHolder>{

    private List<ResponseRideDTO> rideList;
    private RideItemClickListener rideItemClickListener;

    public RideListAdapter(RideItemClickListener rideItemClickListener) {
        this.rideItemClickListener = rideItemClickListener;
    }

    public void setRideList(List<ResponseRideDTO> rideList) {
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
            rideTitleTextView.setText("Ride " + ride.getId());

            String startTime = ride.getStartTime() != null ? String.format("%02d:%02d",
                    ride.getStartTime().getHours(),
                    ride.getStartTime().getMinutes()) : "";

            String endTime = ride.getEndTime() != null ? String.format("%02d:%02d",
                    ride.getEndTime().getHours(),
                    ride.getEndTime().getMinutes()) : "";

            rideDetailsTextView.setText(startTime + " - " + endTime);
        }
    }

    public interface RideItemClickListener {
        void onRideItemClick(ResponseRideDTO ride);
    }
}