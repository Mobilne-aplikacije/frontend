package project.roomeo.components.guest;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import project.roomeo.R;
import project.roomeo.models.Accommodation;

public class AccommodationViewHolder  extends RecyclerView.ViewHolder {
    public TextView id;
    public TextView name;
    public TextView description;
    public TextView location;
    public TextView wifi;
    public TextView type;
    public TextView kitchen;
    public TextView airConditioner;
    public TextView parking;
    //    public List<Date> availability;
    public TextView payment;
    public TextView price;
    public TextView bookingMethod;
    //    private List<Rating> ratings;
//    private List<String> photos;
    public TextView minGuest;
    public TextView maxGuest;
    //    private AccommodationRequestStatus status;
    public TextView hostId;
    public Button details;
    public TextView deadline;
    public TextView priceIncrease;
    public ImageView placeImage;
    private Context context;

    public AccommodationViewHolder(View itemView, Context context) {
        super(itemView);
        // Inicijalizujte vaše komponente ovde
        name = itemView.findViewById(R.id.name);
        description = itemView.findViewById(R.id.description);
        location = itemView.findViewById(R.id.location);
        type = itemView.findViewById(R.id.type);
        payment = itemView.findViewById(R.id.payment);
        price = itemView.findViewById(R.id.price);
        bookingMethod = itemView.findViewById(R.id.bookingMethod);
        minGuest = itemView.findViewById(R.id.minGuest);
        maxGuest = itemView.findViewById(R.id.maxGuest);
        details = itemView.findViewById(R.id.details);
        priceIncrease = itemView.findViewById(R.id.priceIncrease);
        deadline = itemView.findViewById(R.id.deadline);
        placeImage = itemView.findViewById(R.id.placeImage);
        this.context = context;
    }

    public void bindData(Accommodation item) {
        name.setText(item.getName());
        description.setText(item.getDescription());
        location.setText(item.getLocation());
        type.setText(item.getType().getDisplayName());
        payment.setText(item.getPayment().getDisplayName());
        price.setText(item.getPrice()+"$");
        minGuest.setText("Min guests: "+ item.getMinGuest());
        maxGuest.setText("Max guests: "+ item.getMaxGuest());
        Log.i("DEADLINE:", String.valueOf(item.getCancellationDeadline()));

        if (deadline != null) {
            deadline.setText(item.getCancellationDeadline());
        }

        int drawableResourceId = context.getResources().getIdentifier(item.getPhotos(), "drawable", context.getPackageName());

        if (drawableResourceId != 0) {
            Glide.with(itemView)
                    .load(drawableResourceId)
                    .placeholder(R.drawable.ic_email)
                    .error(R.drawable.image3)
                    .centerCrop()
                    .into(placeImage);
        } else {
            // Postavite podrazumevanu sliku ili preduzmite odgovarajuće akcije
            Glide.with(itemView)
                    .load(R.drawable.aparment_placeholder)
                    .placeholder(R.drawable.ic_email)
                    .error(R.drawable.image3)
                    .centerCrop()
                    .into(placeImage);
            }
        }
    }