package com.burov.game.three.controller;

import com.burov.game.three.model.Game;
import com.burov.game.three.model.Player;
import com.burov.game.three.model.Status;
import com.burov.game.three.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.websocket.server.PathParam;

@RestController
@RequestMapping(value = "/games")
public class GameController {

    private GameService gameService;

    @Autowired
    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    /**
     * Retrieves list of games
     *
     * @param statuses array of statuses to filter games by. Optional, in case
     *                 not set, all games returned.
     * @return list of games
     */
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public GamesResponse listGames(@RequestParam(name = "statuses", required = false) Status[] statuses) {
        return new GamesResponse(statuses == null ?
                gameService.listGames(Status.values()) : gameService.listGames(statuses));
    }

    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Game newGame(@RequestBody @Valid Game game) {
        return gameService.create(game);
    }

    @RequestMapping(path = "/{gameId}/players/{playerId}", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public void applyToGame(@PathVariable("playerId") @NotNull @Size(min = 1) String playerId,
                            @PathVariable("gameId") @NotNull @Size(min = 1) String gameId,
                            @RequestBody @Valid Player player) {
        gameService.applyToGame(player, gameId);
    }
}
