package com.snowbud56.player.event;

/*
* Created by snowbud56 on February 19, 2018
* Do not change or use this code without permission
*/

import org.bukkit.GameMode;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftInventoryCrafting;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;

public class InventoryManager implements Listener {

    @EventHandler
    public void InventoryClickHandler(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if (e.getInventory() instanceof CraftInventoryCrafting) {
            if (p.getGameMode() != GameMode.CREATIVE) e.setCancelled(true);
        }
    }

    @EventHandler
    public void ItemDropHandler(PlayerDropItemEvent e) {
        Player p = e.getPlayer();
        if (p.getGameMode() != GameMode.CREATIVE) e.setCancelled(true);
    }
}
