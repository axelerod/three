package com.burov.game.three.controller;

import com.burov.game.three.Application;
import com.burov.game.three.model.Player;
import com.burov.game.three.service.PlayerService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@SpringApplicationConfiguration({Application.class, MockConfiguration.class})
public class UserControllerTest {

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
//        reset(getGameService());
    }

    @Test
    public void shouldReturnCreatedStatusOnSuccess() throws Exception {
        mockMvc.perform(post("/users")
                .param("name", PLAYER_NAME))
                .andExpect(status().is(HttpStatus.CREATED.value()));
    }

    @Test
    public void shouldReturnBadRequestInCaseNameNotPassed() throws Exception {
        mockMvc.perform(post("/users"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldRegisterNewPlayer() throws Exception {
        when(getPlayerService().registerPlayer(PLAYER_NAME)).thenReturn(player());

        mockMvc.perform(post("/users")
                .param("name", PLAYER_NAME))
                .andExpect(jsonPath("$.player.id").value(PLAYER_ID))
                .andExpect(jsonPath("$.player.name").value(PLAYER_NAME));
    }

    private Player player() {
        return new Player(PLAYER_NAME, PLAYER_ID);
    }

    private PlayerService getPlayerService() {
        return mockConfiguration.playerService();
    }
}