package com.example.uberapp_tim22.service;

import com.example.uberapp_tim22.DTO.DriverActivityDTO;
import com.example.uberapp_tim22.DTO.DriverDTO;
import com.example.uberapp_tim22.DTO.DriverRideDTO;
import com.example.uberapp_tim22.DTO.DriverUpdate;
import com.example.uberapp_tim22.DTO.DriverVehicleDTO;
import com.example.uberapp_tim22.DTO.PassengerDTO;
import com.example.uberapp_tim22.DTO.PassengerUpdate;
import com.example.uberapp_tim22.DTO.RequestDriverDTO;
import com.example.uberapp_tim22.DTO.RequestPassengerDTO;
import com.example.uberapp_tim22.DTO.ResponseRideDTO;
import com.example.uberapp_tim22.DTO.ResponseRideNewDTO;
import com.example.uberapp_tim22.model.Driver;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface IDriverService {
    @Headers(
            {"User-Agent: Mobile-Android",
                    "Content-Type:application/json"}
    )
    @GET(ServiceUtils.driver + "/{id}/accept") //Accept ride
    Call<ResponseRideNewDTO> acceptRide(@Path("id") String id);

    @GET(ServiceUtils.driver + "/{id}/withdraw") //Withdraw ride
    Call<ResponseRideNewDTO> withdrawRide(@Path("id") String id);

    @GET(ServiceUtils.driver + "/{id}") //Get driver details
    Call<DriverDTO> getDriver(@Path("id") String id);

    @PUT(ServiceUtils.driver + "/update/{id}") //Update existing driver
    Call<DriverDTO> updateDriver2(@Path("id") String id, @Body DriverUpdate driverUpdate);

    //    @GET(ServiceUtils.driver + "/{id}/ride")
//    Call<Paginated<DriverRideDTO>> getRides(@Path("id") String id,
//                                            @Query("page") int page,
//                                            @Query("size") int size,
//                                            @Query("sort") String sort);
    @GET(ServiceUtils.driver + "/{id}/rides")
    Call<List<ResponseRideDTO>> getDriverRides(@Path("id") String id);

    @GET(ServiceUtils.driver + "/{id}/pendingrides")
    Call<List<ResponseRideDTO>> getDriverPendingRides(@Path("id") String id);

    @POST(ServiceUtils.driver + "/{id}/activity")
    Call<String> changeActivity(@Path("id") String id, @Body DriverActivityDTO driverActivityDTO);

    @POST(ServiceUtils.driver) //Create passenger
    Call<DriverDTO> createNewDriver(@Body RequestDriverDTO request);


    @GET(ServiceUtils.driver + "/{id}/vehicle")
    Call<DriverVehicleDTO> getDriverVehicle(@Path("id") String id);

//    @POST(ServiceUtils.driver + "/{id}/activity")
//    Call<String> changeActivity(@Path("id") String id, @Body DriverActivityDTO driverActivityDTO); cemu ovo


}
