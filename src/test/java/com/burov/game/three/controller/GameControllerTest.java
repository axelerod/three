package com.burov.game.three.controller;

import com.burov.game.three.Application;
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

import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@SpringApplicationConfiguration({Application.class, MockConfiguration.class})
public class GameControllerTest {

    public static final String GAME_ID = "Game Id";
    public static final String PLAYER_ID = "Player Id";
    public static final String PLAYER_NAME = "Player name";

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
    public void shouldLoadListOfGames() throws Exception {
        when(getGameService().listGames(new Status[]{Status.NEW})).thenReturn(listOfGames());

        mockMvc.perform(get("/games").accept(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .param("statuses", Status.NEW.name()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.games[0].id").value(GAME_ID))
                .andExpect(jsonPath("$.games[0].firstPlayer.name").value(PLAYER_NAME))
                .andExpect(jsonPath("$.games[0].firstPlayer.id").value(PLAYER_ID));
    }

    @Test
    public void shouldLoadGamesWithAllStatuses() throws Exception {
        when(getGameService().listGames(Status.values())).thenReturn(null);

        mockMvc.perform(get("/games").accept(MediaType.APPLICATION_JSON_UTF8_VALUE));

        verify(getGameService()).listGames(Status.values());
    }

    private GameService getGameService() {
        return mockConfiguration.gameService();
    }

    private List<Game> listOfGames() {
        return ImmutableList.of(new Game(GAME_ID, new Player(PLAYER_NAME, PLAYER_ID)));
    }
}