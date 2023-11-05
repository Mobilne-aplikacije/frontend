package com.example.uberapp_tim22.service;

import com.example.uberapp_tim22.DTO.DriverVehicleDTO;
import com.example.uberapp_tim22.DTO.PassengerDTO;
import com.example.uberapp_tim22.DTO.ResponseRideNewDTO;
import com.example.uberapp_tim22.DTO.RideDTO;
import com.example.uberapp_tim22.DTO.VehicleDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface IVehicleService {

    @Headers(
            {"User-Agent: Mobile-Android",
                    "Content-Type:application/json"}
    )

    @PUT(ServiceUtils.vehicle + "/{id}/location")
    Call<VehicleDTO> changeLocation(@Path("id") String id, @Body VehicleDTO vehicleDTO);

}
