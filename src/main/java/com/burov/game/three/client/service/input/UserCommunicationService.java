package com.burov.game.three.client.service.input;

import com.burov.game.three.client.service.validation.InputValidator;
import com.google.common.collect.ImmutableList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
        return binaryQuestion("C", "A",
                "Do you want to [C]reate game or [A]pply to existing one?");
    }

    private boolean binaryQuestion(String mappedToTrue, String mappedToFalse, String message) {

        ImmutableList<String> responses = ImmutableList.of(mappedToTrue, mappedToFalse);
        String selectedValue = selectValue(message, responses);

        return mappedToTrue.equals(selectedValue);
    }

    private String selectValue(String message, List<String> responses) {
        boolean rightAnswer = false;
        String enteredValue = null;
        while (!rightAnswer) {
            enteredValue = inputReaderService.getString(message, responses);
            rightAnswer = inputValidator.validate(enteredValue, responses);
        }
        return enteredValue;
    }

    public boolean shouldFinish() {
        return binaryQuestion("E", "P", "Do you want to [E]xit or [P]lay again?");
    }

    public String selectGame(List<String> opponents) {
        return selectValue("Please select your opponent by entering his name.", opponents);
    }

    public Integer enterNumber() {
        boolean rightAnswer = false;
        String enteredValue = null;
        while (!rightAnswer) {
            enteredValue = inputReaderService.getString("Please, enter start number: ");
            rightAnswer = inputValidator.validateInteger(enteredValue);
        }

        return Integer.parseInt(enteredValue);
    }
}
