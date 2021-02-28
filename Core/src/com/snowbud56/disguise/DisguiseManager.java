package com.snowbud56.disguise;

/*
* Created by snowbud56 on January 08, 2018
* Do not change or use this code without permission
*/

import com.snowbud56.Core;
import com.snowbud56.disguise.events.PlayerNicknameEvent;
import com.snowbud56.util.managers.LogManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

public class DisguiseManager implements Listener {

    private static Boolean nicksDisabled = false;

    @EventHandler
    public void nicknameEvent(PlayerNicknameEvent e) {
        e.setCancelled(nicksDisabled);
    }

    public static void setNicksDisabled(Boolean nicksDisabled) {
        DisguiseManager.nicksDisabled = nicksDisabled;
    }

    public static Boolean getNicksDisabled() {
        return nicksDisabled;
    }

    public static Map<Player, PlayerDisguise> disguises = new HashMap<>();

    public static void setPlayerDisguise(Player p, PlayerDisguise disguise) {
        disguises.put(p, disguise);
    }

    public static PlayerDisguise getPlayerDisguise(Player p) {
        return disguises.get(p);
    }

    @EventHandler
    public void playerJoinDisguiseUpdate(PlayerJoinEvent event) {
        for (Map.Entry<Player, PlayerDisguise> playerDisguise : disguises.entrySet()) {
            event.getPlayer().hidePlayer(playerDisguise.getKey());
            new BukkitRunnable() {
                @Override
                public void run() {
                    playerDisguise.getValue().update(playerDisguise.getKey(), event.getPlayer());
                }
            }.runTaskLater(Core.getPlugin(), 20L);
        }
        LogManager.logConsole("Disguises updated for " + event.getPlayer().getName());
    }
}
