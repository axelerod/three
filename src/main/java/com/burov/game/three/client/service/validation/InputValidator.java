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
}
