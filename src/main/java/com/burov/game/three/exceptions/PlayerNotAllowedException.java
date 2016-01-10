package com.burov.game.three.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Game is played by others")
public class PlayerNotAllowedException extends RuntimeException{
    public PlayerNotAllowedException(String message) {
        super(message);
    }
}
