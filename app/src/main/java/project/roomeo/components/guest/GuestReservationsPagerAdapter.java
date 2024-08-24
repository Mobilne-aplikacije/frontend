package project.roomeo.components.guest;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;


public class GuestReservationsPagerAdapter extends FragmentStateAdapter {


    public GuestReservationsPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 1) {
            return new GuestReservationsListFragment();
        }else if(position == 2){
            return new GuestFavoritesFragment();

        }
        return new GuestRequestsFragment();
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
