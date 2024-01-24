package project.roomeo.components.host;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import project.roomeo.R;
import project.roomeo.models.Accommodation;
import project.roomeo.models.ReportItem;
import project.roomeo.models.Reservation;
import project.roomeo.models.enums.ReservationRequestStatus;
import project.roomeo.service.ServiceUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HostReportsFragment extends Fragment {
    private Long myId;
    private final List<ReportItem> reservationList = new ArrayList<>();
    private RecyclerView recyclerView;
    private ReportAdapter reportAdapter;
    private Button show,pdfBtn;
    private TextView txt3,txt4;
    private EditText dateEditText1, dateEditText2;
    private String date1Value,date2Value;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_host_reports, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        txt3 = view.findViewById(R.id.textView3);
        txt4 = view.findViewById(R.id.textView4);

        show = view.findViewById(R.id.showReports);
        pdfBtn = view.findViewById(R.id.createPdf);

        show.setOnClickListener(v -> {

            date1Value = dateEditText1.getText().toString();
            date2Value = dateEditText2.getText().toString();

            Log.i("date1Value", date1Value);
            if (TextUtils.isEmpty(date1Value) || TextUtils.isEmpty(date2Value)) {
                Toast.makeText(getContext(), "Please enter start and end date", Toast.LENGTH_SHORT).show();
            } else {
                txt3.setVisibility(View.VISIBLE);
                txt4.setVisibility(View.VISIBLE);
//                pdfBtn.setVisibility(View.VISIBLE);
                reservationList.clear();
                getReportList();
                recyclerView.setVisibility(View.VISIBLE);
            }

        });

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("preferences", Context.MODE_PRIVATE);
        String myEmail = sharedPreferences.getString("pref_email", "");
        myId = sharedPreferences.getLong("pref_id", 0L);
        Log.i("PROVERA ID", "provera id: " + myId);

        dateEditText1 = view.findViewById(R.id.date1);

        dateEditText1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog1();
            }
        });
        dateEditText2 = view.findViewById(R.id.date2);

        dateEditText2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog2();
            }
        });
//        pdfBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                createPdf();
//            }
//        });

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    private void getReportList(){
        Call<List<ReportItem>> call = ServiceUtils.reservationService.getReport(myId.toString(),date1Value,date2Value);

        call.enqueue(new Callback<List<ReportItem>>() {
            @Override
            public void onResponse(Call<List<ReportItem>> call, Response<List<ReportItem>> response) {
                if (response.isSuccessful()) {
                    List<ReportItem> list = response.body();
                    reportAdapter = new ReportAdapter(list);
                    recyclerView.setAdapter(reportAdapter);

                }
            }
            @Override
            public void onFailure(Call<List<ReportItem>> call, Throwable t) {
                Log.e("AccommodationRequestsFragment", "API call failed: " + t.getMessage());
            }
        });
    }
    private void showDatePickerDialog1() {

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getActivity(),
                (view, year1, month1, dayOfMonth) -> {
                    // Koristite SimpleDateFormat za formatiranje datuma
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-DD");

                    // Postavi odabrani datum u EditText
                    Calendar selectedCalendar = Calendar.getInstance();
                    selectedCalendar.set(year1, month1, dayOfMonth);
                    String selectedDate = dateFormat.format(selectedCalendar.getTime());

                    dateEditText1.setText(selectedDate);
                },
                year,
                month,
                day
        );

        // Prikazivanje dijaloga
        datePickerDialog.show();
    }
    private void showDatePickerDialog2() {

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getActivity(),
                (view, year1, month1, dayOfMonth) -> {
                    // Koristite SimpleDateFormat za formatiranje datuma
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-DD");

                    // Postavi odabrani datum u EditText
                    Calendar selectedCalendar = Calendar.getInstance();
                    selectedCalendar.set(year1, month1, dayOfMonth);
                    String selectedDate = dateFormat.format(selectedCalendar.getTime());

                    dateEditText2.setText(selectedDate);
                },
                year,
                month,
                day
        );

        // Prikazivanje dijaloga
        datePickerDialog.show();
    }
}