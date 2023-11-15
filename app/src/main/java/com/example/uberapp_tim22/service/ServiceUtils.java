package com.example.uberapp_tim22.service;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceUtils {


    //Andrea  192.168.0.27
    public static final String SERVICE_API_PATH = "http://192.168.0.27:8084/api/";


    //public static final String SERVICE_API_PATH = "http://192.168.1.17:8084/api/";
    public static final String driver = "driver";
    public static final String passenger = "passenger";
    public static final String vehicle = "vehicle";
    public static final String review = "review";
    public static final String user = "user";
    public static final String ride = "ride";
    public static final String panic = "panic";
    public static final String unregisteredUser = "unregisteredUser";
    public static final String chat = "chat";


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
    public static IPassengerService passengerService = retrofit.create(IPassengerService.class);
    public static IDriverService driverService = retrofit.create(IDriverService.class);
    public static IVehicleService vehicleService = retrofit.create(IVehicleService.class);
    public static IReviewService reviewService = retrofit.create(IReviewService.class);
    public static IPanicService panicService = retrofit.create(IPanicService.class);
    public static IRideService rideService = retrofit.create(IRideService.class);
    public static IUnregisteredUserService unregisteredUserService = retrofit.create(IUnregisteredUserService.class);
    public static IChatService chatService = retrofit.create(IChatService.class);



}
