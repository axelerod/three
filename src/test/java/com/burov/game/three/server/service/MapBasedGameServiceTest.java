package com.burov.game.three.server.service;

import com.burov.game.three.server.exceptions.*;
import com.burov.game.three.shared.model.Game;
import com.burov.game.three.shared.model.Player;
import com.burov.game.three.shared.model.Status;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MapBasedGameServiceTest {

    public static final String NEW_GAME_ID = "New game id";
    public static final String PLAYING_GAME_ID = "Playing game id";
    public static final int START_NUMBER = 15;
    public static final String PLAYER_ID = "player Id";
    public static final String PLAYER_NAME = "Player name";
    public static final String ABSENT_GAME_ID = "absent game id";
    public static final String ABSENT_PLAYER = "absent player";
    public static final String GAME_ID_WITH_PLAYERS = "some game id";
    public static final String SECOND_PLAYER_NAME = "second player name";
    public static final String SECOND_PLAYER_ID = "second player id";

    @Rule
    public ExpectedException exceptionThrown = ExpectedException.none();

    @Mock
    private PlayerService playerService;

    private Game gameWithNewStatus;
    private GameService testedInstance;
    private Map<String, Game> games;
    private Game gameWithPlayers;

    @Before
    public void composeGames() {
        gameWithNewStatus = newGame(NEW_GAME_ID, Status.NEW);
        Game gameWithPlayingStatus = newGame(PLAYING_GAME_ID, Status.PLAYING);
        gameWithPlayers = newGameWithPlayers(GAME_ID_WITH_PLAYERS);

        games = new HashMap<>();
        games.put(NEW_GAME_ID, gameWithNewStatus);
        games.put(PLAYING_GAME_ID, gameWithPlayingStatus);
        games.put(GAME_ID_WITH_PLAYERS, gameWithPlayers);

        testedInstance = new MapBasedGameService(games, playerService);
    }

    private Game newGameWithPlayers(String gameId) {
        Game game = newGame(gameId, Status.PLAYING);
        game.setFirstPlayer(new Player(PLAYER_NAME, PLAYER_ID));
        game.setSecondPlayer(new Player(SECOND_PLAYER_NAME, SECOND_PLAYER_ID));
        game.setPerformedLastMove(new Player(PLAYER_NAME, PLAYER_ID));
        return game;
    }

    @Test
    public void shouldThrowExceptionIfUserAppliedIsNotRegistered() {
        exceptionThrown.expectMessage("User 'Player name' not registered");
        exceptionThrown.expect(UserNotRegisteredException.class);

        testedInstance.applyToGame(new Player(PLAYER_NAME, PLAYER_ID), ABSENT_GAME_ID);
    }

    @Test
    public void shouldThrowExceptionIfGameNotFound() {
        when(playerService.getPlayer(PLAYER_ID)).thenReturn(new Player());

        exceptionThrown.expectMessage("Game 'absent game id' not found");
        exceptionThrown.expect(GameNotFoundException.class);

        testedInstance.applyToGame(new Player(PLAYER_NAME, PLAYER_ID), ABSENT_GAME_ID);
    }

    @Test
    public void shouldApplyPlayerAsFirstOneInCaseItIsNotApplied() {
        Player player = new Player(PLAYER_NAME, PLAYER_ID);
        when(playerService.getPlayer(PLAYER_ID)).thenReturn(player);

        testedInstance.applyToGame(player, NEW_GAME_ID);

        Player firstPlayer = games.get(NEW_GAME_ID).getFirstPlayer();
        assertThat(firstPlayer, is(notNullValue()));
        assertThat(firstPlayer.getName(), is(PLAYER_NAME));
        assertThat(firstPlayer.getId(), is(PLAYER_ID));
    }

    @Test
    public void shouldApplyPlayerAsSecondOneInCaseFirstAlreadyApplied() {
        gameWithNewStatus.setFirstPlayer(new Player("first player", "firt player id"));
        Player player = new Player(PLAYER_NAME, PLAYER_ID);
        when(playerService.getPlayer(PLAYER_ID)).thenReturn(player);

        testedInstance.applyToGame(player, NEW_GAME_ID);

        Player secondPlayer = games.get(NEW_GAME_ID).getSecondPlayer();
        assertThat(secondPlayer, is(notNullValue()));
        assertThat(secondPlayer.getName(), is(PLAYER_NAME));
        assertThat(secondPlayer.getId(), is(PLAYER_ID));
    }

    @Test
    public void shouldChangeGameStatusWhenApplyingFirstPlayer() {
        when(playerService.getPlayer(PLAYER_ID)).thenReturn(new Player(PLAYER_NAME, PLAYER_ID));

        testedInstance.applyToGame(new Player(PLAYER_NAME, PLAYER_ID), NEW_GAME_ID);

        Game game = games.get(NEW_GAME_ID);
        assertThat(game.getStatus(), is(Status.WAITING_FOR_OPPONENT));
    }

    @Test
    public void shouldChangeGameStatusWhenApplyingSecondPlayer() {
        gameWithNewStatus.setFirstPlayer(new Player("first player", "first player id"));

        when(playerService.getPlayer(PLAYER_ID)).thenReturn(new Player(PLAYER_NAME, PLAYER_ID));

        testedInstance.applyToGame(new Player(PLAYER_NAME, PLAYER_ID), NEW_GAME_ID);

        Game game = games.get(NEW_GAME_ID);
        assertThat(game.getStatus(), is(Status.PLAYING));
    }

    @Test
    public void shouldThrowExceptionInCaseGameStarted() {
        gameWithNewStatus.setStatus(Status.PLAYING);
        Player player = new Player(PLAYER_NAME, PLAYER_ID);
        when(playerService.getPlayer(PLAYER_ID)).thenReturn(player);
        exceptionThrown.expectMessage("Game '" + NEW_GAME_ID + "' already started");
        exceptionThrown.expect(GameAlreadyStartedException.class);

        testedInstance.applyToGame(player, NEW_GAME_ID);
    }

    @Test
    public void shouldReturnGamesByStatus() {
        List<Game> actualResult = testedInstance.listGames(new Status[]{Status.NEW});

        assertThat(actualResult, hasSize(1));
        assertThat(actualResult.get(0), is(gameWithNewStatus));
    }

    @Test
    public void shouldReturnEmptyListGamesDoesNotSatisfyStatus() {

        List<Game> actualResult = testedInstance.listGames(new Status[]{Status.FINISHED});

        assertThat(actualResult, hasSize(0));
    }

    private Game newGame(String gameId, Status status) {
        Game game = new Game(START_NUMBER, null, null);
        game.setId(gameId);
        game.setStatus(status);
        return game;
    }

    @Test
    public void shouldSaveGame() {
        Game savedGame = testedInstance.create(new Game(START_NUMBER, null, null));

        assertThat(savedGame.getId().length(), greaterThan(1));
        assertThat(savedGame.getNumber(), is(START_NUMBER));
        assertThat(savedGame.getStatus(), is(Status.NEW));
    }

    @Test
    public void shouldValidateGameExistenceOnMove() {
        exceptionThrown.expect(GameNotFoundException.class);

        testedInstance.move(newGame(ABSENT_GAME_ID, Status.NEW), PLAYER_ID, ABSENT_GAME_ID);
    }

    @Test
    public void shouldValidateGameStatusOnMove() {
        exceptionThrown.expect(GameNotPlayingException.class);

        testedInstance.move(newGame(NEW_GAME_ID, Status.NEW), PLAYER_ID, NEW_GAME_ID);
    }

    @Test
    public void shouldValidatePlayerOnMove() {
        exceptionThrown.expect(PlayerNotAllowedException.class);

        testedInstance.move(newGame(GAME_ID_WITH_PLAYERS, Status.NEW), ABSENT_PLAYER, GAME_ID_WITH_PLAYERS);
    }

    @Test
    public void shouldValidatePlayerTurnOnMove() {
        exceptionThrown.expect(WrongTurnException.class);

        testedInstance.move(newGame(GAME_ID_WITH_PLAYERS, Status.NEW), PLAYER_ID, GAME_ID_WITH_PLAYERS);
    }

    @Test
    public void shouldUpdateGame() {
        Integer numberToUpdate = 13;
        Game game = newGame(GAME_ID_WITH_PLAYERS, Status.PLAYING);
        game.setNumber(numberToUpdate);

        Game updatedGame = testedInstance.move(game, SECOND_PLAYER_ID, GAME_ID_WITH_PLAYERS);

        assertThat(updatedGame.getNumber(), is(13));
        assertThat(updatedGame.getPerformedLastMove().getId(), is(SECOND_PLAYER_ID));
        assertThat(game.getStatus(), is(Status.PLAYING));
    }

    @Test
    public void shouldUpdateGameStatusInCaseNewNumberIsOne() {
        Integer numberToUpdate = 1;
        Game game = newGame(GAME_ID_WITH_PLAYERS, Status.PLAYING);
        game.setNumber(numberToUpdate);

        Game updatedGame = testedInstance.move(game, SECOND_PLAYER_ID, GAME_ID_WITH_PLAYERS);

        assertThat(updatedGame.getNumber(), is(1));
        assertThat(updatedGame.getStatus(), is(Status.FINISHED));
    }

    @Test
    public void shouldRemoveGameInCaseAnotherPlayerIsGoingToMove() {
        gameWithPlayers.setStatus(Status.FINISHED);
        Game game = newGame(GAME_ID_WITH_PLAYERS, Status.FINISHED);

        testedInstance.move(game, SECOND_PLAYER_ID, GAME_ID_WITH_PLAYERS);

        assertNull(games.get(GAME_ID_WITH_PLAYERS));
    }
}