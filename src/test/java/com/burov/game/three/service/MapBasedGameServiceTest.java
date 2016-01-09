package com.burov.game.three.service;

import com.burov.game.three.model.Game;
import com.burov.game.three.model.Player;
import com.burov.game.three.model.Status;
import com.google.common.collect.ImmutableMap;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

public class MapBasedGameServiceTest {

    public static final String NEW_GAME_ID = "New game id";
    public static final String PLAYING_GAME_ID = "Playing game id";

    private Game gameWithNewStatus;
    private GameService testedInstance;

    @Before
    public void composeGames() {
        gameWithNewStatus = newGame(NEW_GAME_ID, Status.NEW);
        Game gameWithPlayingStatus = newGame(NEW_GAME_ID, Status.PLAYING);
        Map<String, Game> games = ImmutableMap.of(NEW_GAME_ID, gameWithNewStatus, PLAYING_GAME_ID, gameWithPlayingStatus);
        testedInstance = new MapBasedGameService(games);
    }

    @Test
    public void shouldReturnGamesByStatus() {

        List<Game> actualResult = testedInstance.listGames(Status.NEW);

        assertThat(actualResult, hasSize(1));
        assertThat(actualResult.get(0), is(gameWithNewStatus));
    }

    @Test
    public void shouldReturnEmptyListGamesDoesNotSatisfyStatus() {

        List<Game> actualResult = testedInstance.listGames(Status.FINISHED);

        assertThat(actualResult, hasSize(0));
    }

    private Game newGame(String gameId, Status status) {
        Game game = new Game(gameId, new Player("Player name", "Player id"));
        game.setStatus(status);
        return game;
    }
}