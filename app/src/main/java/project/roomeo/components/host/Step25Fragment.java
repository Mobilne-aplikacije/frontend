package project.roomeo.components.host;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import project.roomeo.R;
import project.roomeo.models.Accommodation;
import project.roomeo.models.EcoFriendlyAmenity;
import project.roomeo.models.enums.AccommodationRequestStatus;
import project.roomeo.models.enums.EcoAmenity;

public class Step25Fragment extends Fragment {
    private Accommodation accommodation;
    private Long myId;
    private LinearLayout checkboxContainer;

    public void setAccommodation(Accommodation accommodation) {
        this.accommodation = accommodation;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_step25, container, false);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("preferences", Context.MODE_PRIVATE);
        String myEmail = sharedPreferences.getString("pref_email", "");
        myId = sharedPreferences.getLong("pref_id", 0L);

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

        Button next = view.findViewById(R.id.nextButton);
        next.setOnClickListener(v -> {
            Step3Fragment fragment = new Step3Fragment();
            fragment.setAccommodation(getData());
            ((HostMainActivity) v.getContext()).loadFragment(fragment);
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

    public Accommodation getData() {

        List<EcoFriendlyAmenity> selectedAmenities = new ArrayList<>();
        for (int i = 0; i < checkboxContainer.getChildCount(); i++) {
            View view = checkboxContainer.getChildAt(i);
            if (view instanceof CheckBox) {
                CheckBox checkBox = (CheckBox) view;
                if (checkBox.isChecked()) {

                    // Pronađi odgovarajuću EcoFriendlyAmenity na osnovu teksta CheckBox-a
                    EcoFriendlyAmenity amenity = new EcoFriendlyAmenity();
                    amenity.setName(EcoAmenity.valueOf(checkBox.getText().toString().replace(" ", "")));
                    selectedAmenities.add(amenity);
                }
            }
        }
        // Postavi izabrane ekološke sadržaje u accommodation objekat
        accommodation.setEcoFriendlyAmenities(selectedAmenities);

        return accommodation;
    }
}