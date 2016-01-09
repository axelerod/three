package com.burov.game.three.model;

import com.google.common.base.Objects;

import javax.validation.constraints.NotNull;

public class Game {
    private String id;
    @NotNull
    private Integer startNumber;
    private Player firstPlayer;
    private Player secondPlayer;
    private Status status;

    public void setId(String id) {
        this.id = id;
    }



    public void setStartNumber(Integer startNumber) {
        this.startNumber = startNumber;
    }

    public void setFirstPlayer(Player firstPlayer) {
        this.firstPlayer = firstPlayer;
    }

    public Game() {
    }

    public Game(Integer startNumber) {

        this.startNumber = startNumber;
    }

    public Player getFirstPlayer() {
        return firstPlayer;
    }

    public Player getSecondPlayer() {
        return secondPlayer;
    }

    public Status getStatus() {
        return status;
    }

    public Integer getStartNumber() {
        return startNumber;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setSecondPlayer(Player secondPlayer) {
        this.secondPlayer = secondPlayer;
    }

    public String getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Game game = (Game) o;
        return Objects.equal(id, game.id) &&
                Objects.equal(firstPlayer, game.firstPlayer) &&
                Objects.equal(secondPlayer, game.secondPlayer) &&
                Objects.equal(status, game.status);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, firstPlayer, secondPlayer, status);
    }
}
