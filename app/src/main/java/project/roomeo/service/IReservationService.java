package project.roomeo.service;

import java.util.List;

import project.roomeo.DTO.ReservationAndAccommodationDTO;
import project.roomeo.models.Accommodation;
import project.roomeo.models.Rating;
import project.roomeo.models.ReportItem;
import project.roomeo.models.Reservation;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface IReservationService {
    @Headers(
            {"User-Agent: Mobile-Android",
                    "Content-Type:application/json"}
    )

    @GET(ServiceUtils.reservation)
    Call<List<Reservation>> getAllReservations();

    @DELETE(ServiceUtils.reservation + "/delete-reservation/{id}")
    Call<Void> deleteReservation(@Path("id") Long id);

    @POST(ServiceUtils.reservation + "/add-reservation")
    Call<Reservation> addReservation(@Body Reservation reservation);

    @GET(ServiceUtils.reservation + "/{id}")
    Call<Reservation> getReservation(@Path("id") String id);
    @PUT(ServiceUtils.reservation + "/accept/{id}")
    Call<Reservation> acceptReservationRequest(@Path("id") String id);

    @PUT(ServiceUtils.reservation + "/decline/{id}")
    Call<Reservation> declineReservationRequest(@Path("id") String id);
    @PUT(ServiceUtils.reservation + "/cancel/{id}")
    Call<Reservation> cancelReservationRequest(@Path("id") String id);

    @GET(ServiceUtils.reservation + "/guestReservations/{guestId}")
    Call<List<Reservation>> getGuestReservations(@Path("guestId") String guestId);
    @GET(ServiceUtils.reservation + "/hostReservations/{hostId}")
    Call<List<Reservation>> getHostReservations(@Path("hostId") String hostId);
    @GET(ServiceUtils.reservation + "/accommodationReservations/{accommodationId}")
    Call<List<Reservation>> getAccommodationReservations(@Path("accommodationId") String accommodationId);
    @GET(ServiceUtils.reservation + "/hostReport/{hostId}/{start}/{end}")
    Call<List<ReportItem>> getReport(@Path("hostId") String hostId,@Path("start") String start,@Path("end") String end);

    @GET(ServiceUtils.reservation + "/hostReservationAndAccommodationName/{hostId}")
    Call<List<Reservation>> getHostReservationAndAccommodationName(@Path("hostId") Long hostId);

    @GET(ServiceUtils.reservation + "/guestReservationAndAccommodationName/{guestId}")
    Call<List<Reservation>> getGuestReservationAndAccommodationName(@Path("guestId") Long myId);

    @DELETE("/reservations/{id}")
    Call<Void> deleteReservationRequest(String toString);
}
