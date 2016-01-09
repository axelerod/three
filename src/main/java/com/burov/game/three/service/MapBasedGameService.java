package com.burov.game.three.service;

import com.burov.game.three.model.Game;
import com.burov.game.three.model.Status;
import com.google.common.annotations.VisibleForTesting;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.stream.Collectors.toList;

@Service
public class MapBasedGameService implements GameService {
    private final Map<String, Game> games;

    @VisibleForTesting
    MapBasedGameService(Map<String, Game> games) {
        this.games = games;
    }

    public MapBasedGameService() {
        this.games = new HashMap<>();
    }

    @Override
    public synchronized List<Game> listGames(Status[] statuses) {
        List<Status> statusList = Arrays.asList(statuses);
        return games.entrySet()
                .stream()
                .filter(k -> statusList.contains(k.getValue().getStatus()))
                .map(Map.Entry::getValue)
                .collect(toList());
    }

    @Override
    public synchronized Game create(Game game) {
        String gameUuid = UUID.randomUUID().toString();
        game.setId(gameUuid);
        game.setStatus(Status.NEW);
        games.put(gameUuid, game);

        return game;
    }
}
