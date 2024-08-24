package project.roomeo.components.host;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import project.roomeo.R;
import project.roomeo.components.PhotoAdapterUpd;
import project.roomeo.models.Accommodation;
import project.roomeo.models.EcoFriendlyAmenity;
import project.roomeo.models.enums.EcoAmenity;
import project.roomeo.service.ServiceUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateAccommodationDialogFragment extends DialogFragment {


    private static final int REQUEST_IMAGE_PICK = 2;
    private Accommodation accommodation;
    private HostAccommodationFragment hostAccommodationFragment;
    private List<Bitmap> photoList = new ArrayList<>();
    private PhotoAdapterUpd photoAdapter;
    public UpdateAccommodationDialogFragment(Accommodation accommodation, HostAccommodationFragment hostAccommodationFragment) {
        this.accommodation = accommodation;
        this.hostAccommodationFragment = hostAccommodationFragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setTitle("Update Accommodation");
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_update_accommodation, container, false);

        EditText nameEditText = view.findViewById(R.id.name);
        EditText locationEditText = view.findViewById(R.id.location);
        EditText descriptionEditText = view.findViewById(R.id.description);
        EditText priceEditText = view.findViewById(R.id.price);
        EditText minGuestEditText = view.findViewById(R.id.minGuest);
        EditText maxGuestEditText = view.findViewById(R.id.maxGuest);

        CheckBox checkBoxWifi = view.findViewById(R.id.checkBoxWifi);
        CheckBox checkBoxKitchen = view.findViewById(R.id.checkBoxKitchen);
        CheckBox checkBoxParking = view.findViewById(R.id.checkBoxParking);
        CheckBox checkBoxAC = view.findViewById(R.id.checkBoxAC);

        LinearLayout ecoLayout = view.findViewById(R.id.ecoLayout);
        RecyclerView recyclerViewPhotos = view.findViewById(R.id.recyclerViewPhotos);
        Button buttonAddPhoto = view.findViewById(R.id.buttonAddPhoto);
        buttonAddPhoto.setOnClickListener(v -> {
            // Pokreni aktivnost za izbor slike iz galerije
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, REQUEST_IMAGE_PICK);
        });

        nameEditText.setText(accommodation.getName());
        locationEditText.setText(accommodation.getLocation());
        descriptionEditText.setText(accommodation.getDescription());
        priceEditText.setText(String.valueOf(accommodation.getPrice()));
        minGuestEditText.setText(String.valueOf(accommodation.getMinGuest()));
        maxGuestEditText.setText(String.valueOf(accommodation.getMaxGuest()));

        // Set initial state of checkboxes based on accommodation details
        checkBoxWifi.setChecked(accommodation.isWifi());
        checkBoxKitchen.setChecked(accommodation.isKitchen());
        checkBoxParking.setChecked(accommodation.isParking());
        checkBoxAC.setChecked(accommodation.isAirConditioner());


        photoAdapter = new PhotoAdapterUpd(requireContext(), photoList, position -> {
            // Handle removal of photo
            photoList.remove(position);
            photoAdapter.notifyItemRemoved(position);
        });
        recyclerViewPhotos.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewPhotos.setAdapter(photoAdapter);

        // Load existing photos into the adapter
        loadPhotosIntoAdapter();

        Button buttonCancel = view.findViewById(R.id.buttonCancel);
        buttonCancel.setOnClickListener(v -> dismiss());

        Button buttonSave = view.findViewById(R.id.buttonSave);
        buttonSave.setOnClickListener(v -> {
            // Update accommodation details based on checkbox states
            accommodation.setWifi(checkBoxWifi.isChecked());
            accommodation.setKitchen(checkBoxKitchen.isChecked());
            accommodation.setParking(checkBoxParking.isChecked());
            accommodation.setAirConditioner(checkBoxAC.isChecked());

            accommodation.setName(nameEditText.getText().toString());
            accommodation.setLocation(locationEditText.getText().toString());
            accommodation.setDescription(descriptionEditText.getText().toString());
            try {
                accommodation.setPrice(Integer.parseInt(priceEditText.getText().toString()));
            } catch (NumberFormatException e) {
                Log.e("UpdateAccommodation", "Invalid price format", e);
            }
            try {
                accommodation.setMinGuest(Integer.parseInt(minGuestEditText.getText().toString()));
            } catch (NumberFormatException e) {
                Log.e("UpdateAccommodation", "Invalid minimum guests format", e);
            }
            try {
                accommodation.setMaxGuest(Integer.parseInt(maxGuestEditText.getText().toString()));
            } catch (NumberFormatException e) {
                Log.e("UpdateAccommodation", "Invalid maximum guests format", e);
            }

//            accommodation.setPhotos(photoUrls);
            Call<Accommodation> call = ServiceUtils.adminService.updateAccommodation(accommodation.getId().toString(), accommodation);

            call.enqueue(new Callback<Accommodation>() {
                @Override
                public void onResponse(Call<Accommodation> call, Response<Accommodation> response) {
                    if (response.isSuccessful()) {
                        if (hostAccommodationFragment != null) {
                        }
                        dismiss();
                    } else {
                        Log.e("UpdateAccommodation", "Update failed with status code: " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<Accommodation> call, Throwable t) {
                    Log.e("UpdateAccommodation", "Update failed: " + t.getMessage());
                }
            });

            dismiss();
        });


        return view;
    }



    @Override
    public void onStart() {
        super.onStart();
        // Adjust the dialog width and height
        if (getDialog() != null) {
            Window window = getDialog().getWindow();
            if (window != null) {
                WindowManager.LayoutParams params = window.getAttributes();
                params.width = WindowManager.LayoutParams.MATCH_PARENT; // Set width to match parent
                params.height = WindowManager.LayoutParams.WRAP_CONTENT; // Set height to wrap content
                params.gravity = Gravity.CENTER; // Center the dialog
                window.setAttributes(params);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == getActivity().RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), selectedImageUri);
                photoList.add(bitmap);
                photoAdapter.notifyItemInserted(photoList.size() - 1);
            } catch (IOException e) {
                Log.e("UpdateAccommodation", "Failed to load image", e);
            }
        }
    }


    private String convertBitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }


    private void loadPhotosIntoAdapter() {
        // Pretpostavljamo da getPhotos vraća string u formatu "image1;image2;image3"
        String photosString = accommodation.getPhotos();

        // Razdvojite string na osnovu tačke-zareza
        List<String> photoUrls = new ArrayList<>();
        if (photosString != null && !photosString.isEmpty()) {
            String[] photoArray = photosString.split(";");
            for (String photo : photoArray) {
                photoUrls.add(photo.trim()); // Dodajte svaki deo u listu
            }
        }

        photoList.clear();
        // Obradite svaki URL ili base64 string u listi
        for (String photoUrl : photoUrls) {
            if (photoUrl.startsWith("http")) {
                // Ako koristite URL-ove
                loadBitmapFromUrl(photoUrl);
            } else {
                // Ako koristite base64 stringove
                Bitmap bitmap = convertStringToBitmap(photoUrl);
                if (bitmap != null) {
                    photoList.add(bitmap);
                    photoAdapter.notifyItemInserted(photoList.size() - 1);
                }
            }
        }
    }


    private void loadBitmapFromUrl(String url) {
        Glide.with(this)
                .asBitmap()
                .load(url)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        photoList.add(resource);
                        photoAdapter.notifyItemInserted(photoList.size() - 1);
                    }

                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        super.onLoadFailed(errorDrawable);
                        Log.e("UpdateAccommodation", "Failed to load image from URL: " + url);
                    }
                });
    }

    private Bitmap convertStringToBitmap(String encodedString) {
        try {
            byte[] decodedString = Base64.decode(encodedString, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        } catch (IllegalArgumentException e) {
            Log.e("UpdateAccommodation", "Failed to decode base64 string to bitmap", e);
            return null;
        }
    }


}

