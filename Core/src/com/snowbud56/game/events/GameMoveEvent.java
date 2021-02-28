package com.snowbud56.game.events;

/*
 * Created by snowbud56 on April 14, 2019
 * Do not change or use this code without permission
 */

import com.snowbud56.Core;
import com.snowbud56.game.GameManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class GameMoveEvent implements Listener {
    @EventHandler
    public void PlayerMoveEvent(PlayerMoveEvent e) {
        if (Core.isGameServer()) {
            Player p = e.getPlayer();
            if (!GameManager.getActiveGame().canMove(p)) {
                if (e.getFrom().getBlockX() != e.getTo().getBlockX() || e.getFrom().getBlockZ() != e.getTo().getBlockZ()) {
                    p.teleport(e.getFrom());
                }
            }
        }
    }
}
