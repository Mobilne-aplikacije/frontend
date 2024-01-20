package project.roomeo.service;


import project.roomeo.DTO.RequestLoginDTO;
import project.roomeo.DTO.ResponseLoginDTO;
import project.roomeo.DTO.UserDTO;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface IUserService {

    @Headers(
            {"User-Agent: Mobile-Android",
                    "Content-Type:application/json"}
    )

    @GET(ServiceUtils.user + "/{id}/user")
    Call<UserDTO> findById(@Path("id") String id);


    @POST(ServiceUtils.user + "/login")
    Call<ResponseLoginDTO> login(@Body RequestLoginDTO loginDTO);

    @GET(ServiceUtils.user + "/{email}/resetPasswordByEmail")
    Call<UserDTO> findByEmail(@Path("email") String email);



}
