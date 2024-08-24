package project.roomeo.components.admin;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import project.roomeo.components.host.HostRequestsFragment;
import project.roomeo.components.host.HostReservationsListFragment;

public class AdminRatingsPageAdapter extends FragmentStateAdapter {
    public AdminRatingsPageAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 1) {
            return new RatingReportRequestsFragment();
        }
        return new RatingRequestsFragment();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
