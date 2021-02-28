package com.snowbud56.game.events;

/*
 * Created by snowbud56 on April 21, 2019
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
import org.bukkit.event.player.PlayerInteractEvent;

public class GameLobbyDamage implements Listener {

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent e) {
        if (!Core.isGameServer()) return;
        Game game = GameManager.getActiveGame();
        if (e.getEntity() instanceof Player)
            if (game.isState(GameState.LOBBY) || game.isState(GameState.STARTING) || game.isState(GameState.ENDING))
                e.setCancelled(true);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        if (!Core.isGameServer()) return;
        Game game = GameManager.getActiveGame();
        if (game.isState(GameState.LOBBY) || game.isState(GameState.STARTING) || game.isState(GameState.ENDING))
            e.setCancelled(true);
    }
}
