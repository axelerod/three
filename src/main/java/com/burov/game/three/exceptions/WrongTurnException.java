package com.burov.game.three.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Move out of turn is not allowed")
public class WrongTurnException extends RuntimeException{
    public WrongTurnException(String message) {
        super(message);
    }
}
