package com.burov.game.three.shared.model;

import com.google.common.base.MoreObjects;

import javax.validation.constraints.NotNull;

public class Player {
    @NotNull
    private String name;
    @NotNull
    private String id;

    public Player() {
    }

    public Player(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("name", name)
                .add("id", id)
                .toString();
    }
}
