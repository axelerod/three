package com.burov.game.three.server.controller;

import com.burov.game.three.shared.model.Player;

public class PlayerResponse {
    private final Player player;

    public PlayerResponse(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }
}
