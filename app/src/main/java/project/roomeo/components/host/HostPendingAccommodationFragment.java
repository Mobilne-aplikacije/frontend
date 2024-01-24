package project.roomeo.components.host;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import project.roomeo.R;
import project.roomeo.models.Accommodation;


public class HostPendingAccommodationFragment extends Fragment {

    private Accommodation accommodation;
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
    public TextView pricee;
    public TextView bookingMethod;
    //    private List<Rating> ratings;
//    private List<String> photos;
    public TextView minGuest;
    public TextView maxGuest;
    //    private AccommodationRequestStatus status;
    public TextView hostName;
    public TextView hostLastname;
    public TextView deadline;
    public TextView priceIncrease;
    public ImageView placeImage;

    public HostPendingAccommodationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_host_pending_accommodation, container, false);
    }

    public void setAccommodationRequest(Accommodation request) {
        this.accommodation = request;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        name = getView().findViewById(R.id.name);
        description = getView().findViewById(R.id.description);
        location = getView().findViewById(R.id.location);
        wifi = getView().findViewById(R.id.wifi);
        type = getView().findViewById(R.id.type);
        kitchen = getView().findViewById(R.id.kitchen);
        airConditioner = getView().findViewById(R.id.airConditioner);
        bookingMethod = getView().findViewById(R.id.bookingMethod);
        parking = getView().findViewById(R.id.parking);
        payment = getView().findViewById(R.id.payment);
        pricee = getView().findViewById(R.id.price);
        minGuest = getView().findViewById(R.id.minGuest);
        maxGuest = getView().findViewById(R.id.maxGuest);
        priceIncrease = getView().findViewById(R.id.priceIncrease);
        deadline = getView().findViewById(R.id.deadline);

        placeImage = getView().findViewById(R.id.placeImage);
        name.setText(accommodation.getName());
        description.setText(accommodation.getDescription());
        location.setText(accommodation.getLocation());
        if (accommodation.isWifi()) {
            wifi.setText("Yes");
        } else {
            wifi.setText("No");
        }
        type.setText("Type: " + accommodation.getType());
        if (accommodation.isKitchen()) {
            kitchen.setText("Yes");
        } else {
            kitchen.setText("No");
        }
        if (accommodation.isAirConditioner()) {
            airConditioner.setText("Yes");
        } else {
            airConditioner.setText("No");
        }
        if (accommodation.isParking()) {
            parking.setText("Yes");
        } else {
            parking.setText("No");
        }
        payment.setText(accommodation.getPayment().getDisplayName());
        pricee.setText(String.valueOf(accommodation.getPrice()));
        bookingMethod.setText(accommodation.getBookingMethod().toString());
        minGuest.setText(String.valueOf(accommodation.getMinGuest()));
        maxGuest.setText(String.valueOf(accommodation.getMaxGuest()));
        deadline.setText(String.valueOf(accommodation.getCancellationDeadline()));
        priceIncrease.setText(accommodation.getPercentage_of_price_increase());

        int drawableResourceId = requireContext().getResources().getIdentifier(accommodation.getPhotos(), "drawable", requireContext().getPackageName());

        if (drawableResourceId != 0) {
            Glide.with(getView())
                    .load(drawableResourceId)
                    .placeholder(R.drawable.ic_email)
                    .error(R.drawable.image3)
                    .centerCrop()
                    .into(placeImage);
        } else {
            // Postavite podrazumevanu sliku ili preduzmite odgovarajuće akcije
            Glide.with(getView())
                    .load(R.drawable.aparment_placeholder)
                    .placeholder(R.drawable.ic_email)
                    .error(R.drawable.image3)
                    .centerCrop()
                    .into(placeImage);
        }
    }
}