package com.example.uberapp_tim22.service;

import com.example.uberapp_tim22.DTO.DriverRideDTO;
import com.example.uberapp_tim22.DTO.PassengerDTO;
import com.example.uberapp_tim22.DTO.ResponseRideNewDTO;
import com.example.uberapp_tim22.DTO.RideDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface IRideService {

    @Headers(
            {"User-Agent: Mobile-Android",
                    "Content-Type:application/json"}
    )
    @GET(ServiceUtils.ride + "/{id}") //Ride details
    Call<DriverRideDTO> getRide(@Path("id") String id);

    @POST(ServiceUtils.ride)
    Call<ResponseRideNewDTO> createRide(@Body RideDTO ride);

//    @GET(ServiceUtils.ride + "/driver/{driverId}/active") // Active ride for driver
//    Call<RideDTO> getRide(@Path("driverId") String id);
//
//    @GET(ServiceUtils.ride + "/passenger/{passengerId}/active") // Active ride for passenger
//    Call<RideDTO> getRide(@Path("passengerId") String id);

//    @POST(ServiceUtils.ride) //Creating a ride
//    Call<CreateRideDTO> createRide(@Body CreateRideDTO createdRideDTO);
//
//    @PUT(ServiceUtils.ride + "/{id}/end") //Accept ride
//    Call<DriverRideDTO> acceptRide(@Path("id") String id);
//
//    @PUT(ServiceUtils.ride + "/{id}/end") //Start ride
//    Call<DriverRideDTO> startRide(@Path("id") String id);
//
//    @PUT(ServiceUtils.ride + "/{id}/end") //End the ride
//    Call<DriverRideDTO> endRide(@Path("id") String id);
//
//    @PUT(ServiceUtils.ride + "/{id}/withdraw") //Cancel existing ride
//    Call<DriverRideDTO> withdrawRide(@Path("id") String id);
//
////    @PUT(ServiceUtils.ride + "/{id}/panic") //Panic procedure for the ride
////    Call<> setPanicReason(@Path("id") String id);
////
////    @PUT(ServiceUtils.ride + "/{id}/cancel") //Cancel the ride with an explanation
////    Call<> cancelRide(@Path("id") String id);
//
//
//    @POST(ServiceUtils.ride) //Create favorite locations for quick selection
//    Call<CreateRideDTO> postFavoriteRoute(@Path("favorites") String id, @Body CreateRideDTO createdRideDTO);
//
//    @GET(ServiceUtils.ride) //Get favorite locations
//    Call<DriverRideDTO> getFavoriteRoute(@Path("favorites") String id);
//
//    Delete existing favorite ride

    }
