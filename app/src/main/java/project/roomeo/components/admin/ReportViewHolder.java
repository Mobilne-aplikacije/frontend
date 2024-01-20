package project.roomeo.components.admin;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import project.roomeo.R;
import project.roomeo.models.Guest;
import project.roomeo.models.Host;
import project.roomeo.models.Rating;
import project.roomeo.models.Report;
import project.roomeo.service.ServiceUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportViewHolder  extends RecyclerView.ViewHolder {

    public TextView userType;
    public TextView userName;
    public TextView email;
    public TextView phone;
    public TextView address;
    public Button acceptButton;
    public Button declineButton;
    private Guest guest;
    private Host host;

    public ReportViewHolder(View itemView){
        super(itemView);
        userType = itemView.findViewById(R.id.userType);
        userName = itemView.findViewById(R.id.userName);
        email = itemView.findViewById(R.id.email);
        phone = itemView.findViewById(R.id.phone);
        address = itemView.findViewById(R.id.address);

        acceptButton = itemView.findViewById(R.id.accept);
        declineButton = itemView.findViewById(R.id.decline);
    }

    @SuppressLint("SetTextI18n")
    public void bindData(Report item) {

        if(item.getHostId() != null){

            Call<Host> call = ServiceUtils.hostService.getHost(String.valueOf(item.getHostId()));

            call.enqueue(new Callback<Host>() {
                @Override
                public void onResponse(Call<Host> call, Response<Host> response) {
                    if (response.isSuccessful()) {
                        host = response.body();
                        Log.i("host",host.toString());
                        userName.setText(host.getFirstName()+" "+host.getLastName());
                        userType.setText("HOST");
                        email.setText(host.getEmail());
                        phone.setText(String.valueOf(host.getPhoneNumber()));
                        address.setText(host.getAddress());
                    } else {
                        onFailure(call, new Throwable("API call failed with status code: " + response.code()));
                    }
                }

                @Override
                public void onFailure(Call<Host> call, Throwable t) {
                    Log.e("Host", "API call failed: " + t.getMessage());
                }
            });

        }else if(item.getGuestId() != null){
            Call<Guest> call = ServiceUtils.guestService.getGuest(String.valueOf(item.getGuestId()));

            call.enqueue(new Callback<Guest>() {
                @Override
                public void onResponse(Call<Guest> call, Response<Guest> response) {
                    if (response.isSuccessful()) {
                        guest = response.body();
                        Log.i("guest",guest.toString());
                        userName.setText(guest.getFirstName()+" "+guest.getLastName());
                        userType.setText("GUEST");
                        email.setText(guest.getEmail());
                        phone.setText(String.valueOf(guest.getPhoneNumber()));
                        address.setText(guest.getAddress());
                    } else {
                        onFailure(call, new Throwable("API call failed with status code: " + response.code()));
                    }
                }

                @Override
                public void onFailure(Call<Guest> call, Throwable t) {
                    Log.e("guest", "API call failed: " + t.getMessage());
                }
            });
        }
    }

}
