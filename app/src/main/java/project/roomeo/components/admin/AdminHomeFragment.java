package project.roomeo.components.admin;

import android.os.Bundle;

import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.text.SimpleDateFormat;

import project.roomeo.R;

public class AdminHomeFragment extends Fragment {

    public AdminHomeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admin_home, container, false);

        // Set up Date Range Picker functionality
        TextView dateRangeTextView = view.findViewById(R.id.datePickerEditText);

        MaterialDatePicker<Pair<Long, Long>> picker = MaterialDatePicker.Builder.dateRangePicker().build();
        picker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Pair<Long, Long>>() {
            @Override
            public void onPositiveButtonClick(Pair<Long, Long> selection) {
                // Handle the selected date range here
                SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
                String startDate = dateFormat.format(selection.first);
                String endDate = dateFormat.format(selection.second);
                String dateRange = startDate + " - " + endDate;
                dateRangeTextView.setText(dateRange);
            }
        });

        dateRangeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picker.show(getParentFragmentManager(), picker.toString());
            }
        });

        return view;
    }
}