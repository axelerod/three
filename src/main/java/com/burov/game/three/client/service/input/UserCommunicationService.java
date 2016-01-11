package com.burov.game.three.client.service.input;

import com.burov.game.three.client.service.validation.InputValidator;
import com.google.common.collect.ImmutableList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserCommunicationService {
    private final InputReaderService inputReaderService;
    private final InputValidator inputValidator;

    @Autowired
    public UserCommunicationService(InputReaderService inputReaderService, InputValidator inputValidator) {
        this.inputReaderService = inputReaderService;
        this.inputValidator = inputValidator;
    }

    public String getUserName() {
        return inputReaderService.getString("Enter your name: ");
    }


    public boolean shouldStartNewGame() {
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
