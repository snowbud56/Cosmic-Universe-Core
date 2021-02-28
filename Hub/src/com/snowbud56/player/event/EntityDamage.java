package com.snowbud56.player.event;

/*
* Created by snowbud56 on February 08, 2018
* Do not change or use this code without permission
*/

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

public class EntityDamage implements Listener {

    @EventHandler
    public void onEntityDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player){
            e.setCancelled(true);
            ((Player) e.getEntity()).setHealth(((Player) e.getEntity()).getMaxHealth());
        }
        else if (e.getCause() != EntityDamageEvent.DamageCause.FALL
                        && e.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK
                && e.getCause() != EntityDamageEvent.DamageCause.DROWNING) {
                            e.setCancelled(true);
        }
    }

    @EventHandler
    public void playerDeathEvent(PlayerDeathEvent e) {
        e.setDeathMessage(null);
    }
}
