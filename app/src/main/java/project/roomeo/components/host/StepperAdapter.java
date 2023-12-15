package project.roomeo.components.host;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class StepperAdapter extends FragmentStateAdapter {

    public StepperAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new Step1Fragment();
            case 1:
                return new Step2Fragment();
            case 2:
                return new Step3Fragment();
            default:
                return new Step1Fragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;  // Broj koraka u vašem steperu
    }
}
