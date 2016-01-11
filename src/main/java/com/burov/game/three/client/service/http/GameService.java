package com.burov.game.three.client.service.http;

import com.burov.game.three.server.controller.GamesResponse;
import com.burov.game.three.shared.model.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import retrofit2.Call;

import java.util.Optional;

@Service
public class GameService {
    private final RetrofitGameService gameService;

    @Autowired
    public GameService(RetrofitGameService gameService) {
        this.gameService = gameService;
    }

    public Optional<GamesResponse> listGames(Status... statuses) {
        Call<GamesResponse> gamesResponseCall = gameService.listGames(statuses);
        return new CallProcessor<>(HttpStatus.OK, gamesResponseCall, "Games weren't retrieved: ")
                .process();
    }
}
