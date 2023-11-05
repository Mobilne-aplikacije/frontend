package com.example.uberapp_tim22.service;

import com.example.uberapp_tim22.DTO.PassengerDTO;
import com.example.uberapp_tim22.DTO.ReviewDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface IReviewService {

    @Headers(
            {"User-Agent: Mobile-Android",
                    "Content-Type:application/json"}
    )
    @GET(ServiceUtils.review + "/{rideId}") //Getting all reviews for the specific ride
    Call<ReviewDTO> getRideReviews(@Path("rideId") String id);

    @GET(ServiceUtils.review + "/driver/{id}") //Getting the reviews for the driver
    Call<ReviewDTO> getDriverReviews(@Path("id") String id);

    @GET(ServiceUtils.review + "/vehicle/{id}") //Getting the reviews for the vehicle
    Call<ReviewDTO> getVehicleReviews(@Path("rideId") String id);

//    @POST(ServiceUtils.review + "/{rideId}/vehicle") //Leave review for the vehicle
//    Call<CreateVehicleReviewDTO> createVehicleReview(@Path("rideId") String id, @Body CreateVehicleReviewDTO createVehicleReviewDTO);
//
//    @POST(ServiceUtils.review + "{rideId}/driver") //Leave review for the vehicle
//    Call<CreateDriverReviewDTO> createDriverReview(@Path("rideId") String id, @Body CreateDriverReviewDTO createDriverReviewDTO);
}
