package com.burov.game.three.server.controller;

import com.burov.game.three.server.service.GameService;
import com.burov.game.three.server.service.PlayerService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import static org.mockito.Mockito.mock;

@Configuration
public class MockConfiguration {

    private GameService gameService = mock(GameService.class);
    private PlayerService playerService = mock(PlayerService.class);

    @Bean
    @Primary
    GameService gameService() {
        return gameService;
    }

    @Bean
    @Primary
    PlayerService playerService() {
        return playerService;
    }
}


