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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private Map<EcoAmenity, Double> ecoAmenityWeights;

    public RateAccommodationFragment(int accommodationId, Long reservationId) {
        this.accommodationId = accommodationId;
        this.reservationId = reservationId;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ecoAmenityWeights = new HashMap<>();
        ecoAmenityWeights.put(EcoAmenity.SolarPanels, 1.0);
        ecoAmenityWeights.put(EcoAmenity.SmartThermostats, 1.5);
        ecoAmenityWeights.put(EcoAmenity.EnergyEfficientDevices, 1.2);
        ecoAmenityWeights.put(EcoAmenity.ZeroWastePolicy, 2.0);
        ecoAmenityWeights.put(EcoAmenity.RecyclingBins, 1.0);
        ecoAmenityWeights.put(EcoAmenity.LimitedUseOfSingleUsePlastics, 1.3);
        ecoAmenityWeights.put(EcoAmenity.WaterSavingFaucetsShowersToilets, 1.4);
        ecoAmenityWeights.put(EcoAmenity.NaturalNonToxicMaterials, 1.5);
        ecoAmenityWeights.put(EcoAmenity.GreenSpacesAndGardens, 1.6);
        ecoAmenityWeights.put(EcoAmenity.FreeBicyclesForGuests, 1.3);
        ecoAmenityWeights.put(EcoAmenity.ElectricVehicleChargers, 1.4);

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

            double ratingValue = calculateRating();

            Rating rating = new Rating(ratingValue, RatingStatus.ACCEPTED,this.accommodationId);
            System.out.println("rating "+ rating);

            Call<Rating> call = ServiceUtils.ratingService.addRating(rating);

            call.enqueue(new Callback<Rating>() {
                @Override
                public void onResponse(Call<Rating> call, Response<Rating> response) {
                    if(!response.isSuccessful()) return;
                    Log.d("Success" ,rating.toString());

                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                    builder.setMessage("Based on your experience, the hotel received a rating of " +ratingValue+ ". Thank you!")
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

    private double calculateRating() {
        double totalWeight = 0.0;
        double selectedWeight = 0.0;

        for (int i = 0; i < checkboxContainer.getChildCount(); i++) {
            View view2 = checkboxContainer.getChildAt(i);
            if (view2 instanceof CheckBox) {
                CheckBox checkBox = (CheckBox) view2;
                EcoAmenity amenity = EcoAmenity.valueOf(removeSpacesFromCamelCase(checkBox.getText().toString()));
                if (checkBox.isChecked() && ecoAmenityWeights.containsKey(amenity)) {
                    selectedWeight += ecoAmenityWeights.get(amenity);
                }
                totalWeight += ecoAmenityWeights.getOrDefault(amenity, 0.0);
            }
        }

        double ratingValue = (totalWeight > 0) ? (selectedWeight / totalWeight) * 4 + 1 : 1.0; // Scale to 1.0 to 5.0

        ratingValue = Math.min(Math.max(ratingValue, 1.0), 5.0);

        DecimalFormat df = new DecimalFormat("#.#");
        String roundedRating = df.format(ratingValue);

        return Double.parseDouble(roundedRating);
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

    private String removeSpacesFromCamelCase(String text) {
        return text.replace(" ", "");
    }

}