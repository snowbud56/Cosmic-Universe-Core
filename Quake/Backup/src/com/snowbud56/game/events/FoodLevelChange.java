package com.snowbud56.game.API;


import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class FoodLevelChange implements Listener {
    @EventHandler
    public void FoodChange(FoodLevelChangeEvent e) {
        e.setCancelled(true);
    }
}
