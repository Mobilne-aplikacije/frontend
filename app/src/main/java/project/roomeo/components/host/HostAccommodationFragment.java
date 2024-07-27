package project.roomeo.components.host;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.activity.OnBackPressedCallback;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.textview.MaterialTextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import project.roomeo.R;
import project.roomeo.components.ImageSliderAdapter;
import project.roomeo.models.Accommodation;
import project.roomeo.models.EcoFriendlyAmenity;
import project.roomeo.models.Rating;
import project.roomeo.models.enums.RatingType;
import project.roomeo.service.ServiceUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HostAccommodationFragment extends Fragment {

    private Accommodation accommodation;
    public TextView name;
    public TextView description;
    public TextView location;
    public TextView wifi;
    public TextView type;
    public TextView kitchen;
    public TextView airConditioner;
    public TextView parking;
    public TextView payment;
    public TextView pricee;
    public TextView minGuest;
    private boolean pending;
    public TextView averageRate;
    public ImageView placeImage;
    private ViewPager2 viewPager;

    public HostAccommodationFragment() {
        this.pending = false;
    }

    public HostAccommodationFragment(boolean pending) {
        this.pending = pending;
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
            View view = inflater.inflate(R.layout.fragment_host_accommodation, container, false);

            Button report = view.findViewById(R.id.edit);
            report.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Long accommodationId = accommodation.getId();
                    AccommodationReportFragment fragment = new AccommodationReportFragment(accommodationId);
                    ((HostMainActivity) v.getContext()).loadFragment(fragment);
                }
            });

            Button updateButton = view.findViewById(R.id.updateAccommodationButton);
            updateButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UpdateAccommodationDialogFragment dialog = new UpdateAccommodationDialogFragment(accommodation, HostAccommodationFragment.this);
                    dialog.show(getParentFragmentManager(), "UpdateAccommodationDialog");
                }
            });

            return view;
        }
    }

    public void setAccommodationRequest(Accommodation request) {
        this.accommodation = request;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        name = getView().findViewById(R.id.name);
        description = getView().findViewById(R.id.description);
        location = getView().findViewById(R.id.location);
        wifi = getView().findViewById(R.id.wifi);
        kitchen = getView().findViewById(R.id.kitchen);
        airConditioner = getView().findViewById(R.id.airConditioner);
        parking = getView().findViewById(R.id.parking);
        pricee = getView().findViewById(R.id.price);
        minGuest = getView().findViewById(R.id.minGuest);
        averageRate = getView().findViewById(R.id.averageRate);
        viewPager = view.findViewById(R.id.viewPager);

        String photosString = accommodation.getPhotos();
        List<String> imageUrls = new ArrayList<>(Arrays.asList(photosString.split(";")));

        ImageSliderAdapter adapter = new ImageSliderAdapter(getContext(), imageUrls);
        viewPager.setAdapter(adapter);

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
        pricee.setText(accommodation.getPrice() + "$");
        minGuest.setText("Number of guests: " + accommodation.getMinGuest() + "-" + accommodation.getMaxGuest());


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


        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Zamenite `DefinedFragment` sa fragmentom na koji želite da se vratite
                Fragment fragment = new HostAccommodationsFragment();
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.guest_content, fragment)
                        .addToBackStack(null)
                        .commit();
            }
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