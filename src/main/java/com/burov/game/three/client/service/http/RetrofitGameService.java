package com.burov.game.three.client.service.http;

import com.burov.game.three.server.controller.GamesResponse;
import com.burov.game.three.shared.model.Status;
import retrofit2.Call;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RetrofitGameService {
    @POST("/games")
    @Headers("Accept: application/vnd.burov.three.v1+json")
    Call<GamesResponse> listGames(@Query("statuses")Status... statuses);
}
