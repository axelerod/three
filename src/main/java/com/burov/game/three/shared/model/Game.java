package com.burov.game.three.shared.model;

import com.google.common.base.Objects;

import javax.validation.constraints.NotNull;

public class Game {
    private String id;
    @NotNull
    private Integer number;
    private Player firstPlayer;
    private Player secondPlayer;
    private Status status;
    private Player performedLastMove;
    @NotNull
    private Integer previosNumber;
    @NotNull
    private Integer addedNumber;

    public Game() {
    }

    public Game(Integer number, Integer previosNumber, Integer addedNumber) {
        this.number = number;
        this.previosNumber = previosNumber;
        this.addedNumber = addedNumber;
    }

    public Integer getAddedNumber() {
        return addedNumber;
    }

    public void setAddedNumber(Integer addedNumber) {
        this.addedNumber = addedNumber;
    }

    public Integer getPreviosNumber() {
        return previosNumber;
    }

    public void setPreviosNumber(Integer previosNumber) {
        this.previosNumber = previosNumber;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public void setFirstPlayer(Player firstPlayer) {
        this.firstPlayer = firstPlayer;
    }

    public Player getPerformedLastMove() {
        return performedLastMove;
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

    public Integer getNumber() {
        return number;
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
        return Objects.equal(id, game.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    public void setPerformedLastMove(Player performedLastMove) {
        this.performedLastMove = performedLastMove;
    }

    public Player getPlayer(String playerId) {
        return firstPlayer.getId().equals(playerId) ? firstPlayer
                : secondPlayer.getId().equals(playerId) ? secondPlayer : null;
    }
}
