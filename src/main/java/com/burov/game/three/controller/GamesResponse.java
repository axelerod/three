package com.burov.game.three.controller;

import com.burov.game.three.model.Game;

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
