package com.burov.game.three.client.service.stage;

import com.burov.game.three.client.model.Context;
import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class GameManager {
    private final StartingStage startingStage;
    private final PlayStage playStage;
    private final EndStage endStage;
    private final SelectGameStage selectGameStage;
    private Map<Stage, Directions> stagesGraph;

    @Autowired
    public GameManager(StartingStage startingStage, PlayStage playStage, EndStage endStage, SelectGameStage selectGameStage) {
        this.startingStage = startingStage;
        this.playStage = playStage;
        this.endStage = endStage;
        this.selectGameStage = selectGameStage;
        initGraph();
    }

    private void initGraph() {
        stagesGraph = ImmutableMap.<Stage, Directions>builder()
                .put(startingStage, Directions.direct(selectGameStage, endStage))
                .put(selectGameStage, Directions.direct(playStage, endStage))
                .put(playStage, Directions.direct(endStage, endStage))
                .put(endStage, Directions.direct(null, playStage))
                .build();
    }

    public void runGame() {
        Stage stage = startingStage;
        Context context = Context.withFailedStage();
        while (true) {
            context = stage.perform(context);
            Directions directions = stagesGraph.get(stage);
            if (context.isFinishedStage()) {
                stage = directions.getOnSuccess();
            } else {
                stage = directions.getOnFailure();
            }
            if (stage == null) {
                break;
            }
        }
    }

    private static class Directions {
        private final Stage onSuccess;
        private final Stage onFailure;

        private Directions(Stage onSuccess, Stage onFailure) {
            this.onSuccess = onSuccess;
            this.onFailure = onFailure;

        }

        public Stage getOnSuccess() {
            return onSuccess;
        }

        public Stage getOnFailure() {
            return onFailure;
        }

        private static Directions direct(Stage success, Stage failure) {
            return new Directions(success, failure);
        }
    }
}
