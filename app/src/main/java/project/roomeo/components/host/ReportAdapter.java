package project.roomeo.components.host;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import project.roomeo.R;
import project.roomeo.models.ReportItem;

public class ReportAdapter extends RecyclerView.Adapter<ReportsViewHolder> {
    private List<ReportItem> reservationList;


    public ReportAdapter(List<ReportItem> reservationList) {
        this.reservationList = reservationList;
    }

    @NonNull
    @Override
    public ReportsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reports_item, parent, false);
        return new ReportsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportsViewHolder holder, int position) {
        ReportItem request = reservationList.get(position);

        holder.bindData(request);

//        holder.details.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                HostAccommodationFragment fragment = new HostAccommodationFragment(pending);
//                fragment.setAccommodationRequest(request);
//                ((HostMainActivity) v.getContext()).loadFragment(fragment);
//            }
//        });
    }


    @Override
    public int getItemCount() {
        return reservationList != null ? reservationList.size() : 0;
    }
}
