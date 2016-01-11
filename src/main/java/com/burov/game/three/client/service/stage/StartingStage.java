package com.burov.game.three.client.service.stage;

import com.burov.game.three.client.model.Context;
import com.burov.game.three.client.service.http.PlayerService;
import com.burov.game.three.client.service.input.UserCommunicationService;
import com.burov.game.three.shared.model.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static com.burov.game.three.client.model.Context.withFailedStage;

@Component
public class StartingStage implements Stage {

    private final UserCommunicationService communicationService;
    private final PlayerService playerService;

    @Autowired
    public StartingStage(UserCommunicationService communicationService,
                         PlayerService playerService) {
        this.communicationService = communicationService;
        this.playerService = playerService;
    }

    @Override
    public Context perform(Context context) {
        String userName = communicationService.getUserName();
        Optional<Player> registeredPlayer = playerService.registerPlayer(new Player(userName, null));
        if (!registeredPlayer.isPresent()) {
            return withFailedStage();
        }

        Player player = registeredPlayer.get();
        return Context.newContext(player);
    }

}
