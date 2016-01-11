package com.burov.game.three.client.service.http;

import com.burov.game.three.server.controller.GamesResponse;
import com.burov.game.three.shared.model.Game;
import com.burov.game.three.shared.model.Player;
import com.burov.game.three.shared.model.Status;
import retrofit2.Call;
import retrofit2.http.*;

public interface RetrofitGameService {
    @GET("/games")
    @Headers("Accept: application/vnd.burov.three.v1+json")
    Call<GamesResponse> listGames(@Query("statuses")Status... statuses);

    @POST("/games/{gameId}/players/{playerId}")
    @Headers("Accept: application/vnd.burov.three.v1+json")
    Call<Game> applyToGame(@Path("gameId")String gameId, @Path("playerId") String playerId, @Body Player player);

    @POST("/games")
    @Headers("Accept: application/vnd.burov.three.v1+json")
    Call<Game> newGame(@Body Game game);
}
