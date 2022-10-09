package com.example.communitygrocerystoreapp.SendNotificationPack;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAkL8LoSQ:APA91bFXsSYB3dUzTWnB2ZwPlJea8K0ESNjlHCar7ebpilVSm2GCnKNzIWOxcw2KW8YHl419s8IlTAotpC_4ICYB5fxWJL_LfqAyaU1YkEkNP51f-8dnHHSFUDqmqbZCqs2zCc-gWUDG"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotifcation(@Body NotificationSender body);
}

