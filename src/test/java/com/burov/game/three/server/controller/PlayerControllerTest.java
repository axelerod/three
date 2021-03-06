package com.burov.game.three.server.controller;

import com.burov.game.three.server.ServerApplication;
import com.burov.game.three.server.service.PlayerService;
import com.burov.game.three.shared.model.Player;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@SpringApplicationConfiguration({ServerApplication.class, MockConfiguration.class})
public class PlayerControllerTest {

    public static final String PLAYER_NAME = "user name";
    public static final String PLAYER_ID = "player id";
    @Autowired
    private WebApplicationContext context;
    @Autowired
    private MockConfiguration mockConfiguration;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    public void shouldReturnCreatedStatusOnSuccess() throws Exception {
        mockMvc.perform(post("/players")
                .accept(PlayerController.VERSION_HEADER)
                .content(asJson())
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().is(HttpStatus.CREATED.value()));
//                .andExpect(content().contentType(PlayerController.VERSION_HEADER));
    }

    private String asJson() {
        return "{\"name\":\"user name\",\"id\":\"player id\"}";
    }

    @Test
    public void shouldReturnBadRequestInCaseNameNotPassed() throws Exception {
        mockMvc.perform(post("/players")
                .accept(PlayerController.VERSION_HEADER)
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldRegisterNewPlayer() throws Exception {
        when(getPlayerService().registerPlayer(any(Player.class))).thenReturn(player());

        mockMvc.perform(post("/players")
                .accept(PlayerController.VERSION_HEADER)
                .content(asJson())
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.id").value(PLAYER_ID))
                .andExpect(jsonPath("$.name").value(PLAYER_NAME));
    }

    private Player player() {
        return new Player(PLAYER_NAME, PLAYER_ID);
    }

    private PlayerService getPlayerService() {
        return mockConfiguration.playerService();
    }
}