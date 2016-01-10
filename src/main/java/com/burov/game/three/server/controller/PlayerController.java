package com.burov.game.three.server.controller;

import com.burov.game.three.server.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/players", produces = PlayerController.VERSION_HEADER)
public class PlayerController {
    public static final String VERSION_HEADER = "application/vnd.burov.three.v1+json";

    private final PlayerService playerService;

    @Autowired
    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    /**
     * Creates new player for game
     *
     * @param name user name. Required, if not set error response with 400 code returned.
     * @return player entity
     */
    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public PlayerResponse newPlayer(@RequestParam("name") String name) {
        return new PlayerResponse(playerService.registerPlayer(name));
    }
}
