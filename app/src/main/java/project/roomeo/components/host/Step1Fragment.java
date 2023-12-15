package project.roomeo.components.host;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import project.roomeo.R;
import project.roomeo.models.Accommodation;


public class Step1Fragment extends Fragment {

    private EditText editTextName, editTextDescription, editTextLocation, editTextMinGuest, editTextMaxGuest;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_step1, container, false);

        editTextName = view.findViewById(R.id.editTextName);
        editTextDescription = view.findViewById(R.id.editTextDescription);
        editTextLocation = view.findViewById(R.id.editTextLocation);
        editTextMinGuest = view.findViewById(R.id.editTextMinGuest);
        editTextMaxGuest = view.findViewById(R.id.editTextMaxGuest);

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

        return true;
    }

    public Accommodation getData() {
        String name = editTextName.getText().toString();
        String description = editTextDescription.getText().toString();
        String location = editTextLocation.getText().toString();
        String minGuest = editTextMinGuest.getText().toString();
        String maxGuest = editTextMaxGuest.getText().toString();

        Accommodation accommodation = new Accommodation();
        accommodation.setName(name);
        accommodation.setDescription(description);
        accommodation.setLocation(location);
        accommodation.setMinGuest(Integer.parseInt(minGuest));
        accommodation.setMaxGuest(Integer.parseInt(maxGuest));

        return accommodation;
    }
}