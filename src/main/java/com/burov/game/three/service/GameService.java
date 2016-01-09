package com.burov.game.three.service;

import com.burov.game.three.model.Game;
import com.burov.game.three.model.Status;

import java.util.List;

public interface GameService {
    List<Game> listGames(Status... statuses);
}
