package com.example.uberapp_tim22.service;

import com.example.uberapp_tim22.DTO.PanicDTO;
import com.example.uberapp_tim22.DTO.PassengerDTO;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;

public interface IPanicService {

    @Headers(
            {"User-Agent: Mobile-Android",
                    "Content-Type:application/json"}
    )
    @GET(ServiceUtils.panic)
    Call<PanicDTO> getPanic();
}
