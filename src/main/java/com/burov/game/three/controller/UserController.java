package com.burov.game.three.controller;

import com.burov.game.three.model.Player;
import com.burov.game.three.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final PlayerService playerService;

    @Autowired
    public UserController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public PlayerResponse newPlayer(@RequestParam("name") String name) {
        return new PlayerResponse(playerService.registerPlayer(name));
    }
}
