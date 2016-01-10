package com.burov.game.three.server.controller;

import com.burov.game.three.shared.model.Game;

import java.util.List;

public class GamesResponse extends ResponseWrapper{
    private List<Game> games;

    public GamesResponse(List<Game> games) {
        this.games = games;
    }

    public List<Game> getGames() {
        return games;
    }
}
