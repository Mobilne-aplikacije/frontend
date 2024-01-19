package project.roomeo.service;

import java.util.List;

import project.roomeo.models.Accommodation;
import project.roomeo.models.Rating;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface IRatingService {
    @Headers(
            {"User-Agent: Mobile-Android",
                    "Content-Type:application/json"}
    )

    @GET(ServiceUtils.rating)
    Call<List<Rating>> getAllRatings();

    @PUT(ServiceUtils.rating + "/accept/{rating-id}")
    Call<Rating> acceptRatingRequest(@Path("rating-id") String id);

    @PUT(ServiceUtils.rating + "/reject/{rating-id}")
    Call<Rating> rejectRatingRequest(@Path("rating-id") String id);

    @GET(ServiceUtils.rating + "/get-host-ratings/{host-id}")
    Call<List<Rating>> getAllHostRatings(@Path("host-id") String id);
    @GET(ServiceUtils.rating + "/get-accommodation-ratings/{accommodation-id}")
    Call<List<Rating>> getAllAccommodationRatings(@Path("accommodation-id") String id);


}