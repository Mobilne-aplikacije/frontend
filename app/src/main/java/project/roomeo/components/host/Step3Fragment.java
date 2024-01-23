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
import project.roomeo.models.Accommodation;
import project.roomeo.service.ServiceUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Step3Fragment extends Fragment {
    private static final int REQUEST_IMAGE_PICK = 2;
    public static int counter = 0;

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
        imageViewPhoto1 = view.findViewById(R.id.imageViewPhoto1);
        imageViewPhoto2 = view.findViewById(R.id.imageViewPhoto2);
        imageViewPhoto3 = view.findViewById(R.id.imageViewPhoto3);

        buttonAddPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        Button next = view.findViewById(R.id.nextButton);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call<Accommodation> call = ServiceUtils.hostService.addAccommodation(accommodation);

                call.enqueue(new Callback<Accommodation>() {
                    @Override
                    public void onResponse(Call<Accommodation> call, Response<Accommodation> response) {
                        if(!response.isSuccessful()) return;
                        Log.d("Success" ,"Successfully added accommodation");

                        HostAccommodationsFragment fragment = new HostAccommodationsFragment();
                        ((HostMainActivity) v.getContext()).loadFragment(fragment);
                    }

                    @Override
                    public void onFailure(Call<Accommodation> call, Throwable t) {
                        Log.d("FAIL", t.getMessage());
                    }
                });
            }
        });

        return view;
    }
    public Accommodation getData() {

        String image1 = imageViewPhoto1.toString();
        Log.i("photos", image1);
//        String image2 = imageViewPhoto2.toString();
//        String image3 = imageViewPhoto3.toString();
//
//        List<String> photos = new ArrayList<String>();
//        photos.add(image1);
//        photos.add(image2);
//        photos.add(image3);
        accommodation.setPhotos(image1);

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
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_PICK) {
                if (data != null) {
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireContext().getContentResolver(), data.getData());
                        setPic(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void setPic(Bitmap bitmap) {
        counter ++;
        if (counter == 1){
            imageViewPhoto1.setImageBitmap(bitmap);
        }
        if (counter == 2){
            imageViewPhoto2.setImageBitmap(bitmap);
        }
        if (counter >= 3){
            imageViewPhoto3.setImageBitmap(bitmap);
        }
    }

}