package com.burov.game.three.service;

import com.burov.game.three.model.Game;
import com.burov.game.three.model.Status;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.stream.Collectors.toList;

@Service
public class MapBasedGameService implements GameService {
    private final Map<String,Game> games;

    @VisibleForTesting
    MapBasedGameService(Map<String, Game> games) {
        this.games = games;
    }

    public MapBasedGameService() {
        this.games = new ConcurrentHashMap<>();
    }

    @Override
    public List<Game> listGames(Status... statuses) {
        List<Status> statusList = Arrays.asList(statuses);
        return games.entrySet()
                .stream()
                .filter(k -> statusList.contains(k.getValue().getStatus()))
                .map(Map.Entry::getValue)
                .collect(toList());
    }
}
