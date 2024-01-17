package project.roomeo.components.host;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import project.roomeo.R;
import project.roomeo.models.Accommodation;
import project.roomeo.models.enums.AccommodationType;


public class Step1Fragment extends Fragment {

    private EditText editTextName, editTextDescription, editTextLocation, editTextMinGuest, editTextMaxGuest, editTextDeadline;
    private Spinner dropdownButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_step1, container, false);

        editTextName = view.findViewById(R.id.editTextName);
        editTextDescription = view.findViewById(R.id.editTextDescription);
        editTextLocation = view.findViewById(R.id.editTextLocation);
        editTextMinGuest = view.findViewById(R.id.editTextMinGuest);
        editTextMaxGuest = view.findViewById(R.id.editTextMaxGuest);
        editTextDeadline = view.findViewById(R.id.editTextDeadline);
        dropdownButton = view.findViewById(R.id.dropdownButton);

        List<String> options = new ArrayList<>();
        options.add("0%");
        options.add("5%");
        options.add("10%");
        options.add("20%");
        options.add("50%");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, options);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropdownButton.setAdapter(adapter);

        Button next = view.findViewById(R.id.nextButton);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateData()) {
                    Step2Fragment fragment = new Step2Fragment();
                    fragment.setAccommodation(getData());
                    ((HostMainActivity) v.getContext()).loadFragment(fragment);

                }
            }
        });


        return view;
    }

    public boolean validateData() {
        String name = editTextName.getText().toString();
        String description = editTextDescription.getText().toString();
        String location = editTextLocation.getText().toString();
        String minGuest = editTextMinGuest.getText().toString();
        String maxGuest = editTextMaxGuest.getText().toString();
        String deadline = editTextDeadline.getText().toString();

        if (TextUtils.isEmpty(name)) {
            editTextName.setError("Name is required");
            return false;
        }
        if (TextUtils.isEmpty(location)) {
            editTextLocation.setError("Location is required");
            return false;
        }
        if (TextUtils.isEmpty(minGuest)) {
            editTextMinGuest.setError("Minimum number of guests is required");
            return false;
        }
        if (TextUtils.isEmpty(maxGuest)) {
            editTextMaxGuest.setError("Maximum number of guests is required");
            return false;
        }
        if (TextUtils.isEmpty(deadline)) {
            editTextDeadline.setError("Cancellation deadline is required");
            return false;
        }

        return true;
    }

    public Accommodation getData() {
        String name = editTextName.getText().toString();
        String description = editTextDescription.getText().toString();
        String location = editTextLocation.getText().toString();
        String minGuest = editTextMinGuest.getText().toString();
        String maxGuest = editTextMaxGuest.getText().toString();
        String deadline = editTextDeadline.getText().toString();
        String dropdownValue = dropdownButton.getSelectedItem().toString();

        Accommodation accommodation = new Accommodation();
        accommodation.setName(name);
        accommodation.setDescription(description);
        accommodation.setLocation(location);
        accommodation.setMinGuest(Integer.parseInt(minGuest));
        accommodation.setMaxGuest(Integer.parseInt(maxGuest));
        accommodation.setCancellationDeadline(Integer.parseInt(deadline));


        switch (dropdownValue) {
            case "5%":
                accommodation.setPercentage_of_price_increase(5);
                break;
            case "10%":
                accommodation.setPercentage_of_price_increase(10);
                break;
            case "20%":
                accommodation.setPercentage_of_price_increase(20);
                break;
            case "50%":
                accommodation.setPercentage_of_price_increase(50);
                break;
            default:
                accommodation.setPercentage_of_price_increase(0);
                break;
        }

        return accommodation;
    }
}