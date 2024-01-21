package project.roomeo.components.guest;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import project.roomeo.R;
import project.roomeo.models.Accommodation;
import project.roomeo.models.enums.AccommodationRequestStatus;
import project.roomeo.service.ServiceUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GuestFavoritesFragment extends Fragment {

    private RecyclerView recyclerView;
    private FavoritesAdapter favoritesAdapter;
    private Long myId;
    private Button removeFromFav;

    public GuestFavoritesFragment() {
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
        View view = inflater.inflate(R.layout.fragment_guest_favorites, container, false);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("preferences", Context.MODE_PRIVATE);
        String myEmail = sharedPreferences.getString("pref_email", "");
        myId = sharedPreferences.getLong("pref_id", 0L);
        Log.e("PROVERA ID", "provera id: " + myId);


        recyclerView = view.findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        getAccommodationList();
        return view;
    }

    private void getAccommodationList() {
        Call<List<Accommodation>> call = ServiceUtils.guestService.getFavoriteAccommodations(myId.toString());

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
                        favoritesAdapter = new FavoritesAdapter(listAccepted,myId);
                        recyclerView.setAdapter(favoritesAdapter);
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