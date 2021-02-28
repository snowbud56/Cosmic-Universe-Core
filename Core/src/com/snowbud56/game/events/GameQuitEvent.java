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
import org.bukkit.event.player.PlayerQuitEvent;

public class GameQuitEvent implements Listener {

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        if (Core.isGameServer()) {
            Player p = e.getPlayer();
            e.setQuitMessage(null);
            GameManager.getActiveGame().quitGame(p);
        }
    }
}
