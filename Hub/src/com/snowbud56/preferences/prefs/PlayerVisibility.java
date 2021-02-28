package com.snowbud56.preferences.prefs;

/*
* Created by snowbud56 on February 11, 2018
* Do not change or use this code without permission
*/

import com.snowbud56.player.PlayerManager;
import com.snowbud56.player.Rank;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PlayerVisibility {

    public static void enablePlayerVis(Player p) {
        for (Player target : Bukkit.getOnlinePlayers()) {
            if (!PlayerManager.getPlayer(target).getRank().Has(Rank.HELPER)) p.hidePlayer(target);
        }
    }
    public static void disablePlayerVis(Player p) {
        for (Player target : Bukkit.getOnlinePlayers()) {
            //TODO MAKE IT COMPATIBLE WITH VANISH WHEN VANISH IS MADE
            p.showPlayer(target);
        }
    }
}
