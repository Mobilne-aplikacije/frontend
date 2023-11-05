package com.example.uberapp_tim22.service;

import com.example.uberapp_tim22.DTO.EmailDTO;
import com.example.uberapp_tim22.DTO.IdAndEmailDTO;
import com.example.uberapp_tim22.DTO.PassengerDTO;
import com.example.uberapp_tim22.DTO.PassengerUpdate;
import com.example.uberapp_tim22.DTO.RequestPassengerDTO;
import com.example.uberapp_tim22.DTO.ResponseRideDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface IPassengerService {

    @Headers(
            {"User-Agent: Mobile-Android",
                    "Content-Type:application/json"}
    )
    @GET(ServiceUtils.passenger + "/{id}") //Passenger details
    Call<PassengerDTO> getPassenger(@Path("id") String id);

    @PUT(ServiceUtils.passenger + "/{id}") //Update existing passenger
    Call<PassengerDTO> updatePassenger(@Path("id") String id, @Body PassengerUpdate passengerDTO);

    @POST(ServiceUtils.passenger) //Create passenger
    Call<PassengerDTO> createPassenger(@Body RequestPassengerDTO request);

    @GET(ServiceUtils.passenger + "/passengers")
    Call<List<String>> getPassengers();

    @POST(ServiceUtils.passenger + "/exist") //Get Passenger Id and Email
    Call<IdAndEmailDTO> getPassengerIdAndEmail(@Body EmailDTO email);

//    @GET(ServiceUtils.passenger + "/{id}/ride")
//    Call<Paginated<ResponseRideDTO>> getPassengerRides(@Path("id") String id, @Query("page") int page, @Query("size") int size);

    @GET(ServiceUtils.passenger + "/{id}/rides")
    Call<List<ResponseRideDTO>> getPassengerRides(@Path("id") String id);


//    @PUT(ServiceUtils.passenger + "/{id}") //Update existing passenger
//    Call<PassengerDTO> updatePassenger(@Path("id") String id, @Body PassengerDTO passengerDTO);


//    @GET(ServiceUtils.passenger + "/activate/{activationId}") //Activate passenger account
//    Call<> activatePassenger(@Path("id") String id);

//    @GET(ServiceUtils.passenger) //Getting passengers ???
//    Call<Paginated<PassengerDTO> getPassengers(@Query("page") int page, @Query("size") int size);


}
