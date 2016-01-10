package com.burov.game.three.shared.model;

public enum Status {
    NEW("New game initialized by player"),
    WAITING_FOR_OPPONENT("One player connected and waiting for opponent"),
    PLAYING("Game is currently in progress"),
    FINISHED("Game is finished");

    Status(String description) {
        this.description = description;
    }

    private String description;

    public boolean canApplyToGame() {
        return this == NEW || this == WAITING_FOR_OPPONENT;
    }
}