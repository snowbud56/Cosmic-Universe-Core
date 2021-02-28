package com.snowbud56.game.events;

/*
 * Created by snowbud56 on April 14, 2019
 * Do not change or use this code without permission
 */

import com.snowbud56.Core;
import com.snowbud56.game.GameManager;
import com.snowbud56.player.PlayerManager;
import com.snowbud56.util.TabUtils;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class GameJoinEvent implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        if (Core.isGameServer()) {
            e.setJoinMessage(null);
            Player p = e.getPlayer();
            p.setMaxHealth(20);
            p.setHealth(p.getMaxHealth());
            p.setFoodLevel(25);
            p.setGameMode(GameMode.SURVIVAL);
            TabUtils.setPlayerPrefix(p, PlayerManager.getPlayer(p).getRank().color + "");
            GameManager.getActiveGame().joinGame(p, false, false);
        }
    }
}
