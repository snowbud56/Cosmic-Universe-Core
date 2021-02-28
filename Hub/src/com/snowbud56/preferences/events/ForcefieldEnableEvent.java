package com.snowbud56.preferences.events;

/*
* Created by snowbud56 on February 05, 2018
* Do not change or use this code without permission
*/

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

public class ForcefieldEnableEvent extends PlayerEvent {
    private static final HandlerList handlers = new HandlerList();

    public ForcefieldEnableEvent(Player who) {
        super(who);
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
