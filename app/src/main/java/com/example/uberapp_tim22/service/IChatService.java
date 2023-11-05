package com.example.uberapp_tim22.service;

import com.example.uberapp_tim22.DTO.ChatMessagesDTO;
import com.example.uberapp_tim22.DTO.MessDTO;
import com.example.uberapp_tim22.DTO.ResponseChatDTO;
import com.example.uberapp_tim22.DTO.ResponseRideNewDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface IChatService {

    @Headers(
            {"User-Agent: Mobile-Android",
                    "Content-Type:application/json"}
    )

    @GET(ServiceUtils.chat + "/{idUser1}/{idUser2}")
    Call<Long> getChatId(@Path("idUser1") Long idUser1, @Path("idUser2") Long idUser2);

    @GET(ServiceUtils.chat + "/user/{id}")
    Call<List<ResponseChatDTO>> getChatsOfUser(@Path("id") Long id);

    @POST(ServiceUtils.chat + "/sendMessage")
    Call<MessDTO> sendMessageToChat(@Body ChatMessagesDTO chatMessages);


}
