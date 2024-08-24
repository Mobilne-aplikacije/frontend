package project.roomeo.components.host;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import project.roomeo.R;
import project.roomeo.components.ImageSliderAdapter;
import project.roomeo.models.Accommodation;
import project.roomeo.models.Rating;

public class HostAccomodationDetailsFragment extends Fragment implements OnMapReadyCallback {

    private Accommodation accommodation;
    public TextView name;
    public TextView description;
    private ImageView wifiIcon;
    private ImageView kitchenIcon;
    private ImageView airConditionerIcon;
    private ImageView parkingIcon;
    public TextView price;
    public TextView guestCapacity;
    public RatingBar averageRate;
    private ViewPager2 viewPager;
    private MapView mapView;
    private TextView reviewCount;
    private LinearLayout facilitiesLayout;
    public Long myId;


    public HostAccomodationDetailsFragment() {
    }
    public HostAccomodationDetailsFragment(boolean pending, Long myId) {
        this.myId = myId;
    }

    public void setAccommodation(Accommodation accommodation) {
        this.accommodation = accommodation;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_host_accommodation_details, container, false);
        return view;
    }

    public void setAccommodationRequest(Accommodation request) {
        this.accommodation = request;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        name = getView().findViewById(R.id.accommodationTitle);
        description = getView().findViewById(R.id.accommodationDescription);
        wifiIcon = getView().findViewById(R.id.wifi_icon);
        kitchenIcon = getView().findViewById(R.id.kitchen_icon);
        airConditionerIcon = getView().findViewById(R.id.air_conditioner_icon);
        parkingIcon = getView().findViewById(R.id.parking_icon);
        price = getView().findViewById(R.id.price);
        guestCapacity = getView().findViewById(R.id.guest_capacity);
        averageRate = getView().findViewById(R.id.ratingBar);
        viewPager = view.findViewById(R.id.viewPager);
        reviewCount = getView().findViewById(R.id.reviewCount);
        mapView = view.findViewById(R.id.mapContainer);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        facilitiesLayout = getView().findViewById(R.id.facilitiesLayout);

        // Image Slider setup
        String photosString = accommodation.getPhotos();
        List<String> imageUrls = new ArrayList<>(Arrays.asList(photosString.split(";")));

        ImageSliderAdapter adapter = new ImageSliderAdapter(getContext(), imageUrls);
        viewPager.setAdapter(adapter);

        // Set TextViews with Accommodation details
        name.setText(accommodation.getName());
        averageRate.setRating((float) calculateAverageRate(accommodation.getRatings()));
        description.setText(accommodation.getDescription());
        setFacilityIcon(wifiIcon, accommodation.isWifi());
        setFacilityIcon(kitchenIcon, accommodation.isKitchen());
        setFacilityIcon(airConditionerIcon, accommodation.isAirConditioner());
        setFacilityIcon(parkingIcon, accommodation.isParking());
        price.setText(String.format("$%s / night", accommodation.getPrice()));
        guestCapacity.setText(String.format("Capacity: %s - %s people", accommodation.getMinGuest(), accommodation.getMaxGuest()));
        reviewCount.setText(String.format("(%s Reviews)", Integer.toString(accommodation.getRatings().size())));

        CalendarView calendarView = getView().findViewById(R.id.calendarView);

        List<Long> availableDatesMillis = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        List<String> availableDates = accommodation.getAvailability();

        for (String dateString : accommodation.getAvailability()) {
            try {
                Date date = sdf.parse(dateString);
                if (date != null) {
                    availableDatesMillis.add(date.getTime());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (!availableDatesMillis.isEmpty()) {
            calendarView.setMinDate(Collections.min(availableDatesMillis));
            calendarView.setMaxDate(Collections.max(availableDatesMillis));
        }

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                Calendar selectedDate = Calendar.getInstance();
                selectedDate.set(year, month, dayOfMonth);
                String selectedDateString = sdf.format(selectedDate.getTime());

                if (availableDates.contains(selectedDateString)) {
                    Toast.makeText(getContext(), "Date available!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Date NOT available!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setFacilityIcon(ImageView iconView, boolean available) {
        int iconResource = available ? R.drawable.ic_checkmark : R.drawable.ic_cross;
        iconView.setImageResource(iconResource);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    private double calculateAverageRate(List<Rating> ratings) {
        if (ratings == null || ratings.isEmpty()) {
            return 0;
        }

        double sum = 0;
        for (Rating rating : ratings) {
            sum += rating.getRating();
        }
        return sum / ratings.size();
    }
}
