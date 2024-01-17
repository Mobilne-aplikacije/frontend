package project.roomeo.service;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceUtils {


    //Andrea  192.168.0.29
    public static final String SERVICE_API_PATH = "http://192.168.0.29:8084/api/";


    public static final String host = "host";
    public static final String guest = "guest";
    public static final String accommodation = "accommodation";
    public static final String user = "user";


    public static OkHttpClient test(){
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .addInterceptor(interceptor).build();

        return client;
    }

    public static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(SERVICE_API_PATH)
            .addConverterFactory(GsonConverterFactory.create())
            .client(test())
            .build();

    public static IUserService userService = retrofit.create(IUserService.class);
    public static IGuestService guestService = retrofit.create(IGuestService.class);
    public static IHostService hostService = retrofit.create(IHostService.class);
    public static IAdminService adminService = retrofit.create(IAdminService.class);



}
