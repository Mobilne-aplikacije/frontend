package project.roomeo.service;

import java.util.List;

import project.roomeo.DTO.GuestDTO;
import project.roomeo.DTO.RequestGuestDTO;
import project.roomeo.models.Accommodation;
import project.roomeo.models.Guest;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
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
    @GET(ServiceUtils.accommodation)
    Call<List<Accommodation>> getAllAccommodations();
    @GET(ServiceUtils.accommodation + "/favorites/{guestId}")
    Call<List<Accommodation>> getFavoriteAccommodations(@Path("guestId") String guestId);
    @DELETE(ServiceUtils.accommodation + "/removeFavorite/{guestId}/{accommodationId}")
    Call<Void> removeFavorite(@Path("guestId") Long guestId,@Path("accommodationId") Long accommodationId);
    @PUT(ServiceUtils.accommodation + "/addFavorite/{guestId}/{accommodationId}")
    Call<Void> addFavorite(@Path("guestId") Long guestId,@Path("accommodationId") Long accommodationId);

}
