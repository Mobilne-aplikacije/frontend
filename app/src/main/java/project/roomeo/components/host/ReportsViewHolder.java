package project.roomeo.components.host;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import project.roomeo.R;
import project.roomeo.models.Accommodation;
import project.roomeo.models.ReportItem;
import project.roomeo.models.Reservation;

public class ReportsViewHolder  extends RecyclerView.ViewHolder{
    public TextView name;
    public TextView resNum;
    public TextView profit;
    public Button removeFromFav;

    public ReportsViewHolder(View itemView) {
        super(itemView);

        name = itemView.findViewById(R.id.name);
        resNum = itemView.findViewById(R.id.resNum);
        profit = itemView.findViewById(R.id.profit);
    }

    public void bindData(ReportItem item) {
        name.setText(item.getName());
        resNum.setText(String.valueOf(item.getResNum()));
        profit.setText(String.valueOf(item.getProfit()));
    }
}
