package com.burov.game.three.controller;

import com.burov.game.three.model.Player;

public class PlayerResponse {
    private final Player player;

    public PlayerResponse(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }
}
