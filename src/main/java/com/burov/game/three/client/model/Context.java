package com.burov.game.three.client.model;

import com.burov.game.three.shared.model.Game;
import com.burov.game.three.shared.model.Player;

public class Context {
    private final Player player;
    private final Game game;
    private final boolean finishedStage;


    private Context(Game game, Player player, boolean finished) {
        this.player = player;
        this.game = game;
        this.finishedStage = finished;
    }

    public Player getPlayer() {
        return player;
    }

    public Game getGame() {
        return game;
    }

    public boolean isFinishedStage() {
        return finishedStage;
    }

    public static Context withFailedStage() {
        return new Context(null, null, false);
    }

    public static Context copyWithNewResult(Context context, boolean result) {
        return new Context(context.getGame(), context.getPlayer(), result);
    }

    public static Context newContext(Player player) {
        return new Context(null, player, true);
    }

    public static Context newContext(Game game, Player player) {
        return new Context(game, player, true);
    }

    public static Context success() {
        return new Context(null,null, true);
    }
}
