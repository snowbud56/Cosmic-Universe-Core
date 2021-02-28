package com.snowbud56.player.event;

/*
* Created by snowbud56 on February 06, 2018
* Do not change or use this code without permission
*/

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class FoodLevelChange implements Listener {

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent e) {
        e.setCancelled(true);
        if (e.getEntity() instanceof Player) ((Player) e.getEntity()).setFoodLevel(25);
    }
}
