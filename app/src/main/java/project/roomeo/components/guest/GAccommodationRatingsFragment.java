package project.roomeo.components.guest;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import project.roomeo.R;
import project.roomeo.components.host.AccommodationRatingAdapter;
import project.roomeo.models.Rating;
import project.roomeo.models.Reservation;
import project.roomeo.models.enums.RatingStatus;
import project.roomeo.models.enums.RatingType;
import project.roomeo.service.ServiceUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class GAccommodationRatingsFragment extends Fragment {

    private RecyclerView recyclerView;
    private GAccommodationRatingAdapter ratingAdapter;
    private Long myId;
    private Long accommodationId;
    private Long hostId;
    private Button addRating;
    public TextView averageRate;
    public boolean hadRes;
    private static final String CHANNEL_ID = "MyChannelId";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_g_accommodation_ratings, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        hadRes = false;

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("preferences", Context.MODE_PRIVATE);
        myId = sharedPreferences.getLong("pref_id", 0L);
        Log.e("PROVERA ID", "provera id: " + myId);

        addRating = view.findViewById(R.id.add);

        Call<List<Reservation>> call = ServiceUtils.reservationService.getGuestReservations(myId.toString());
        call.enqueue(new Callback<List<Reservation>>() {
            @Override
            public void onResponse(@NonNull Call<List<Reservation>> call, @NonNull Response<List<Reservation>> response) {
                if (response.isSuccessful()) {
                    List<Reservation> list = response.body();
                    if (list != null) {
                        for (int i = 0; i < list.size(); i++) {
                            if (list.get(i).getGuestId() == myId.intValue()) {
                                hadRes = true;
                                break;
                            }
                        }
                        if (hadRes) {
                            addRating.setVisibility(View.VISIBLE);
                        } else {
                            addRating.setVisibility(View.GONE);
                        }


                        Log.i("true", String.valueOf(hadRes));
                    } else {
                        onFailure(call, new Throwable("API call failed with status code: " + response.code()));
                    }
                }
            }
            @Override
            public void onFailure(@NonNull Call<List<Reservation>> call, @NonNull Throwable t) {
                Log.e("Report", "API call failed: " + t.getMessage());
            }
        });

        addRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Kreiranje dijaloga
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                LayoutInflater inflater = requireActivity().getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.rating_dialog, null);
                builder.setView(dialogView);

                // Pronalaženje polja za unos ocene i komentara
                EditText ratingInput = dialogView.findViewById(R.id.ratingInput);
                EditText commentInput = dialogView.findViewById(R.id.commentInput);

                // Postavljanje dugmadi u dijalogu
                builder.setPositiveButton("Potvrdi", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Dobijanje vrednosti iz polja za unos
                        String ratingText = ratingInput.getText().toString();
                        String commentText = commentInput.getText().toString();

                        // Provera da li su polja popunjena
                        if (!ratingText.isEmpty() && !commentText.isEmpty()) {
                            // Konverzija ocene u int
                            int ratingValue = Integer.parseInt(ratingText);

                            // Dobijanje trenutnog datuma
                            Date currentDate = new Date();
                            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
                            String formattedDate = dateFormat.format(currentDate);

                            // Kreiranje objekta Rating sa unetim vrednostima
                            Rating rating = new Rating(ratingValue, commentText, RatingStatus.PENDING, RatingType.HOST, accommodationId.intValue(), myId.intValue(), formattedDate);

                            // Pozivanje API poziva
                            Call<Rating> call = ServiceUtils.ratingService.addRating(rating);
                            call.enqueue(new Callback<Rating>() {
                                @Override
                                public void onResponse(@NonNull Call<Rating> call, @NonNull Response<Rating> response) {
                                    if (response.isSuccessful()) {
                                        Log.i("host id acc id", hostId.toString() + " m " + accommodationId.toString());
                                        GHostRatingsFragment fragment = new GHostRatingsFragment(hostId, accommodationId);
                                        ((GuestMainActivity) view.getContext()).loadFragment(fragment);
                                        showNotification();
                                        Toast.makeText(view.getContext(), "Rating added.", Toast.LENGTH_SHORT).show();
                                    } else {
                                        onFailure(call, new Throwable("API call failed with status code: " + response.code()));
                                    }
                                }

                                @Override
                                public void onFailure(@NonNull Call<Rating> call, @NonNull Throwable t) {
                                    Log.e("Report", "API call failed: " + t.getMessage());
                                }
                            });
                        } else {
                            Toast.makeText(getActivity(), "Enter rate and comment", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                builder.setNegativeButton("Otkaži", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                // Prikaz dijaloga
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
        return view;
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getAccommodationRatingsList(accommodationId);
    }

    private void getAccommodationRatingsList(Long accommodationId) {

        Call<List<Rating>> call = ServiceUtils.ratingService.getAllAccommodationRatings(accommodationId.toString());

        call.enqueue(new Callback<List<Rating>>() {
            @Override
            public void onResponse(Call<List<Rating>> call, Response<List<Rating>> response) {
                if (response.isSuccessful()) {
                    List<Rating> list = response.body();
                    if (list != null) {
                        List<Rating> acceptedRatings = new ArrayList<>();
                        for (Rating r : list) {
                            if (r.getStatus() == RatingStatus.ACCEPTED) {
                                acceptedRatings.add(r);
                            }
                        }
                        ratingAdapter = new GAccommodationRatingAdapter(acceptedRatings, hostId, myId);
                        recyclerView.setAdapter(ratingAdapter);

                    }
                } else {
                    onFailure(call, new Throwable("API call failed with status code: " + response.code()));
                }
            }

            @Override
            public void onFailure(Call<List<Rating>> call, Throwable t) {
                Log.e("GAccommodationRatingsFragment", "API call failed: " + t.getMessage());
            }
        });
    }

    public GAccommodationRatingsFragment(Long accommodationId, Long hostId) {
        this.accommodationId = accommodationId;
        this.hostId = hostId;
    }
    private void showNotification() {
        createNotificationChannel();

        NotificationCompat.Builder builder = new NotificationCompat.Builder(requireContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notifications)
                .setContentTitle("New comment")
                .setContentText("You have received new rating from your guest.")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManager notificationManager = (NotificationManager) requireContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, builder.build());
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Your Channel";
            String description = "Channel for your notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            channel.enableLights(true);
            channel.setLightColor(Color.RED);

            NotificationManager notificationManager = requireContext().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}