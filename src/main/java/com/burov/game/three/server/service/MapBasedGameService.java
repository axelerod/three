package com.burov.game.three.server.service;

import com.burov.game.three.server.exceptions.*;
import com.burov.game.three.shared.model.Game;
import com.burov.game.three.shared.model.Player;
import com.burov.game.three.shared.model.Status;
import com.google.common.annotations.VisibleForTesting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import static java.util.stream.Collectors.toList;

@Service
public class MapBasedGameService implements GameService {
    private final Map<String, Game> games;

    private final PlayerService playerService;

    @Autowired
    public MapBasedGameService(PlayerService playerService) {
        this.playerService = playerService;
        this.games = new HashMap<>();
    }

    @VisibleForTesting
    MapBasedGameService(Map<String, Game> games, PlayerService playerService) {
        this.games = games;
        this.playerService = playerService;
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

    @Override
    public synchronized Game applyToGame(Player player, String gameId) {
        validatePlayer(player);
        Game game = games.get(gameId);
        validateGameForApplying(gameId, game);

        Player firstPlayer = game.getFirstPlayer();
        if (firstPlayer == null) {
            game.setFirstPlayer(player);
            game.setStatus(Status.WAITING_FOR_OPPONENT);
            game.setPerformedLastMove(player);
            return game;
        }
        Player secondPlayer = game.getSecondPlayer();

        if (secondPlayer == null) {
            game.setSecondPlayer(player);
            game.setStatus(Status.PLAYING);
        }
        return game;
    }

    @Override
    public synchronized Game move(Game updatedGame, String playerId, String gameId) {
        Game savedGame = games.get(gameId);
        if (savedGame == null) {
            throw new GameNotFoundException(String.format("Game '%s' not found", gameId));
        }
        if (savedGame.getStatus() == Status.FINISHED) {
            games.remove(gameId);
            return savedGame;
        }
        if (savedGame.getStatus() != Status.PLAYING) {
            throw new GameNotPlayingException("Game can't be played: opponent absent or game finished");
        }
        Player player = savedGame.getPlayer(playerId);
        if (player == null) {
            throw new PlayerNotAllowedException("Game is played by other players");
        }
        if (savedGame.getPerformedLastMove().getId().equals(playerId)) {
            throw new WrongTurnException("Please, wait for another player to move");
        }


        Integer number = updatedGame.getNumber();

        savedGame.setNumber(number);
        savedGame.setPerformedLastMove(player);
        if (1 == number) {
            savedGame.setStatus(Status.FINISHED);
        }

        return savedGame;

    }

    private void validateGameForApplying(String gameId, Game game) {
        if (game == null) {
            throw new GameNotFoundException(String.format("Game '%s' not found", gameId));
        }
        Status status = game.getStatus();
        if (!status.canApplyToGame()) {
            throw new GameAlreadyStartedException(String.format("Game '%s' already started", gameId));
        }
    }

    private void validatePlayer(Player player) {
        if (playerService.getPlayer(player.getId()) == null) {
            throw new UserNotRegisteredException(String.format("User '%s' not registered", player.getName()));
        }
    }
}
