package com.burov.game.three.client.service.stage;

import com.burov.game.three.client.model.Context;
import com.burov.game.three.client.service.http.GameService;
import com.burov.game.three.client.service.input.UserCommunicationService;
import com.burov.game.three.shared.model.Game;
import com.burov.game.three.shared.model.Player;
import com.burov.game.three.shared.model.Status;
import com.google.common.base.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.function.BiFunction;

import static com.burov.game.three.client.model.Context.withFailedStage;

@Component
public class PlayStage implements Stage {

    public static final int BOUNDARY_VALUE = 1;
    private final long sleepTime;
    private final long repeatTimes;
    private final GameService gameService;
    private final UserCommunicationService communicationService;

    @Autowired
    public PlayStage(@Value("${three.client.api.timeout.millis}") long sleepTime,
                     @Value("${three.client.api.timeout.max-times}") long repeatTimes,
                     GameService gameService, UserCommunicationService communicationService) {
        this.sleepTime = sleepTime;
        this.repeatTimes = repeatTimes;
        this.gameService = gameService;
        this.communicationService = communicationService;
    }

    @Override
    public Context perform(Context context) {
        Game game = context.getGame();
        Preconditions.checkNotNull(game);
        Player player = context.getPlayer();
        Preconditions.checkNotNull(player);

        if (game.getStatus() == Status.WAITING_FOR_OPPONENT) {
            System.out.println("Waiting for opponent...");
            Optional<Game> updatedGame = waitForOpponent(game, this::isAbleToStartPlay);
            if (!updatedGame.isPresent()) {
                gameService.remove(game.getId());
                return withFailedStage();
            }
            System.out.println("Opponent connected:" + updatedGame.get().getSecondPlayer().getName() +
                    "let's wait for his turn");
            game = updatedGame.get();
        }

        return play(game, player);
    }

    private Context play(Game game, Player player) {
        System.out.println("Starting game:");
        printGameInfo(game);
        while (true) {
            if (game.getPerformedLastMove().equals(player)) {
                System.out.println("Should wait for opponent move");
                Optional<Game> updatedGame = waitForOpponent(game, this::isAbleToMove);
                if (!updatedGame.isPresent()) {
                    return withFailedStage();
                }
                game = updatedGame.get();
            }

            printGameInfo(game);

            Integer previousNumber = game.getNumber();
            if (previousNumber == BOUNDARY_VALUE) {
                System.out.println("Well done, your opponent reached 1. Game over!");
                return Context.success();
            }
            Integer newNumber = communicationService.enterNewNumber(previousNumber);
            Integer added = newNumber * 3 - previousNumber;
            game.setNumber(newNumber);
            game.setPreviosNumber(previousNumber);
            game.setAddedNumber(added);

            Optional<Game> afterMove = gameService.move(game.getId(), player.getId(), game);
            if (!afterMove.isPresent()) {
                return withFailedStage();
            }

            game = afterMove.get();

            if (newNumber == BOUNDARY_VALUE) {
                System.out.println("Well done, you reached 1! Game over.");
                return Context.success();
            }
        }
    }

    private void printGameInfo(Game game) {
        Integer number = game.getNumber();
        Integer previosNumber = game.getPreviosNumber();
        Integer addedNumber = game.getAddedNumber();
        System.out.println(
                (previosNumber == 0 ? "" :
                        " previous number: " + previosNumber + " added by opponent number: " + addedNumber)

                        + " current number:" + number);
    }

    private Optional<Game> waitForOpponent(Game game, BiFunction<Optional<Game>, Game, Boolean> canContinue) {

        int counter = 0;
        while (counter <= repeatTimes) {
            try {
                Thread.sleep(sleepTime);
                counter++;
                Optional<Game> updated = gameService.getGame(game.getId());

                if (canContinue.apply(updated, game)) {

                    return updated;
                } else if (updated.isPresent()) {
                    System.out.println("No changes, waiting...");
                }

                if (counter == repeatTimes) {
                    if (communicationService.shouldWaitAgain()) {
                        counter = 0;
                    } else {
                        return Optional.empty();
                    }
                }
            } catch (InterruptedException e) {
                System.out.println("Seems you don't want to wait any more!");
                return Optional.empty();
            }
        }
        return Optional.empty();
    }

    private boolean isAbleToStartPlay(Optional<Game> updated, Game old) {
        return updated.isPresent() && updated.get().getStatus() == Status.PLAYING;
    }

    private boolean isAbleToMove(Optional<Game> updated, Game old) {
        return updated.isPresent() && !updated.get().getPerformedLastMove().equals(old.getPerformedLastMove());
    }

}
