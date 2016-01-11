package com.burov.game.three.client.service.stage;

import com.burov.game.three.client.model.Context;
import com.burov.game.three.client.service.http.GameService;
import com.burov.game.three.client.service.input.UserCommunicationService;
import com.burov.game.three.server.controller.GamesResponse;
import com.burov.game.three.shared.model.Game;
import com.burov.game.three.shared.model.Player;
import com.burov.game.three.shared.model.Status;
import com.google.common.base.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

import static com.burov.game.three.client.model.Context.withFailedStage;
import static java.util.stream.Collectors.toList;

@Component
public class SelectGameStage implements Stage {

    private final UserCommunicationService communicationService;
    private final GameService gameService;

    @Autowired
    public SelectGameStage(UserCommunicationService communicationService, GameService gameService) {
        this.communicationService = communicationService;
        this.gameService = gameService;
    }

    @Override
    public Context perform(Context context) {
        Player player = context.getPlayer();
        Preconditions.checkNotNull(player);
        boolean startNewGame = communicationService.shouldStartNewGame();

        return startNewGame ? newGame(player) : selectAmongGames(player);
    }


    private Context selectAmongGames(Player player) {
        Optional<GamesResponse> gamesResponse = gameService.listGames(Status.WAITING_FOR_OPPONENT);
        if (!gamesResponse.isPresent()) {
            return withFailedStage();
        }

        List<Game> games = gamesResponse.get().getGames();

        if (games.isEmpty()) {
            System.out.println("No games available for applying");
            return withFailedStage();
        }

        List<String> opponents = getOpponentNames(games);
        String opponentName = communicationService.selectGame(opponents);
        Optional<Game> selectedGame = getSelectedGame(games, opponentName);

        if (!selectedGame.isPresent()) {
            return withFailedStage();
        }

        Optional<Game> game = gameService.applyToGame(selectedGame.get().getId(), player.getId(), player);

        return game.isPresent() ?
                Context.newContext(game.get(), player)
                : withFailedStage();
    }

    private List<String> getOpponentNames(List<Game> games) {
        return games
                .stream()
                .map(g -> g.getFirstPlayer().getName())
                .collect(toList());
    }

    private Optional<Game> getSelectedGame(List<Game> games, String opponentName) {
        return games.stream()
                .filter(g -> g.getFirstPlayer().getName().equals(opponentName))
                .findFirst();
    }

    private Context newGame(Player player) {
        Integer enteredNumber = communicationService.enterNumber();
        Optional<Game> createdGame = gameService.newGame(new Game(enteredNumber, 0, 0));
        if (!createdGame.isPresent()) {
            return withFailedStage();
        }
        Optional<Game> gameWithApplyedUser = gameService.applyToGame(createdGame.get().getId(), player.getId(), player);

        return gameWithApplyedUser.isPresent() ?
                Context.newContext(gameWithApplyedUser.get(), player)
                : withFailedStage();
    }
}
