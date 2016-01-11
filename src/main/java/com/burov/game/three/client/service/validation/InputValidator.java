package com.burov.game.three.client.service.validation;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InputValidator {
    public boolean validate(String input, List<String> candidates) {
        return candidates.stream()
                .filter(c -> c.equals(input))
                .findFirst()
                .isPresent();
    }

    public boolean validateInteger(String input) {
        try {
            Integer.parseInt(input);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    public boolean validateInteger(String input, Integer baseValue) {
        try {
            int enteredValue = Integer.parseInt(input);
            return (Math.abs(enteredValue - baseValue) <= 1) && (enteredValue % 3 == 0);
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
