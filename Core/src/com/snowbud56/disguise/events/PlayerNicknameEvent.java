package com.snowbud56.disguise.events;

/*
* Created by snowbud56 on March 19, 2018
* Do not change or use this code without permission
*/

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerNicknameEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled = false;
    private Player player;

    public PlayerNicknameEvent(Player who) {
        player = who;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }


    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public Player getPlayer() {
        return player;
    }
}
