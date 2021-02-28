package com.snowbud56.game.API;

import com.snowbud56.QuakeMinigame;
import com.snowbud56.game.constructors.Game;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class PlayerDamage implements Listener {
    @EventHandler
    public void onPlayerDamage(EntityDamageEvent e) {
        Game game = QuakeMinigame.getGame();
        if (e.getEntity() instanceof Player) {
            if (game.isState(Game.GameState.ACTIVE)){
                EntityDamageEvent.DamageCause cause = e.getCause();
                if (cause.equals(EntityDamageEvent.DamageCause.ENTITY_ATTACK) || cause.equals(EntityDamageEvent.DamageCause.FALL) || cause.equals(EntityDamageEvent.DamageCause.BLOCK_EXPLOSION) || cause.equals(EntityDamageEvent.DamageCause.ENTITY_EXPLOSION)) {
                    e.setCancelled(true);
                }
            } else e.setCancelled(true);
        }
    }
}
