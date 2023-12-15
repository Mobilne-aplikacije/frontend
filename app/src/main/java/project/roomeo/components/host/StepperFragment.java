package project.roomeo.components.host;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import project.roomeo.R;


public class StepperFragment extends Fragment {

    private ViewPager2 viewPager;
    private StepperAdapter stepperAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stepper, container, false);

        viewPager = view.findViewById(R.id.viewPager);
        stepperAdapter = new StepperAdapter(getChildFragmentManager(), getLifecycle());
        viewPager.setAdapter(stepperAdapter);

        Button nextButton = view.findViewById(R.id.nextButton);
        Button prevButton = view.findViewById(R.id.prevButton);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigacija na sledeći korak
                if (viewPager.getCurrentItem() < stepperAdapter.getItemCount() - 1) {
                    viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                }
            }
        });

        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigacija na prethodni korak
                if (viewPager.getCurrentItem() > 0) {
                    viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
                }
            }
        });

        return view;
    }

}