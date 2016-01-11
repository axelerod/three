package com.burov.game.three.client.service;

import com.burov.game.three.client.service.http.GameService;
import com.burov.game.three.client.service.http.PlayerService;
import com.burov.game.three.client.service.input.UserCommunicationService;
import com.burov.game.three.shared.model.Player;
import com.burov.game.three.shared.model.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ApplicationService {

    private final UserCommunicationService communicationService;
    private final PlayerService playerService;
    private final GameService gameService;

    @Autowired
    public ApplicationService(UserCommunicationService communicationService,
                              PlayerService playerService,
                              GameService gameService) {
        this.communicationService = communicationService;
        this.playerService = playerService;
        this.gameService = gameService;
    }

    public void start() {
        String userName = communicationService.getUserName();
        Optional<Player> player = playerService.registerPlayer(userName);
        if (!player.isPresent()) {
            return;
        }

        boolean startNewGame = communicationService.shouldStartNewGame();

        if (startNewGame) {
            return;
        } else {
            gameService.listGames(Status.WAITING_FOR_OPPONENT);
        }
    }
}
