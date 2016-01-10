package com.burov.game.three.service;

import com.burov.game.three.model.Game;
import com.burov.game.three.model.Player;
import com.burov.game.three.model.Status;

import java.util.List;

public interface GameService {
    List<Game> listGames(Status[] statuses);

    Game create(Game game);

    void applyToGame(Player player, String gameId);

    Game move(Game game, String playerId, String gameId);
}
