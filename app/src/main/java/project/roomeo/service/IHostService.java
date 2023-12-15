package project.roomeo.service;


import java.util.List;

import project.roomeo.DTO.HostDTO;
import project.roomeo.DTO.RequestHostDTO;
import project.roomeo.models.Accommodation;
import project.roomeo.models.Host;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface IHostService {
    @Headers(
            {"User-Agent: Mobile-Android",
                    "Content-Type:application/json"}
    )
    @GET(ServiceUtils.accommodation + "/{hostId}")
    Call<List<Accommodation>> getHostAccommodations(@Path("hostId") String hostId);

    @POST(ServiceUtils.accommodation + "/add-accommodation")
    Call<Accommodation> addAccommodation(@Body Accommodation accommodation);

    @POST(ServiceUtils.host)
    Call<HostDTO> createNewHost(@Body RequestHostDTO request);
    @GET(ServiceUtils.host + "/{id}")
    Call<Host> getHost(@Path("id") String id);
}
