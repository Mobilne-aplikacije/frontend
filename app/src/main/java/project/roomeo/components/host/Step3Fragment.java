package project.roomeo.components.host;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import project.roomeo.R;
import project.roomeo.components.PhotoAdapter;
import project.roomeo.models.Accommodation;
import project.roomeo.service.ServiceUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Step3Fragment extends Fragment {
    private static final int REQUEST_IMAGE_PICK = 2;
    private List<Bitmap> imageBitmaps = new ArrayList<>();
    public static int counter = 0;
    private PhotoAdapter photoAdapter;
    private RecyclerView recyclerView;

    private ImageView imageViewPhoto1;
    private ImageView imageViewPhoto2;
    private ImageView imageViewPhoto3;
    private Accommodation accommodation;


    public void setAccommodation(Accommodation accommodation) {
        this.accommodation = accommodation;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_step3, container, false);

        Button buttonAddPhoto = view.findViewById(R.id.buttonAddPhoto);
        recyclerView = view.findViewById(R.id.recyclerViewPhotos);

        photoAdapter = new PhotoAdapter(imageBitmaps);
        recyclerView.setAdapter(photoAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        buttonAddPhoto.setOnClickListener(v -> openGallery());

        Button next = view.findViewById(R.id.nextButton);
        next.setOnClickListener(v -> {
            String photos = getPhotosString();
            accommodation.setPhotos(photos);
            Log.i("addaccommodation", accommodation.toString());
            Call<Accommodation> call = ServiceUtils.hostService.addAccommodation(accommodation);

            call.enqueue(new Callback<Accommodation>() {
                @Override
                public void onResponse(Call<Accommodation> call, Response<Accommodation> response) {
                    if (!response.isSuccessful()) return;
                    Log.d("Success", "Successfully added accommodation");

                    HostAccommodationsFragment fragment = new HostAccommodationsFragment();
                    ((HostMainActivity) v.getContext()).loadFragment(fragment);
                }

                @Override
                public void onFailure(Call<Accommodation> call, Throwable t) {
                    Log.d("FAIL", t.getMessage());
                }
            });
        });

        return view;
    }
    public Accommodation getData() {

        String image1 = imageViewPhoto1.toString();
        Log.i("photos", image1);
        accommodation.setPhotos("apartmentnew.jpg");
        Log.i("addaccommodation",accommodation.toString());
        return accommodation;
    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, REQUEST_IMAGE_PICK);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_IMAGE_PICK && data != null) {
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireContext().getContentResolver(), data.getData());
                addImageToList(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void addImageToList(Bitmap bitmap) {
        imageBitmaps.add(bitmap);
        photoAdapter.notifyDataSetChanged();
    }

    private String getPhotosString() {
        StringBuilder photosBuilder = new StringBuilder();
        for (int i = 0; i < imageBitmaps.size(); i++) {
            photosBuilder.append("image").append(i + 1);
            if (i < imageBitmaps.size() - 1) {
                photosBuilder.append(";");
            }
        }
        return photosBuilder.toString();
    }

    private void setPic(Bitmap bitmap) {
        counter ++;
        if (counter == 1){
            imageViewPhoto1.setImageBitmap(bitmap);
        }
//        if (counter == 2){
//            imageViewPhoto2.setImageBitmap(bitmap);
//        }
//        if (counter >= 3){
//            imageViewPhoto3.setImageBitmap(bitmap);
//        }
    }

}