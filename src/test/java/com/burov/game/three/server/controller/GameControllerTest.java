package com.burov.game.three.server.controller;

import com.burov.game.three.server.ServerApplication;
import com.burov.game.three.server.exceptions.GameAlreadyStartedException;
import com.burov.game.three.server.service.GameService;
import com.burov.game.three.shared.model.Game;
import com.burov.game.three.shared.model.Player;
import com.burov.game.three.shared.model.Status;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@SpringApplicationConfiguration({ServerApplication.class, MockConfiguration.class})
public class GameControllerTest {

    public static final String GAME_ID = "GameId";
    public static final String PLAYER_ID = "PlayerId";
    public static final String PLAYER_NAME = "Player name";
    public static final int START_NUMBER = 15;
    public static final int ANOTHER_NUMBER = 5;

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
        mockMvc.perform(get("/games").accept(GameController.VERSION_HEADER))
                .andExpect(status().isCreated());
    }

    @Test
    public void shouldRespondWithContentTypeVersion() throws Exception {
        mockMvc.perform(get("/games").accept(GameController.VERSION_HEADER))
                .andExpect(content().contentTypeCompatibleWith(GameController.VERSION_HEADER));
    }

    @Test
    public void shouldNotAcceptWithOtherHeader() throws Exception {
        mockMvc.perform(get("/games").accept(MediaType.APPLICATION_ATOM_XML_VALUE))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void shouldNotValidateGameWithoutStartNumber() throws Exception {
        mockMvc.perform(post("/games")
                .accept(GameController.VERSION_HEADER)
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(newGameWithoutNumberAsJson()))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldSaveNewGame() throws Exception {
        when(getGameService().create(any(Game.class))).thenReturn(gameWithId(START_NUMBER));

        mockMvc.perform(post("/games")
                .accept(GameController.VERSION_HEADER)
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(newGameAsJson()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(GAME_ID))
                .andExpect(jsonPath("$.number").value(START_NUMBER));
    }

    private Game gameWithId(int startNumber) {
        Game game = new Game(startNumber, null, null);
        game.setId(GAME_ID);
        return game;
    }

    private String newGameAsJson() {
        return "{\"id\":null,\"number\":15,\"addedNumber\":0,\"previosNumber\":0,\"firstPlayer\":null,\"secondPlayer\":null,\"status\":null}";
    }

    private String newGameWithoutNumberAsJson() {
        return "{\"id\":null,\"number\":null,\"firstPlayer\":null,\"secondPlayer\":null,\"status\":null}";
    }

    @Test
    public void shouldLoadListOfGames() throws Exception {
        when(getGameService().listGames(new Status[]{Status.NEW})).thenReturn(listOfGames());

        mockMvc.perform(get("/games").accept(GameController.VERSION_HEADER)
                .param("statuses", Status.NEW.name()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.games[0].id").value(GAME_ID))
                .andExpect(jsonPath("$.games[0].number").value(START_NUMBER));
    }

    @Test
    public void shouldLoadGamesWithAllStatuses() throws Exception {
        when(getGameService().listGames(Status.values())).thenReturn(null);

        mockMvc.perform(get("/games").accept(GameController.VERSION_HEADER));

        verify(getGameService()).listGames(Status.values());
    }

    @Test
    public void shouldLoadGame() throws Exception {
        when(getGameService().getGame(GAME_ID)).thenReturn(gameWithId(13));

        mockMvc.perform(get("/games/{gameId}", GAME_ID)
                .accept(GameController.VERSION_HEADER))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(GAME_ID))
                .andExpect(jsonPath("$.number").value(13));
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
    public void shouldValidateApplyInCaseGameIdNotDefined() throws Exception {
        mockMvc.perform(post("/games/{gameId}/players/{playerId}", "", PLAYER_ID)
                .content(playerAsJson())
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void shouldValidateApplyInCasePlayerIdNotDefined() throws Exception {
        mockMvc.perform(post("/games/{gameId}/players/{playerId}", GAME_ID, "")
                .content(playerAsJson())
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void shouldValidateMoveInCasePlayerIdNotDefined() throws Exception {
        mockMvc.perform(put("/games/{gameId}/players/{playerId}", GAME_ID, "")
                .content(newGameAsJson())
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void shouldValidateMoveInCaseGameIdNotDefined() throws Exception {
        mockMvc.perform(put("/games/{gameId}/players/{playerId}", "", PLAYER_ID)
                .content(newGameAsJson())
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void shouldValidateMoveInCaseGameWithoutNumber() throws Exception {
        mockMvc.perform(put("/games/{gameId}/players/{playerId}", GAME_ID, PLAYER_ID)
                .content(newGameWithoutNumberAsJson())
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void shouldRespondAcceptedOnMoveIfSuccess() throws Exception {
        mockMvc.perform(put("/games/{gameId}/players/{playerId}", GAME_ID, PLAYER_ID)
                .content(newGameAsJson())
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isAccepted());
    }

    @Test
    public void shouldReturnUpdatedModel() throws Exception {
        when(getGameService().move(any(Game.class), eq(PLAYER_ID), eq(GAME_ID))).thenReturn(gameWithId(ANOTHER_NUMBER));

        mockMvc.perform(put("/games/{gameId}/players/{playerId}", GAME_ID, PLAYER_ID)
                .accept(GameController.VERSION_HEADER)
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(newGameAsJson()))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.id").value(GAME_ID))
                .andExpect(jsonPath("$.number").value(ANOTHER_NUMBER));
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
        Game game = new Game(START_NUMBER, null, null);
        game.setId(GAME_ID);
        return ImmutableList.of(game);
    }
}