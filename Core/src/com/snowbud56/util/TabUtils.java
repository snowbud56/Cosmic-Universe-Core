package com.snowbud56.util;

/*
* Created by snowbud56 on January 08, 2018
* Do not change or use this code without permission
*/

import com.snowbud56.player.CorePlayer;
import com.snowbud56.player.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class TabUtils {

    public static void updatePlayerPrefixRank(Player p) {
        CorePlayer player = PlayerManager.getPlayer(p);
        Scoreboard board = p.getScoreboard();
        for (Player target : Bukkit.getOnlinePlayers()) {
            if (target.equals(p)) continue;
            Team team = target.getScoreboard().getTeam(p.getName());
            if (team == null) team = target.getScoreboard().registerNewTeam(p.getName());
            team.setPrefix(player.getDisplayRank().getTag(false, true, true) + "§f");
            team.addEntry(p.getName());
        }
        for (Player target : Bukkit.getOnlinePlayers()) {
            Team team = board.getTeam(target.getName());
            if (team == null) team = board.registerNewTeam(target.getName());
            team.setPrefix(PlayerManager.getPlayer(target).getDisplayRank().getTag(false, true, true) + "§f");
            team.addEntry(target.getName());
        }
    }

    public static void setPlayerPrefix(Player p, String prefix) {
        Scoreboard board = p.getScoreboard();
        for (Player target : Bukkit.getOnlinePlayers()) {
            if (target.equals(p)) continue;
            Team team = target.getScoreboard().getTeam(p.getName());
            if (team == null) team = target.getScoreboard().registerNewTeam(p.getName());
            team.setPrefix(prefix);
            team.addEntry(p.getName());
        }
        for (Player target : Bukkit.getOnlinePlayers()) {
            Team team = board.getTeam(target.getName());
            if (team == null) team = board.registerNewTeam(target.getName());
            team.setPrefix(prefix);
            team.addEntry(target.getName());
        }
    }

    public static void setPlayerSuffix(Player p, String suffix) {
        Scoreboard board = p.getScoreboard();
        for (Player target : Bukkit.getOnlinePlayers()) {
            if (target.equals(p)) continue;
            Team team = target.getScoreboard().getTeam(p.getName());
            if (team == null) team = target.getScoreboard().registerNewTeam(p.getName());
            team.setSuffix(suffix);
            team.addEntry(p.getName());
        }
        for (Player target : Bukkit.getOnlinePlayers()) {
            Team team = board.getTeam(target.getName());
            if (team == null) team = board.registerNewTeam(target.getName());
            team.setSuffix(suffix);
            team.addEntry(target.getName());
        }
    }
}
