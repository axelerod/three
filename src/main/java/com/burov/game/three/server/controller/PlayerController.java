package com.burov.game.three.server.controller;

import com.burov.game.three.server.service.PlayerService;
import com.burov.game.three.shared.model.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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
     * @param player Required, if not set error response with 400 code returned.
     * @return player entity
     */
    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Player newPlayer(@RequestBody @Valid Player player) {
        return playerService.registerPlayer(player);
    }
}
