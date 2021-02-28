package com.snowbud56.gameselector.events;

/*
* Created by snowbud56 on March 21, 2018
* Do not change or use this code without permission
*/

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.snowbud56.Hub;
import com.snowbud56.gameselector.GameType;
import com.snowbud56.gameselector.menu.GameMenu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class GameSelector implements Listener {

    @EventHandler
    public void onIntect(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (p.getItemInHand() != null && p.getItemInHand().getType() == Material.COMPASS) {
            GameMenu menu = new GameMenu(p);
            menu.openMenu();
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if (e.getInventory().getTitle().equals("Game Selector")) {
            if (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR) return;
            e.setCancelled(true);
            GameType type = GameType.getType(e.getSlot());
            if (type == null) return;
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("Connect");
            out.writeUTF(type.getServer());
            p.sendPluginMessage(Hub.getInstance(), "BungeeCord", out.toByteArray());
        }
    }
}
