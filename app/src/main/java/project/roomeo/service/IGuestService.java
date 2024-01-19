package project.roomeo.service;

import project.roomeo.DTO.GuestDTO;
import project.roomeo.DTO.RequestGuestDTO;
import project.roomeo.models.Guest;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface IGuestService {

    @Headers(
            {"User-Agent: Mobile-Android",
                    "Content-Type:application/json"}
    )
    @GET(ServiceUtils.guest + "/{id}")
    Call<Guest> getGuest(@Path("id") String id);

    @POST(ServiceUtils.guest)
    Call<GuestDTO> createGuest(@Body RequestGuestDTO request);


}
