package com.burov.game.three.controller;

import com.burov.game.three.service.GameService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import static org.mockito.Mockito.mock;

@Configuration
public class MockConfiguration {

    private GameService gameService = mock(GameService.class);

    @Bean
    @Primary
    GameService gameService() {
        return gameService;
    }
    }
