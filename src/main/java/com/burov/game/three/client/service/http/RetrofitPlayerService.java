package com.burov.game.three.client.service.http;

import com.burov.game.three.shared.model.Player;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface RetrofitPlayerService {
    @POST("/players")
    @Headers("Accept: application/vnd.burov.three.v1+json")
    Call<Player> register(@Body String name);
}
