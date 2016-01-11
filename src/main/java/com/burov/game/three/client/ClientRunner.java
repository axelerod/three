package com.burov.game.three.client;

import com.burov.game.three.client.service.stage.GameManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class ClientRunner implements CommandLineRunner{

    private final GameManager gameManager;

    @Autowired
    public ClientRunner(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @Override
    public void run(String... args) throws Exception {
        gameManager.runGame();
    }
}
