package com.burov.game.three.service;

import com.burov.game.three.exceptions.GameAlreadyStartedException;
import com.burov.game.three.exceptions.GameNotFoundException;
import com.burov.game.three.exceptions.UserNotRegisteredException;
import com.burov.game.three.model.Game;
import com.burov.game.three.model.Player;
import com.burov.game.three.model.Status;
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

    @Rule
    public ExpectedException exceptionThrown = ExpectedException.none();

    @Mock
    private PlayerService playerService;

    private Game gameWithNewStatus;
    private GameService testedInstance;
    private Map<String, Game> games;

    @Before
    public void composeGames() {
        gameWithNewStatus = newGame(NEW_GAME_ID, Status.NEW);
        Game gameWithPlayingStatus = newGame(NEW_GAME_ID, Status.PLAYING);
        games = new HashMap<>();
        games.put(NEW_GAME_ID, gameWithNewStatus);
        games.put(PLAYING_GAME_ID, gameWithPlayingStatus);

        testedInstance = new MapBasedGameService(games, playerService);
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
        Game game = new Game(START_NUMBER);
        game.setId(gameId);
        game.setStatus(status);
        return game;
    }

    @Test
    public void shouldSaveGame() {
        Game savedGame = testedInstance.create(new Game(START_NUMBER));

        assertThat(savedGame.getId().length(), greaterThan(1));
        assertThat(savedGame.getNumber(), is(START_NUMBER));
        assertThat(savedGame.getStatus(), is(Status.NEW));
    }
}