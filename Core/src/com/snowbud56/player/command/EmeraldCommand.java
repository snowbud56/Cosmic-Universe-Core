package com.snowbud56.player.command;

/*
* Created by snowbud56 on February 09, 2018
* Do not change or use this code without permission
*/

import com.snowbud56.Core;
import com.snowbud56.command.CommandBase;
import com.snowbud56.player.PlayerManager;
import com.snowbud56.player.Rank;
import com.snowbud56.util.Chat;
import com.snowbud56.util.PlayersUtil;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Team;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class EmeraldCommand extends CommandBase {
    public EmeraldCommand() {
        super(Rank.ADMIN, "emerald");
    }

    @Override
    public void execute(Player p, String[] args) {
        if (PlayerManager.getPlayer(p).isTestingRank()) {
            p.sendMessage(Chat.prefix + "You have a testing rank, so you're unable to change ranks!");
            return;
        }
        if (args.length < 3 || !(args[0].toLowerCase().equals("set") || args[0].toLowerCase().equals("take") || args[0].toLowerCase().equals("give"))) {
            p.sendMessage(Chat.prefix + "Usage: /emerald <give | take | set> <player> <amount>");
            return;
        }
        new BukkitRunnable() {
            public void run() {
                Integer amount;
                final Integer[] emeralds = new Integer[1];
                try {
                    amount = Integer.parseInt(args[2]);
                } catch (Exception e) {
                    p.sendMessage(Chat.prefix + Chat.cRed + args[2] + Chat.cGray + " is not a valid number!");
                    return;
                }
                List<String> matches = PlayersUtil.findPlayer(p, args[1], true);
                if (matches.size() == 1) {
                    OfflinePlayer target = Bukkit.getOfflinePlayer(matches.get(0));
                    if (!args[0].toLowerCase().equals("set")) {
                        if (target.isOnline()) emeralds[0] = PlayerManager.getPlayer((Player) target).getEmeralds();
                        else {
                            try {
                                PreparedStatement s = Core.getConnection().prepareStatement("SELECT * FROM generalstats WHERE uuid = '" + target.getUniqueId().toString() + "'");
                                ResultSet set = s.executeQuery();
                                if (set.next()) {
                                    emeralds[0] = set.getInt("emeralds");
                                } else {
                                    Statement s1 = Core.getConnection().createStatement();
                                    s1.executeUpdate("INSERT INTO `generalstats` (uuid, rank, emeralds) VALUES ('" + target.getUniqueId().toString() + "', 'ALL', '0')");
                                    emeralds[0] = 0;
                                }
                            } catch (SQLException e) {
                                e.printStackTrace();
                                p.sendMessage(Chat.prefix + "There was an error with the database. Please try again.");
                            }
                        }
                    }
                    Integer updated;
                    switch (args[0]) {
                        case "give":
                            updated = emeralds[0] + amount;
                            break;
                        case "take":
                            updated = emeralds[0] - amount;
                            break;
                        case "set":
                            updated = amount;
                            break;
                        default:
                            return;
                    }
                    try {
                        PreparedStatement s = Core.getConnection().prepareStatement("UPDATE generalstats SET emeralds = '" + updated + "' WHERE uuid = '" + target.getUniqueId().toString() + "'");
                        s.executeUpdate();
                        p.sendMessage(Chat.prefix + Chat.cRed + target.getName() + Chat.cGray + " now has " + Chat.cRed + updated + " emeralds. " + (args[0].toLowerCase().equals("set") ? "" : Chat.cGray + "(Before: " + emeralds[0] + ")"));
                        if (target.isOnline()) {
                            Player t = (Player) target;
                            t.sendMessage(Chat.prefix + "You now have " + Chat.cRed + updated + " emeralds" + Chat.cGray + ".");
                            Team team = t.getScoreboard().getTeam("sb-emeralds");
                            if (team != null) team.setSuffix("§a" + updated);
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                        p.sendMessage(Chat.prefix + "There was an error with the database. Please try again.");
                    }
                } else p.sendMessage(Chat.prefix + (matches.size() == 0 ? Chat.cRed + args[1] + Chat.cGray + " has never joined the server in the past!" : "Too many matches! Did you mean: " + Chat.cRed + matches.toString().replace("[", "").replace("]", "")));
            }
        }.runTaskAsynchronously(Core.getPlugin());
    }
}
