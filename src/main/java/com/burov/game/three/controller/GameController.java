package com.burov.game.three.controller;

import com.burov.game.three.model.Game;
import com.burov.game.three.model.Status;
import com.burov.game.three.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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
}
