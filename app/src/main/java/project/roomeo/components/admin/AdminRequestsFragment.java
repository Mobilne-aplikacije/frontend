package project.roomeo.components.admin;

import android.os.Bundle;

import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.text.SimpleDateFormat;

import project.roomeo.R;
import project.roomeo.components.host.HostAccommodationsFragment;
import project.roomeo.components.host.HostMainActivity;

public class AdminRequestsFragment extends Fragment {
    public Button button1, button2, button3, button4;
    public AdminRequestsFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_admin_requests, container, false);

        button1 = view.findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AccommodationRequestsFragment fragment = new AccommodationRequestsFragment();
                ((AdminMainActivity) view.getContext()).loadFragment(fragment);
                 }
        });

        button2 = view.findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RatingRequestsFragment fragment = new RatingRequestsFragment();
                ((AdminMainActivity) view.getContext()).loadFragment(fragment);
            }
        });

        button3 = view.findViewById(R.id.button3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserReportRequestsFragment fragment = new UserReportRequestsFragment();
                ((AdminMainActivity) view.getContext()).loadFragment(fragment);
            }
        });

        button4 = view.findViewById(R.id.button4);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RatingReportRequestsFragment fragment = new RatingReportRequestsFragment();
                ((AdminMainActivity) view.getContext()).loadFragment(fragment);
            }
        });

        return view;
    }
}