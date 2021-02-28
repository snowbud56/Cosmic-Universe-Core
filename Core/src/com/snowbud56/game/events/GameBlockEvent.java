package com.snowbud56.game.events;

/*
 * Created by snowbud56 on April 15, 2019
 * Do not change or use this code without permission
 */

import com.snowbud56.Core;
import com.snowbud56.game.GameManager;
import com.snowbud56.game.GameState;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class GameBlockEvent implements Listener {

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        if (Core.isGameServer()) {
            if (GameManager.getActiveGame().isState(GameState.LOBBY)) {
                Player p = e.getPlayer();
                if (p.getGameMode().equals(GameMode.SURVIVAL))
                    e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        if (Core.isGameServer()) {
            if (GameManager.getActiveGame().isState(GameState.LOBBY)) {
                Player p = e.getPlayer();
                if (p.getGameMode().equals(GameMode.SURVIVAL))
                    e.setCancelled(true);
            }
        }
    }
}
