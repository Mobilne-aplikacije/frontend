package project.roomeo.service;

import java.util.List;

import project.roomeo.models.Accommodation;
import project.roomeo.models.Host;
import project.roomeo.models.Rating;
import project.roomeo.models.Report;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface IReportService {
    @Headers(
            {"User-Agent: Mobile-Android",
                    "Content-Type:application/json"}
    )

    @GET(ServiceUtils.report)
    Call<List<Report>> getAllReports();

    @DELETE(ServiceUtils.report + "/delete-report/{id}")
    Call<Void> deleteReport(@Path("id") Long id);

    @PUT(ServiceUtils.user + "/{id}/block")
    Call<Void> blockUser(@Path("id") String id);

    @POST(ServiceUtils.report + "/add-report")
    Call<Report> addReport(@Body Report report);

    @GET(ServiceUtils.report + "/{id}")
    Call<Report> getReport(@Path("id") String id);

}
