package com.burov.game.three.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Game is played by others")
public class GameNotPlayingException extends RuntimeException {
    public GameNotPlayingException(String message) {
    }
}
