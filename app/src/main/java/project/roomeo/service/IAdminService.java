package project.roomeo.service;

import java.util.List;

import project.roomeo.models.Accommodation;
import project.roomeo.models.Rating;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface IAdminService {
    @Headers(
            {"User-Agent: Mobile-Android",
                    "Content-Type:application/json"}
    )

    @GET(ServiceUtils.accommodation + "/get-pending-accommodations")
    Call<List<Accommodation>> getAllPendingAccommodations();
    @PUT(ServiceUtils.accommodation + "/accept/{accommodation-id}")
    Call<Accommodation> acceptAccommodationRequest(@Path("accommodation-id") String id);
    @PUT(ServiceUtils.accommodation + "/reject/{accommodation-id}")
    Call<Accommodation> rejectAccommodationRequest(@Path("accommodation-id") String id);
    @GET(ServiceUtils.accommodation + "/accommodation/{id}")
    Call<Accommodation> getAccommodation(@Path("id") String id);

}
