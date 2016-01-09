package com.burov.game.three.controller;

import com.burov.game.three.model.Status;
import com.burov.game.three.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/games")
public class GameController {

    private GameService gameService;

    @Autowired
    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public GamesResponse listGames(@RequestParam(name = "statuses", required = false) Status[] statuses) {
        return new GamesResponse(statuses == null ?
                gameService.listGames(Status.values()) : gameService.listGames(statuses));
    }
}
