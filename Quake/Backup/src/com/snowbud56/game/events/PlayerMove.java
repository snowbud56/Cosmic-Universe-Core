package com.snowbud56.game.API;

import com.snowbud56.QuakeMinigame;
import com.snowbud56.game.constructors.Game;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMove implements Listener {
    @EventHandler
    public void PlayerMoveEvent(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        Game game = QuakeMinigame.getGame();
        if (game.isMovementFrozen()) {
            if (e.getFrom().getBlockX() != e.getTo().getBlockX() || e.getFrom().getBlockZ() != e.getTo().getBlockZ()) {
                p.teleport(e.getFrom());
            }
        }
    }
}
