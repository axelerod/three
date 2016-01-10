package com.burov.game.three.server.service;

import com.burov.game.three.shared.model.Player;

public interface PlayerService {
    Player registerPlayer(String name);
    Player getPlayer(String id);
}
