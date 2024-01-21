package project.roomeo.components.guest;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import project.roomeo.R;
import project.roomeo.components.host.AccommodationAdapter;
import project.roomeo.components.host.HostMainActivity;
import project.roomeo.components.host.HostRatingAdapter;
import project.roomeo.components.host.HostRatingsFragment;
import project.roomeo.components.host.StepperFragment;
import project.roomeo.models.Accommodation;
import project.roomeo.models.Rating;
import project.roomeo.models.Report;
import project.roomeo.models.Reservation;
import project.roomeo.models.enums.AccommodationRequestStatus;
import project.roomeo.models.enums.RatingStatus;
import project.roomeo.models.enums.RatingType;
import project.roomeo.service.ServiceUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GHostRatingsFragment extends Fragment {

    private RecyclerView recyclerView;
    private GHostRatingAdapter ratingAdapter;
    private Long myId;
    private Long hostId;
    private Long accommodationId;
    private Button reportHost;
    private Button addRating;
    public TextView averageRate;
    public boolean hadRes;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_g_host_ratings, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        hadRes = false;

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("preferences", Context.MODE_PRIVATE);
        myId = sharedPreferences.getLong("pref_id", 0L);
        Log.e("PROVERA ID", "provera id: " + myId);

        reportHost = view.findViewById(R.id.reportHost);
        reportHost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Report report = new Report(null, hostId.intValue(), null);
                Call<Report> call = ServiceUtils.reportService.addReport(report);
                call.enqueue(new Callback<Report>() {
                    @Override
                    public void onResponse(@NonNull Call<Report> call, @NonNull Response<Report> response) {
                        if (response.isSuccessful()) {
                            GHostRatingsFragment fragment = new GHostRatingsFragment(hostId,accommodationId);
                            ((GuestMainActivity) view.getContext()).loadFragment(fragment);
                        } else {
                            onFailure(call, new Throwable("API call failed with status code: " + response.code()));
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Report> call, @NonNull Throwable t) {
                        Log.e("Report", "API call failed: " + t.getMessage());

                    }
                });
            }
        });

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
                                        Toast.makeText(getActivity(), "Successfully added comment.", Toast.LENGTH_SHORT).show();
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
        getHostRatingsList(hostId);
        averageRate = getView().findViewById(R.id.averageRate);


        Call<Double> call = ServiceUtils.hostService.getHostAverageRate(hostId.toString());
        call.enqueue(new Callback<Double>() {
            @Override
            public void onResponse(Call<Double> call, Response<Double> response) {
                if (response.isSuccessful()) {
                    Double average = response.body();
                    if (average != null) {
                        averageRate.setText(average.toString());
                        Log.i("prosekk",  String.valueOf(average));

                    }
                } else {
                    onFailure(call, new Throwable("API call failed with status code: " + response.code()));
                }
            }

            @Override
            public void onFailure(Call<Double> call, Throwable t) {
                Log.e("Rating", "API call failed: " + t.getMessage());
            }
        });
    }

    private void getHostRatingsList(Long hostId) {

        Call<List<Rating>> call = ServiceUtils.ratingService.getAllHostRatings(hostId.toString());

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
                        ratingAdapter = new GHostRatingAdapter(acceptedRatings, hostId, myId);
                        recyclerView.setAdapter(ratingAdapter);

                    }
                } else {
                    onFailure(call, new Throwable("API call failed with status code: " + response.code()));
                }
            }

            @Override
            public void onFailure(Call<List<Rating>> call, Throwable t) {
                Log.e("GHostRatingsFragment", "API call failed: " + t.getMessage());
            }
        });
    }

    public GHostRatingsFragment(Long hostId,Long accommodationId) {
        this.hostId = hostId;
        this.accommodationId =accommodationId;
    }
}