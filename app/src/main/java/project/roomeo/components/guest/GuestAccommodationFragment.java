package project.roomeo.components.guest;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.List;

import project.roomeo.R;
import project.roomeo.components.host.AccommodationRatingsFragment;
import project.roomeo.components.host.HostMainActivity;
import project.roomeo.components.host.HostRatingsFragment;
import project.roomeo.models.Accommodation;
import project.roomeo.models.Rating;
import project.roomeo.models.enums.RatingType;
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
    public TextView deadline;
    public TextView priceIncrease;
    public TextView averageRate;
    public double average;
    public double accommodationRates;
    public boolean alreadyInFav;
    public Long myId;

    public GuestAccommodationFragment() {
        this.pending = false;
    }
    public GuestAccommodationFragment(boolean pending,Long myId) {
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
        if (pending){
            return inflater.inflate(R.layout.fragment_host_pending_accommodation, container, false);
        }else{
            View view = inflater.inflate(R.layout.fragment_guest_accommodation, container, false);
            Button hostRatings = view.findViewById(R.id.hostRatings);
            hostRatings.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int hostId = accommodation.getHostId();
                    GHostRatingsFragment fragment = new GHostRatingsFragment((long) hostId, (long) accommodation.getId());
                    ((GuestMainActivity) v.getContext()).loadFragment(fragment);
                }
            });
            Button accommodationRatings = view.findViewById(R.id.accommodationRatings);
            accommodationRatings.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Long accommodationId = accommodation.getId();
                    GAccommodationRatingsFragment fragment = new GAccommodationRatingsFragment(accommodationId, Long.valueOf(accommodation.getHostId()));
                    ((GuestMainActivity) v.getContext()).loadFragment(fragment);
                }
            });
            alreadyInFav = false;


            Button addToFav = view.findViewById(R.id.favorites);
            if (!alreadyInFav) {
                addToFav.setVisibility(View.VISIBLE);
            } else {
                addToFav.setVisibility(View.GONE);
            }
            addToFav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Call<Void> call = ServiceUtils.guestService.addFavorite(myId,(long) accommodation.getId());
                    call.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {

                            if (response.isSuccessful()) {

                                GuestAccommodationFragment fragment = new GuestAccommodationFragment(pending,myId);
                                fragment.setAccommodationRequest(accommodation);
                                ((GuestMainActivity) v.getContext()).loadFragment(fragment);
                                Toast.makeText(view.getContext(), "Added to favorites.", Toast.LENGTH_SHORT).show();

                            } else {
                                onFailure(call, new Throwable("API call failed with status code: " + response.code()));
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                            Log.e("RatingAdapter", "API call failed: " + t.getMessage());

                        }
                    });
                }
            });
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
        averageRate = getView().findViewById(R.id.averageRate);

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
        priceIncrease.setText(String.valueOf(accommodation.getPercentage_of_price_increase())+"%");

        Call<List<Rating>> call = ServiceUtils.ratingService.getAllRatings();
        call.enqueue(new Callback<List<Rating>>() {
            @Override
            public void onResponse(Call<List<Rating>> call, Response<List<Rating>> response) {
                if (response.isSuccessful()) {
                    List<Rating> list = response.body();
                    if (list != null) {
                        for (int i = 0; i < list.size(); i++) {
                            if (list.get(i).getType() == RatingType.ACCOMMODATION && list.get(i).getAccommodationId()==accommodation.getId()){
                                average+=list.get(i).getRating();
                                accommodationRates++;
                                Log.i("PROSEK", String.valueOf(average)+"..."+String.valueOf(accommodationRates));
                            }
                        }
                        Log.i("prosekk", averageRate.toString());

                        DecimalFormat decimalFormat = new DecimalFormat("#.##");
                        String formattedAverage = decimalFormat.format(average/accommodationRates);
                        double formattedDouble = Double.parseDouble(formattedAverage);
                        averageRate.setText(String.valueOf(formattedDouble));

                    }
                } else {
                    onFailure(call, new Throwable("API call failed with status code: " + response.code()));
                }
            }

            @Override
            public void onFailure(Call<List<Rating>> call, Throwable t) {
                Log.e("Rating", "API call failed: " + t.getMessage());
            }
        });
    }
}