package com.snowbud56.player.events;

/*
* Created by snowbud56 on February 04, 2018
* Do not change or use this code without permission
*/

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

import java.util.Iterator;

public class ServerListPing implements Listener {

    @EventHandler
    public void onServerListPing(ServerListPingEvent e) {
        e.setMaxPlayers(e.getNumPlayers() + 1);
        e.setMotd(ChatColor.translateAlternateColorCodes('&', "        &c&m------&7&m[&8&m-&r &9&lCosmic &6&lUniverse &7&m-&8&m]&c&m------&r"));
    }
}
