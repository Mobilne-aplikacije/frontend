package project.roomeo.components.host;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import project.roomeo.R;
import project.roomeo.models.Accommodation;

public class AccommodationAdapter extends RecyclerView.Adapter<AccommodationViewHolder> {
    private List<Accommodation> accommodationList;
    private boolean pending;
    private Context context;

    public AccommodationAdapter(List<Accommodation> accommodationList, Context context) {
        this(accommodationList, false,context);
        this.context = context;
    }

    public AccommodationAdapter(List<Accommodation> accommodationList, boolean pending, Context context) {
        this.accommodationList = accommodationList;
        this.pending = pending;
        this.context = context;
    }

    @NonNull
    @Override
    public AccommodationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.accommodation_item, parent, false);
        return new AccommodationViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull AccommodationViewHolder holder, int position) {
        Accommodation request = accommodationList.get(position);

        holder.bindData(request);

        holder.details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HostAccommodationFragment fragment = new HostAccommodationFragment(pending);
                fragment.setAccommodationRequest(request);
                ((HostMainActivity) v.getContext()).loadFragment(fragment);
            }
        });
    }


    @Override
    public int getItemCount() {
        return accommodationList != null ? accommodationList.size() : 0;
    }
}
