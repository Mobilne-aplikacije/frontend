package project.roomeo.service;

import project.roomeo.models.Accommodation;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface IAccomodationService {
    @GET("/api/accommodation/{id}")
    Call<Accommodation> getAccommodationById(@Path("id") int accommodationId);
}
