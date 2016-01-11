package com.burov.game.three.server.service;

import com.burov.game.three.shared.model.Player;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class MapBasedPlayerService implements PlayerService {

    private Map<String, Player> players = new ConcurrentHashMap<>();

    @Override
    public Player registerPlayer(Player player) {
        String userUuid = UUID.randomUUID().toString();
        player.setId(userUuid);
        players.put(userUuid, player);

        return player;
    }

    @Override
    public Player getPlayer(String id) {
        return players.get(id);
    }
}
