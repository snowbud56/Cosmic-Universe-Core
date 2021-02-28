package com.snowbud56.util;

/*
* Created by snowbud56 on January 08, 2018
* Do not change or use this code without permission
*/

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class PlayersUtil {

    public static List<String> findPlayer(Player p, String name, Boolean offlineIfNotFound) {
        List<String> matches = new ArrayList<>();
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getDisplayName().toLowerCase().startsWith(name.toLowerCase())) matches.add(player.getName());
        }
        if (matches.size() == 0 && offlineIfNotFound) {
            p.sendMessage(Chat.prefix + "No online matches for " + Chat.cRed + name + Chat.cGray + ". Trying an offline search...");
            matches = findOfflinePlayer(name);
        }
        return matches;
    }

    private static List<String> findOfflinePlayer(String name) {
        List<String> matches = new ArrayList<>();
        for (OfflinePlayer p : Bukkit.getOfflinePlayers()) {
            if (p.getName().toLowerCase().startsWith(name.toLowerCase()) && !matches.contains(p.getName())) matches.add(p.getName());
        }
        return matches;
    }

}
