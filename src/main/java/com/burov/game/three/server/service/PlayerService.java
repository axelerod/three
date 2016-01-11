package com.burov.game.three.server.service;

import com.burov.game.three.shared.model.Player;

public interface PlayerService {
    Player registerPlayer(Player name);
    Player getPlayer(String id);
}
