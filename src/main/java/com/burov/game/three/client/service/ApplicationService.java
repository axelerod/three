package com.burov.game.three.client.service;

import com.google.common.collect.ImmutableList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ApplicationService {

    private final InputReaderService inputReaderService;
    private final InputValidator inputValidator;

    @Autowired
    public ApplicationService(InputReaderService inputReaderService, InputValidator inputValidator) {
        this.inputReaderService = inputReaderService;
        this.inputValidator = inputValidator;
    }

    public void start() {
        String userName = inputReaderService.getString("Enter your name: ");
        boolean startNewGame = readKindOfGame();
    }

    private boolean readKindOfGame() {
        boolean rightAnswer = false;

        String kindOfGameResponse = null;
        while (!rightAnswer) {
            ImmutableList<String> responseValues = ImmutableList.of("C", "A");
            kindOfGameResponse = inputReaderService.getString(
                    "Do you want to Create game or Apply to existing one?",
                    responseValues);
            rightAnswer = inputValidator.validate(kindOfGameResponse, responseValues);
        }

        return "A".equals(kindOfGameResponse);
    }
}
