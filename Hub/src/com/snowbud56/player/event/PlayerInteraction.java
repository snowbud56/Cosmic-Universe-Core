package com.snowbud56.player.event;

/*
* Created by snowbud56 on March 28, 2018
* Do not change or use this code without permission
*/

import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerInteraction implements Listener {

    @EventHandler
    public void playerInteractEvent(PlayerInteractEvent e) {
        if (e.getPlayer().getGameMode() != GameMode.CREATIVE) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void PlayerEntityInteract(PlayerInteractAtEntityEvent e) {
        if (e.getPlayer().getGameMode() != GameMode.CREATIVE) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void playerTeleportToSpawn(PlayerMoveEvent e) {
        if (e.getTo().getY() <= -50) {
            e.getPlayer().teleport(LobbyJoin.spawnPoint);
        }
    }
}
