package project.roomeo.components.guest;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.textview.MaterialTextView;

import java.text.DecimalFormat;
import java.util.List;

import project.roomeo.R;
import project.roomeo.components.host.AccommodationRatingsFragment;
import project.roomeo.components.host.HostAccommodationsFragment;
import project.roomeo.components.host.HostMainActivity;
import project.roomeo.components.host.HostRatingsFragment;
import project.roomeo.models.Accommodation;
import project.roomeo.models.EcoFriendlyAmenity;
import project.roomeo.models.Rating;
import project.roomeo.models.Reservation;
import project.roomeo.models.enums.RatingType;
import project.roomeo.models.enums.ReservationRequestStatus;
import project.roomeo.service.ServiceUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GuestAccommodationFragment extends Fragment {

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
    private boolean pending;
    public TextView priceIncrease;
    public TextView averageRate;
    public double average;
    public double accommodationRates;
    public boolean alreadyInFav;
    public Long myId;
    public ImageView placeImage;


    public GuestAccommodationFragment() {
        this.pending = false;
    }

    public GuestAccommodationFragment(boolean pending, Long myId) {
        this.pending = pending;
        this.myId = myId;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (pending) {
            return inflater.inflate(R.layout.fragment_host_pending_accommodation, container, false);
        } else {
            View view = inflater.inflate(R.layout.fragment_guest_accommodation, container, false);

            return view;
        }
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
//        type = getView().findViewById(R.id.type);
        kitchen = getView().findViewById(R.id.kitchen);
        airConditioner = getView().findViewById(R.id.airConditioner);
//        bookingMethod = getView().findViewById(R.id.bookingMethod);
        parking = getView().findViewById(R.id.parking);
//        payment = getView().findViewById(R.id.payment);
        pricee = getView().findViewById(R.id.price);
        minGuest = getView().findViewById(R.id.minGuest);
//        maxGuest = getView().findViewById(R.id.maxGuest);
//        priceIncrease = getView().findViewById(R.id.priceIncrease);
        averageRate = getView().findViewById(R.id.averageRate);
        placeImage = getView().findViewById(R.id.placeImage);

        name.setText(accommodation.getName());
        averageRate.setText(String.valueOf(accommodation.getRate()));
        description.setText(accommodation.getDescription());
        location.setText(accommodation.getLocation());
        if (accommodation.isWifi()) {
            wifi.setText("Yes");
        } else {
            wifi.setText("No");
        }
//        type.setText("Type: " + accommodation.getType());
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
//        payment.setText(accommodation.getPayment().getDisplayName());
        pricee.setText(String.valueOf(accommodation.getPrice())+"$");
//        bookingMethod.setText(accommodation.getBookingMethod().toString());
        minGuest.setText("Number of guests: " +String.valueOf(accommodation.getMinGuest())+"-"+accommodation.getMaxGuest());
//        maxGuest.setText(String.valueOf(accommodation.getMaxGuest()));
//        priceIncrease.setText(String.valueOf(accommodation.getPercentage_of_price_increase()) + "%");

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

        LinearLayout ecoLayout = getView().findViewById(R.id.ecoLayout);
        ecoLayout.removeAllViews();
        for (EcoFriendlyAmenity ecoAmenity : accommodation.getEcoFriendlyAmenities()) {
            MaterialTextView textView = new MaterialTextView(requireContext());
            textView.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            textView.setText(addSpacesToCamelCase(ecoAmenity.getName().toString()));
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            textView.setPadding((int) getResources().getDimension(R.dimen.padding_8dp), 0, 0, 0);

            ecoLayout.addView(textView);
        }


        Button reserve = getView().findViewById(R.id.reserve);
        reserve.setOnClickListener(v -> {
            Reservation reservation = new Reservation(accommodation.getId().intValue(),"07/20/2024","07/22/2024",ReservationRequestStatus.PENDING,this.myId.intValue(),accommodation.getPrice());

            Call<Reservation> call2 = ServiceUtils.reservationService.addReservation(reservation);

            call2.enqueue(new Callback<Reservation>() {
                @Override
                public void onResponse(Call<Reservation> call, Response<Reservation> response) {
                    if(!response.isSuccessful()) return;
                    Log.d("Success" ,"Successfully added reservation");
                }

                @Override
                public void onFailure(Call<Reservation> call, Throwable t) {
                    Log.d("FAIL", t.getMessage());
                }
            });

            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
            builder.setMessage("poslali ste zahtev za rezervaciju, datumi:")
                    .setCancelable(false)
                    .setPositiveButton("OK", (dialog, id) -> {
                        GuestHomeFragment fragment = new GuestHomeFragment();
                        ((GuestMainActivity) v.getContext()).loadFragment(fragment);
                    });

            AlertDialog alert = builder.create();
            alert.show();
        });



    }

    private String addSpacesToCamelCase(String text) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (Character.isUpperCase(c) && i > 0) {
                builder.append(" ");
            }
            builder.append(c);
        }
        return builder.toString();
    }

}

