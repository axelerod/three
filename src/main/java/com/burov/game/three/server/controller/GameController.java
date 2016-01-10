package com.burov.game.three.server.controller;

import com.burov.game.three.shared.model.Game;
import com.burov.game.three.shared.model.Player;
import com.burov.game.three.shared.model.Status;
import com.burov.game.three.server.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

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

    @RequestMapping(path = "/{gameId}/players/{playerId}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Game move(@PathVariable("playerId") @NotNull @Size(min = 1) String playerId,
                     @PathVariable("gameId") @NotNull @Size(min = 1) String gameId,
                     @RequestBody @Valid Game game) {
        return gameService.move(game, playerId, gameId);
    }

    @RequestMapping(path = "/{gameId}/players/{playerId}", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void applyToGame(@PathVariable("playerId") @NotNull @Size(min = 1) String playerId,
                            @PathVariable("gameId") @NotNull @Size(min = 1) String gameId,
                            @RequestBody @Valid Player player) {
        gameService.applyToGame(player, gameId);
    }
}
