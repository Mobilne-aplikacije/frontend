package project.roomeo.service;

import project.roomeo.DTO.GuestDTO;
import project.roomeo.DTO.RequestGuestDTO;
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
    Call<GuestDTO> getGuest(@Path("id") String id);

    @POST(ServiceUtils.guest)
    Call<GuestDTO> createGuest(@Body RequestGuestDTO request);


}
