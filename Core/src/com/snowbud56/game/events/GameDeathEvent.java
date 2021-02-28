package com.snowbud56.game.events;

/*
 * Created by snowbud56 on April 14, 2019
 * Do not change or use this code without permission
 */

import com.snowbud56.Core;
import com.snowbud56.game.Game;
import com.snowbud56.game.GameManager;
import com.snowbud56.game.GameState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class GameDeathEvent implements Listener {

    @EventHandler
    public void onDeath(EntityDamageEvent e) {
        if (Core.isGameServer()) {
            Game game = GameManager.getActiveGame();
            if (e.getEntity() instanceof Player) {
                Player p = (Player) e.getEntity();
                if (p.getHealth() - e.getDamage() <= 0) {
                    if (game.isState(GameState.ACTIVE) || game.isState(GameState.ENDING)) {
                        e.setCancelled(true);
                    } else {
                        p.teleport(GameManager.lobbyPoint);
                    }
                }
            }
        }
    }
}
