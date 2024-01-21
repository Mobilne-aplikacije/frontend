package project.roomeo.components.host;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import project.roomeo.R;
import project.roomeo.models.Accommodation;
import project.roomeo.models.enums.AccommodationRequestStatus;
import project.roomeo.service.ServiceUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HostAccommodationsFragment extends Fragment {

    private RecyclerView recyclerView;
    private AccommodationAdapter accommodationAdapter;
    private Long myId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_host_accommodations, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        Button addAccommodation = view.findViewById(R.id.add_accommodation);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("preferences", Context.MODE_PRIVATE);
        String myEmail = sharedPreferences.getString("pref_email", "");
        myId = sharedPreferences.getLong("pref_id", 0L);
        Log.e("PROVERA ID", "provera id: " + myId);

        getAccommodationList();
        addAccommodation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StepperFragment fragment = new StepperFragment();
                ((HostMainActivity) v.getContext()).loadFragment(fragment);
            }
        });


        Button pendingAccommodations = view.findViewById(R.id.pending_accommodations);
        pendingAccommodations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HostPendingAccommodationsFragment fragment = new HostPendingAccommodationsFragment();
                ((HostMainActivity) v.getContext()).loadFragment(fragment);
                Toast.makeText(view.getContext(), "Pending accommodations", Toast.LENGTH_SHORT).show();
            }
        });
        Button acceptedAccommodations = view.findViewById(R.id.accepted_accommodations);
        acceptedAccommodations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HostAccommodationsFragment fragment = new HostAccommodationsFragment();
                ((HostMainActivity) v.getContext()).loadFragment(fragment);
                Toast.makeText(view.getContext(), "Accepted accommodations", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private void getAccommodationList() {
        Call<List<Accommodation>> call = ServiceUtils.hostService.getHostAccommodations(myId.toString());

        call.enqueue(new Callback<List<Accommodation>>() {
            @Override
            public void onResponse(Call<List<Accommodation>> call, Response<List<Accommodation>> response) {
                if (response.isSuccessful()) {
                    List<Accommodation> list = response.body();
                    if (list != null) {
                        List<Accommodation> listAccepted = new ArrayList<Accommodation>();
                        for (int i = 0; i < list.size(); i++) {
                            if (list.get(i).getStatus()== AccommodationRequestStatus.ACCEPTED){
                                listAccepted.add(list.get(i));
                            }
                        }
                        accommodationAdapter = new AccommodationAdapter(listAccepted);
                        recyclerView.setAdapter(accommodationAdapter);
                    }
                } else {
                    onFailure(call, new Throwable("API call failed with status code: " + response.code()));
                }
            }

            @Override
            public void onFailure(Call<List<Accommodation>> call, Throwable t) {
                Log.e("AccommodationRequestsFragment", "API call failed: " + t.getMessage());
            }
        });
    }
}