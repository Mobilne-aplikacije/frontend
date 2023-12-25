package project.roomeo.components.admin;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.Date;
import java.util.List;

import project.roomeo.R;
import project.roomeo.models.Accommodation;
import project.roomeo.models.Host;
import project.roomeo.models.Rating;
import project.roomeo.models.enums.AccommodationRequestStatus;
import project.roomeo.models.enums.AccommodationType;
import project.roomeo.models.enums.BookingMethod;
import project.roomeo.models.enums.Payment;
import project.roomeo.service.ServiceUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccommodationViewHolder extends RecyclerView.ViewHolder {
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
    public TextView hostName;
    public TextView hostLastname;
    public Button acceptButton;
    public Button declineButton;
    public Host host;

    public AccommodationViewHolder(View itemView) {
        super(itemView);
        // Inicijalizujte vaše komponente ovde
        name = itemView.findViewById(R.id.name);
        description = itemView.findViewById(R.id.description);
        location = itemView.findViewById(R.id.location);
        wifi = itemView.findViewById(R.id.wifi);
        type = itemView.findViewById(R.id.type);
        kitchen = itemView.findViewById(R.id.kitchen);
        airConditioner = itemView.findViewById(R.id.airConditioner);
        parking = itemView.findViewById(R.id.parking);
        payment = itemView.findViewById(R.id.payment);
        price = itemView.findViewById(R.id.price);
        bookingMethod = itemView.findViewById(R.id.bookingMethod);
        minGuest = itemView.findViewById(R.id.minGuest);
        maxGuest = itemView.findViewById(R.id.maxGuest);
        acceptButton = itemView.findViewById(R.id.accept);
        declineButton = itemView.findViewById(R.id.decline);

        Call<Host> call = ServiceUtils.hostService.getHost("4");

        call.enqueue(new Callback<Host>() {
            @Override
            public void onResponse(Call<Host> call, Response<Host> response) {
                if (response.isSuccessful()) {
                    host = response.body();
                    hostName.setText(host.getFirstName());
                    hostLastname.setText(host.getLastName());

                    } else {
                    onFailure(call, new Throwable("API call failed with status code: " + response.code()));
                }
            }

            @Override
            public void onFailure(Call<Host> call, Throwable t) {
                Log.e("Host", "API call failed: " + t.getMessage());
            }
        });

        hostName = itemView.findViewById(R.id.hostName);
        hostLastname = itemView.findViewById(R.id.hostLastname);
    }

    public void bindData(Accommodation item) {
        name.setText(item.getName());

        description.setText(item.getDescription());
        location.setText(item.getLocation());
        if (item.isWifi()) {
            wifi.setText("Yes");
        } else {
            wifi.setText("No");
        }
        type.setText("Type: " + item.getType());
        if (item.isKitchen()) {
            kitchen.setText("Yes");
        } else {
            kitchen.setText("No");
        }
        if (item.isAirConditioner()) {
            airConditioner.setText("Yes");
        } else {
            airConditioner.setText("No");
        }
        if (item.isParking()) {
            parking.setText("Yes");
        } else {
            parking.setText("No");
        }
        payment.setText(item.getPayment().getDisplayName());
        price.setText(String.valueOf(item.getPrice()));
        bookingMethod.setText(item.getBookingMethod().getDisplayName());
        minGuest.setText(String.valueOf(item.getMinGuest()));
        maxGuest.setText(String.valueOf(item.getMaxGuest()));
    }
}