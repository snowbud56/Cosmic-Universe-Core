package com.snowbud56.game.API;

/*
 * Created by snowbud56 on April 14, 2019
 * Do not change or use this code without permission
 */

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class GameJoinEvent extends PlayerEvent {

    private static final HandlerList handlers = new HandlerList();

    public GameJoinEvent(Player player) {
        super(player);
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
