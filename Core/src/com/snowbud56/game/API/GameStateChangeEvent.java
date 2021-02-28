package com.snowbud56.game.API;

/*
 * Created by snowbud56 on April 14, 2019
 * Do not change or use this code without permission
 */

import com.snowbud56.game.GameState;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GameStateChangeEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private GameState fromState, toState;

    public GameStateChangeEvent(GameState fromState, GameState toState) {
        this.fromState = fromState;
        this.toState = toState;
    }

    public GameState getFromState() {
        return fromState;
    }

    public GameState getToState() {
        return toState;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
