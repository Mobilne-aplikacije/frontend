package project.roomeo.components.guest;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import project.roomeo.R;
import project.roomeo.components.host.HostAccommodationsFragment;
import project.roomeo.components.host.HostMainActivity;
import project.roomeo.components.host.HostReservationsFragment;
import project.roomeo.models.Accommodation;
import project.roomeo.models.EcoFriendlyAmenity;
import project.roomeo.models.Rating;
import project.roomeo.models.Reservation;
import project.roomeo.models.enums.EcoAmenity;
import project.roomeo.models.enums.RatingStatus;
import project.roomeo.service.ServiceUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RateAccommodationFragment extends Fragment {

    public int accommodationId;
    public Long reservationId;
    private LinearLayout checkboxContainer;

    public RateAccommodationFragment(int accommodationId, Long reservationId) {
        this.accommodationId = accommodationId;
        this.reservationId = reservationId;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rate_accommodation, container, false);

        checkboxContainer = view.findViewById(R.id.checkboxContainer);

        for (EcoAmenity amenity : EcoAmenity.values()) {
            CheckBox checkBox = new CheckBox(requireContext());
            checkBox.setText(addSpacesToCamelCase(amenity.name()));
            checkBox.setId(View.generateViewId());
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            layoutParams.setMargins(16, 16, 16, 0);
            checkBox.setLayoutParams(layoutParams);
            checkboxContainer.addView(checkBox);
        }

        Button rate = view.findViewById(R.id.rate);
        rate.setOnClickListener(v -> {

            int checkedCount = 0;
            for (int i = 0; i < checkboxContainer.getChildCount(); i++) {
                View view2 = checkboxContainer.getChildAt(i);
                if (view2 instanceof CheckBox) {
                    CheckBox checkBox = (CheckBox) view2;
                    if (checkBox.isChecked()) {
                        checkedCount++;
                    }
                }
            }

            double ratingValue = checkedCount;

            Rating rating = new Rating(ratingValue, RatingStatus.ACCEPTED,this.accommodationId);
            System.out.println("rating "+ rating);

            Call<Rating> call = ServiceUtils.ratingService.addRating(rating);

            call.enqueue(new Callback<Rating>() {
                @Override
                public void onResponse(Call<Rating> call, Response<Rating> response) {
                    if(!response.isSuccessful()) return;
                    Log.d("Success" ,rating.toString());

                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                    builder.setMessage("na osnovu vaseg iskustva hotel je dobio ocenu 3.5")
                            .setCancelable(false)
                            .setPositiveButton("OK", (dialog, id) -> {

                                Call<Reservation> call2 = ServiceUtils.reservationService.declineReservationRequest(reservationId.toString());
                                call2.enqueue(new Callback<Reservation>() {
                                    @Override
                                    public void onResponse(@NonNull Call<Reservation> call, @NonNull Response<Reservation> response) {

                                        if (response.isSuccessful()) {
                                            Log.d("ocena" ,"data ocena i obrisan smestaj");

                                        } else {
                                            onFailure(call, new Throwable("API call failed with status code: " + response.code()));
                                        }
                                    }

                                    @Override
                                    public void onFailure(@NonNull Call<Reservation> call, @NonNull Throwable t) {
                                        Log.e("ReservationAdapter", "API call failed: " + t.getMessage());

                                    }
                                });


                                GuestNotificationsFragment fragment = new GuestNotificationsFragment();
                                ((GuestMainActivity) v.getContext()).loadFragment(fragment);
                            });


                    AlertDialog alert = builder.create();
                    alert.show();
                }

                @Override
                public void onFailure(Call<Rating> call, Throwable t) {
                    Log.d("FAIL", t.getMessage());
                }
            });
        });

        return view;
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