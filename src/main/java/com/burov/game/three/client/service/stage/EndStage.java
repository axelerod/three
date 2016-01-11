package com.burov.game.three.client.service.stage;

import com.burov.game.three.client.model.Context;
import com.burov.game.three.client.service.input.UserCommunicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EndStage implements Stage {

    private final UserCommunicationService communicationService;

    @Autowired
    public EndStage(UserCommunicationService communicationService) {
        this.communicationService = communicationService;
    }

    @Override
    public Context perform(Context context) {
        boolean shouldFinish = communicationService.shouldFinish();
        return Context.copyWithNewResult(context, shouldFinish);
    }
}
