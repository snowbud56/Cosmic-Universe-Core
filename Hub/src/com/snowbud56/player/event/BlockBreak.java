package com.snowbud56.player.event;

/*
* Created by snowbud56 on February 09, 2018
* Do not change or use this code without permission
*/

import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockBreak implements Listener {

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        if (e.getPlayer().getGameMode() != GameMode.CREATIVE)
            e.setCancelled(true);
    }
}
