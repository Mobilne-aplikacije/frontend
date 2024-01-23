package project.roomeo.components.host;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import project.roomeo.R;
import project.roomeo.models.ReportItem;
import project.roomeo.models.Reservation;
import project.roomeo.models.enums.ReservationRequestStatus;
import project.roomeo.service.ServiceUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccommodationReportFragment extends Fragment {


    private Long accommodationId;
    private TextView janRes, febRes, marRes, aprRes, mayRes, junRes, julRes, augRes, sepRes, octRes, novRes, decRes;
    private TextView janProfit, febProfit, marProfit, aprProfit, mayProfit, junProfit, julProfit, augProfit, sepProfit, octProfit, novProfit, decProfit;
    private int janr, janp, febr, febp, marr, marp, aprr, aprp, mayr, mayp, junr, junp, julr, julp, augr, augp, sepr, sepp, octr, octp, novr, novp, decr, decp;

    public AccommodationReportFragment(Long accommodationId) {
        this.accommodationId = accommodationId;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_accommodation_report, container, false);

        janRes = view.findViewById(R.id.janRes);
        janProfit = view.findViewById(R.id.janProfit);
        janr = 0;
        janp = 0;

        febRes = view.findViewById(R.id.febRes);
        febProfit = view.findViewById(R.id.febProfit);
        febr = 0;
        febp = 0;

        marRes = view.findViewById(R.id.marRes);
        marProfit = view.findViewById(R.id.marProfit);
        marr = 0;
        marp = 0;

        aprRes = view.findViewById(R.id.aprRes);
        aprProfit = view.findViewById(R.id.aprProfit);
        aprr = 0;
        aprp = 0;

        mayRes = view.findViewById(R.id.mayRes);
        mayProfit = view.findViewById(R.id.mayProfit);
        mayr = 0;
        mayp = 0;

        junRes = view.findViewById(R.id.junRes);
        junProfit = view.findViewById(R.id.junProfit);
        junr = 0;
        junp = 0;

        julRes = view.findViewById(R.id.julRes);
        julProfit = view.findViewById(R.id.julProfit);
        julr = 0;
        julp = 0;

        augRes = view.findViewById(R.id.augRes);
        augProfit = view.findViewById(R.id.augProfit);
        augr = 0;
        augp = 0;

        sepRes = view.findViewById(R.id.sepRes);
        sepProfit = view.findViewById(R.id.sepProfit);
        sepr = 0;
        sepp = 0;

        octRes = view.findViewById(R.id.octRes);
        octProfit = view.findViewById(R.id.octProfit);
        octr = 0;
        octp = 0;

        novRes = view.findViewById(R.id.novRes);
        novProfit = view.findViewById(R.id.novProfit);
        novr = 0;
        novp = 0;

        decRes = view.findViewById(R.id.decRes);
        decProfit = view.findViewById(R.id.decProfit);
        decr = 0;
        decp = 0;

        getReport();

        return view;
    }

    private void getReport() {
        Call<List<Reservation>> call = ServiceUtils.reservationService.getAccommodationReservations(accommodationId.toString());

        call.enqueue(new Callback<List<Reservation>>() {
            @Override
            public void onResponse(Call<List<Reservation>> call, Response<List<Reservation>> response) {
                if (response.isSuccessful()) {
                    List<Reservation> list = response.body();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
                    Date today = new Date();

                    if (list != null) {
                        for (int i = 0; i < list.size(); i++) {
                            try {
                                Date myDate = dateFormat.parse(list.get(i).getEndDate());
                                Calendar calendar = Calendar.getInstance();
                                calendar.setTime(myDate);

                                if (myDate.before(today)) {
                                    if (list.get(i).getStatus() == ReservationRequestStatus.ACCEPTED) {
                                        if (calendar.get(Calendar.MONTH) == Calendar.JANUARY) {
                                            janr++;
                                            janp = janp + list.get(i).getPrice();
                                        } else if (calendar.get(Calendar.MONTH) == Calendar.FEBRUARY) {
                                            febr++;
                                            febp += list.get(i).getPrice();
                                        } else if (calendar.get(Calendar.MONTH) == Calendar.MARCH) {
                                            marr++;
                                            marp += list.get(i).getPrice();
                                        } else if (calendar.get(Calendar.MONTH) == Calendar.APRIL) {
                                            aprr++;
                                            aprp += list.get(i).getPrice();
                                        } else if (calendar.get(Calendar.MONTH) == Calendar.MAY) {
                                            mayr++;
                                            mayp += list.get(i).getPrice();
                                        } else if (calendar.get(Calendar.MONTH) == Calendar.JUNE) {
                                            junr++;
                                            junp += list.get(i).getPrice();
                                        } else if (calendar.get(Calendar.MONTH) == Calendar.JULY) {
                                            julr++;
                                            julp += list.get(i).getPrice();
                                        } else if (calendar.get(Calendar.MONTH) == Calendar.AUGUST) {
                                            augr++;
                                            augp += list.get(i).getPrice();
                                        } else if (calendar.get(Calendar.MONTH) == Calendar.SEPTEMBER) {
                                            sepr++;
                                            sepp += list.get(i).getPrice();
                                        } else if (calendar.get(Calendar.MONTH) == Calendar.OCTOBER) {
                                            octr++;
                                            octp += list.get(i).getPrice();
                                        } else if (calendar.get(Calendar.MONTH) == Calendar.NOVEMBER) {
                                            novr++;
                                            novp += list.get(i).getPrice();
                                        } else if (calendar.get(Calendar.MONTH) == Calendar.DECEMBER) {
                                            decr++;
                                            decp += list.get(i).getPrice();
                                        }
                                    }
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                        janRes.setText(String.valueOf(janr));
                        janProfit.setText(String.valueOf(janp));

                        febRes.setText(String.valueOf(febr));
                        febProfit.setText(String.valueOf(febp));

                        marRes.setText(String.valueOf(marr));
                        marProfit.setText(String.valueOf(marp));

                        aprRes.setText(String.valueOf(aprr));
                        aprProfit.setText(String.valueOf(aprp));

                        mayRes.setText(String.valueOf(mayr));
                        mayProfit.setText(String.valueOf(mayp));

                        junRes.setText(String.valueOf(junr));
                        junProfit.setText(String.valueOf(junp));

                        julRes.setText(String.valueOf(julr));
                        julProfit.setText(String.valueOf(julp));

                        augRes.setText(String.valueOf(augr));
                        augProfit.setText(String.valueOf(augp));

                        sepRes.setText(String.valueOf(sepr));
                        sepProfit.setText(String.valueOf(sepp));

                        octRes.setText(String.valueOf(octr));
                        octProfit.setText(String.valueOf(octp));

                        novRes.setText(String.valueOf(novr));
                        novProfit.setText(String.valueOf(novp));

                        decRes.setText(String.valueOf(decr));
                        decProfit.setText(String.valueOf(decp));
                    }
                } else {
                    onFailure(call, new Throwable("API call failed with status code: " + response.code()));
                }
            }

            @Override
            public void onFailure(Call<List<Reservation>> call, Throwable t) {
                Log.e("AccommodationRequestsFragment", "API call failed: " + t.getMessage());
            }
        });
    }

}