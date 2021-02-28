package com.snowbud56.updater;

/*
* Created by snowbud56 on February 09, 2018
* Do not change or use this code without permission
*/

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class UpdateEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private UpdateType _type;

    UpdateEvent(UpdateType example)
    {
        _type = example;
    }

    public UpdateType getType()
    {
        return _type;
    }

    public HandlerList getHandlers()
    {
        return handlers;
    }

    public static HandlerList getHandlerList()
    {
        return handlers;
    }
}
