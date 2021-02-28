package com.snowbud56.player.events;

/*
 * Created by snowbud56 on March 25, 2019
 * Do not change or use this code without permission
 */

import com.snowbud56.player.PlayerManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerMoveEvent implements Listener {

    @EventHandler
    public void onPlayerMove(org.bukkit.event.player.PlayerMoveEvent e) {
        if (!PlayerManager.getPlayer(e.getPlayer()).canMove()) e.setTo(e.getFrom());
    }
}
