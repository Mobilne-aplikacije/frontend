package project.roomeo.components.host;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.fragment.app.Fragment;

public class HostReservationsPagerAdapter extends FragmentStateAdapter {

    public HostReservationsPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 1) {
            return new HostReservationsListFragment();
        }
        return new HostRequestsFragment();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
