package com.snowbud56.player;

/*
* Created by snowbud56 on January 08, 2018
* Do not change or use this code without permission
*/

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class PlayerManager {

    public static Map<OfflinePlayer, CorePlayer> players = new HashMap<>();

    public static CorePlayer getPlayer(Player p) {
        return players.get(p);
    }

    public static void addPlayer(Player p, CorePlayer player) {
        players.put(p, player);
    }
}
