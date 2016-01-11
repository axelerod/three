package com.burov.game.three.client.service.http;

import com.burov.game.three.server.controller.GamesResponse;
import com.burov.game.three.shared.model.Game;
import com.burov.game.three.shared.model.Player;
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

    public Optional<Game> applyToGame(String gameId, String playerId, Player player) {

        Call<Game> updatedGame = gameService.applyToGame(gameId, playerId, player);
        return new CallProcessor<>(HttpStatus.ACCEPTED, updatedGame, "Apply to game was not successful: ")
                .process();
    }

    public Optional<Game> newGame(Game game) {
        Call<Game> updatedGame = gameService.newGame(game);
        return new CallProcessor<>(HttpStatus.CREATED, updatedGame, "Game was not created: ")
                .process();
    }

    public Optional<Game> getGame(String gameId) {
        Call<Game> updatedGame = gameService.getGame(gameId);
        return new CallProcessor<>(HttpStatus.OK, updatedGame, "Can't get info about game: ")
                .process();
    }


    public Optional<Game> move(String gameId, String playerId, Game game) {
        Call<Game> updatedGame = gameService.move(gameId, playerId, game);
        return new CallProcessor<>(HttpStatus.ACCEPTED, updatedGame, "Can't get info about game: ")
                .process();
    }

    public void remove(String gameId) {
        Call<Void> voidCall = gameService.deleteGame(gameId);
        new CallProcessor<>(HttpStatus.ACCEPTED, voidCall, "Can't delete game")
        .process();
    }
}
