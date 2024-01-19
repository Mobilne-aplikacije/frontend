package project.roomeo.components.admin;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import project.roomeo.R;
import project.roomeo.models.Accommodation;
import project.roomeo.models.Rating;

public class RatingViewHolder extends RecyclerView.ViewHolder {

    public TextView price;
    public Button acceptButton;
    public Button declineButton;

    public RatingViewHolder(View itemView){
        super(itemView);
        price = itemView.findViewById(R.id.price);
        acceptButton = itemView.findViewById(R.id.accept);
        declineButton = itemView.findViewById(R.id.decline);
    }

    @SuppressLint("SetTextI18n")
    public void bindData(Rating item) {
        price.setText(String.valueOf(item.getRating()));
    }
}
