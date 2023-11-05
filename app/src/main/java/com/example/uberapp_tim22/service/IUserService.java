package com.example.uberapp_tim22.service;

import com.example.uberapp_tim22.DTO.ChangePasswordDTO;
import com.example.uberapp_tim22.DTO.HopInInboxReturnedDTO;
import com.example.uberapp_tim22.DTO.HopInMessageDTO;
import com.example.uberapp_tim22.DTO.HopInMessageReturnedDTO;
import com.example.uberapp_tim22.DTO.MessageDTO;
import com.example.uberapp_tim22.DTO.ResponsePassengerDTO;
import com.example.uberapp_tim22.DTO.SendMessageDTO;
import com.example.uberapp_tim22.DTO.RequestLoginDTO;
import com.example.uberapp_tim22.DTO.ResponseLoginDTO;
import com.example.uberapp_tim22.DTO.UserDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface IUserService {

    @Headers(
            {"User-Agent: Mobile-Android",
                    "Content-Type:application/json"}
    )

    @PUT(ServiceUtils.user + "/{id}/changePassword")
    Call<ResponsePassengerDTO> changePassword(@Path("id") String id, @Body ChangePasswordDTO changePasswordDTO);

    @GET(ServiceUtils.user + "/{id}/user")
    Call<UserDTO> findById(@Path("id") String id);

    @POST(ServiceUtils.user + "/{id}/message")
    Call<MessageDTO> sendMessage(@Path("id") String id, @Body SendMessageDTO sendMessageDTO);

    @GET(ServiceUtils.user + "/{id}/message")
    Call<Paginated<MessageDTO>> getMessages(@Path("id") String id);

    @POST(ServiceUtils.user + "/login")
    Call<ResponseLoginDTO> login(@Body RequestLoginDTO loginDTO);

    @GET(ServiceUtils.user + "/{email}/resetPasswordByEmail")
    Call<UserDTO> findByEmail(@Path("email") String email);

    @POST("/{id}/hopin-message")
    Call<HopInMessageReturnedDTO> sendMessage(@Path("id") Long id, @Body HopInMessageDTO message);

    @GET("/{id}/inbox")
    Call<HopInInboxReturnedDTO> getInbox(@Path("id") Long id);


}
