package com.burov.game.three.server.service;

import com.burov.game.three.shared.model.Player;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class MapBasedPlayerService implements PlayerService {

    private Map<String,Player> players = new ConcurrentHashMap<>();

    @Override
    public Player registerPlayer(String name) {
        String userUuid = UUID.randomUUID().toString();
        Player player = new Player(name, userUuid);
        players.put(userUuid, player);

        return player;
    }

    @Override
    public Player getPlayer(String id) {
        return players.get(id);
    }
}
