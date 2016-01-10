package com.burov.game.three.controller;

import com.burov.game.three.Application;
import com.burov.game.three.exceptions.GameAlreadyStartedException;
import com.burov.game.three.model.Game;
import com.burov.game.three.model.Player;
import com.burov.game.three.model.Status;
import com.burov.game.three.service.GameService;
import com.google.common.collect.ImmutableList;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@SpringApplicationConfiguration({Application.class, MockConfiguration.class})
public class GameControllerTest {

    public static final String GAME_ID = "GameId";
    public static final String PLAYER_ID = "PlayerId";
    public static final String PLAYER_NAME = "Player name";
    public static final int START_NUMBER = 15;

    @Autowired
    private WebApplicationContext context;
    @Autowired
    private MockConfiguration mockConfiguration;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        reset(getGameService());
    }

    @Test
    public void shouldAcceptJsonHeader() throws Exception {
        mockMvc.perform(get("/games").accept(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldRespondWithContentJsonType() throws Exception {
        mockMvc.perform(get("/games").accept(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }

    @Test
    public void shouldNotAcceptWithOtherHeader() throws Exception {
        mockMvc.perform(get("/games").accept(MediaType.APPLICATION_ATOM_XML_VALUE))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void shouldNotValidateGameWithoutStartNmber() throws Exception {
        mockMvc.perform(post("/games")
                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(newGameWithoutNumberAsJson()))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldSaveNewGame() throws Exception {
        when(getGameService().create(any(Game.class))).thenReturn(gameWithId());

        mockMvc.perform(post("/games")
                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(newGameAsJson()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(GAME_ID))
                .andExpect(jsonPath("$.number").value(START_NUMBER));
    }

    private Game gameWithId() {
        Game game = new Game(START_NUMBER);
        game.setId(GAME_ID);
        return game;
    }

    private String newGameAsJson() {
        return "{\"id\":null,\"number\":15,\"firstPlayer\":null,\"secondPlayer\":null,\"status\":null}";
    }

    private String newGameWithoutNumberAsJson() {
        return "{\"id\":null,\"number\":null,\"firstPlayer\":null,\"secondPlayer\":null,\"status\":null}";
    }

    @Test
    public void shouldLoadListOfGames() throws Exception {
        when(getGameService().listGames(new Status[]{Status.NEW})).thenReturn(listOfGames());

        mockMvc.perform(get("/games").accept(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .param("statuses", Status.NEW.name()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.games[0].id").value(GAME_ID))
                .andExpect(jsonPath("$.games[0].number").value(START_NUMBER));
    }

    @Test
    public void shouldLoadGamesWithAllStatuses() throws Exception {
        when(getGameService().listGames(Status.values())).thenReturn(null);

        mockMvc.perform(get("/games").accept(MediaType.APPLICATION_JSON_UTF8_VALUE));

        verify(getGameService()).listGames(Status.values());
    }

    @Test
    public void shouldReturnAcceptedWhenApplyToGame() throws Exception {
        mockMvc.perform(post("/games/{gameId}/players/{playerId}", GAME_ID, PLAYER_ID)
                .content(playerAsJson())
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isAccepted());
    }

    @Test
    public void shouldValidatePlayer() throws Exception {
        mockMvc.perform(post("/games/{gameId}/players/{playerId}", GAME_ID, PLAYER_ID)
                .content(playerWithoutNameAsJson())
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturnConflictInCaseGameAlreadyStarted() throws Exception {
        doThrow(new GameAlreadyStartedException("Already started"))
                .when(getGameService()).applyToGame(any(Player.class), eq(GAME_ID));

        mockMvc.perform(post("/games/{gameId}/players/{playerId}", GAME_ID, PLAYER_ID)
                .content(playerAsJson())
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isConflict());
    }

    @Test
    public void shouldValidateInCaseGameIdNotDefined() throws Exception {
        mockMvc.perform(post("/games/{gameId}/players/{playerId}", "", PLAYER_ID)
                .content(playerAsJson())
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void shouldValidateInCasePlayerIdNotDefined() throws Exception {
        mockMvc.perform(post("/games/{gameId}/players/{playerId}", GAME_ID, "")
                .content(playerAsJson())
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().is4xxClientError());
    }

    private String playerAsJson() {
        return "{\"name\":\"user name\",\"id\":\"player id\"}";
    }

    private String playerWithoutNameAsJson() {
        return "{\"name\":null,\"id\":\"player id\"}";
    }

    private GameService getGameService() {
        return mockConfiguration.gameService();
    }

    private List<Game> listOfGames() {
        Game game = new Game(START_NUMBER);
        game.setId(GAME_ID);
        return ImmutableList.of(game);
    }
}