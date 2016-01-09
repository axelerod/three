package com.burov.game.three.service;

import com.burov.game.three.model.Game;
import com.burov.game.three.model.Player;
import com.burov.game.three.model.Status;
import com.google.common.collect.ImmutableMap;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

public class MapBasedGameServiceTest {

    public static final String NEW_GAME_ID = "New game id";
    public static final String PLAYING_GAME_ID = "Playing game id";
    public static final int START_NUMBER = 15;

    private Game gameWithNewStatus;
    private GameService testedInstance;

    @Before
    public void composeGames() {
        gameWithNewStatus = newGame(NEW_GAME_ID, Status.NEW);
        Game gameWithPlayingStatus = newGame(NEW_GAME_ID, Status.PLAYING);
        Map<String, Game> games = new HashMap<>();
        games.put(NEW_GAME_ID, gameWithNewStatus);
        games.put(PLAYING_GAME_ID, gameWithPlayingStatus);

        testedInstance = new MapBasedGameService(games);
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
        assertThat(savedGame.getStartNumber(), is(START_NUMBER));
        assertThat(savedGame.getStatus(), is(Status.NEW));
    }
}