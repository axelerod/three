package com.burov.game.three.model;

public enum Status {
    NEW("New game initialized by player"),
    PLAYING("Game is currently in progress"),
    FINISHED("Game is finished");

    Status(String description) {
        this.description = description;
    }

    private String description;
}