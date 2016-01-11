package com.burov.game.three.server.service;

import com.burov.game.three.shared.model.Game;
import com.burov.game.three.shared.model.Player;
import com.burov.game.three.shared.model.Status;

import java.util.List;

public interface GameService {
    List<Game> listGames(Status[] statuses);

    Game create(Game game);

    Game applyToGame(Player player, String gameId);

    Game move(Game game, String playerId, String gameId);
}
