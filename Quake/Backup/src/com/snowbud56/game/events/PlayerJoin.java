package com.snowbud56.game.API;

import com.snowbud56.QuakeMinigame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoin implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        e.setJoinMessage(null);
        Player p = e.getPlayer();
        p.setMaxHealth(20);
        p.setHealth(p.getMaxHealth());
        p.setFoodLevel(25);
        QuakeMinigame.getGame().joinGame(p);
    }
}
