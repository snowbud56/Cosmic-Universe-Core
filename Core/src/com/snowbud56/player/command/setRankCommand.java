package com.snowbud56.player.command;

/*
* Created by snowbud56 on January 08, 2018
* Do not change or use this code without permission
*/

import com.snowbud56.command.CommandBase;
import com.snowbud56.player.CorePlayer;
import com.snowbud56.player.PlayerManager;
import com.snowbud56.player.Rank;
import com.snowbud56.util.Chat;
import com.snowbud56.util.PlayersUtil;
import com.snowbud56.util.TabUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class setRankCommand extends CommandBase {

    public setRankCommand() {
        super(Rank.ADMIN, "setrank");
    }

    @Override
    public void execute(Player p, String[] args) {
        if (PlayerManager.getPlayer(p).isTestingRank()) {
            p.sendMessage(Chat.prefix + "You have a temporary rank, so you're unable to change ranks!");
            return;
        }
        if (args.length <= 1) {
            List<String> ranks = new ArrayList<>();
            for (Rank r : Rank.values()) ranks.add(r.name());
            p.sendMessage(Chat.prefix + Chat.mBody + "Set rank usages:\n" +
                    Chat.cDAqua + "/" + aliasUsed + " <player> <rank> " + Chat.mBody + "Set's a player's rank " + Rank.ADMIN.toString() + "\n" +
                    Chat.mBody + "Avaliable ranks: " + Chat.cRed + ranks.toString().replace("[", "").replace("]", ""));
            return;
        }
        Rank rank;
        try {
            rank = Rank.valueOf(args[1]);
        } catch (Exception ex) {
            List<String> ranks = new ArrayList<>();
            for (Rank r : Rank.values()) ranks.add(r.name());
            p.sendMessage(Chat.mHead + "Error> " + Chat.mBody + "That is an invalid rank!\n" +
                    Chat.mHead + "Error> " + Chat.mBody + "Avaliable ranks (case-sensitive): " + Chat.cRed + ranks.toString().replace("[", "").replace("]", ""));
            return;
        }
        List<String> playermatches = PlayersUtil.findPlayer(p, args[0], true);
        if (playermatches.size() == 1) {
            OfflinePlayer player = Bukkit.getOfflinePlayer(playermatches.get(0));
            try {
//                PreparedStatement st = Core.getConnection().prepareStatement("SELECT * FROM generalstats WHERE uuid = '" + player.getUniqueId().toString() + "'");
//                ResultSet set = st.executeQuery();
//                if (set.next()) {
//                    Core.getConnection().createStatement().execute("UPDATE generalstats SET rank = '" + rank.name() + "' WHERE uuid = '" + player.getUniqueId().toString() + "'");
//                } else {
//                    Core.getConnection().createStatement().execute("INSERT INTO generalstats (uuid, rank) VALUES ('" + player.getUniqueId().toString() + "', '" + rank.name() + "')");
//                }
                p.sendMessage(Chat.AdminPrefix + "You have successfully set " + Chat.adminElement(player.getName() + "'s ") + "rank to " + Chat.adminElement(rank.name()));
                System.out.println("[Core] " + p.getName() + " has set " + player.getName() + "'s rank to " + rank.name);
                Chat.messageRank(Chat.AdminPrefix + p.getName() + " has set " + player.getName() + "'s rank to " + rank.name, Rank.ADMIN);
                if (player.isOnline()) {
                    Player target = Bukkit.getPlayer(playermatches.get(0));
                    CorePlayer target1 = PlayerManager.getPlayer(target);
                    target1.setRank(rank);
                    target1.setDisplayRank(rank);
                    target.sendMessage(Chat.prefix + Chat.mBody + "Your rank is now " + Chat.cRed + rank.name);
                    TabUtils.updatePlayerPrefixRank(target);
                    if (target.getScoreboard().getTeam("sb-rank") != null) target.getScoreboard().getTeam("sb-rank").setSuffix(rank.color + rank.name);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                p.sendMessage(Chat.prefix + Chat.cRed + "There was an error with saving " + player.getName() + "'s rank. Please contact a developer.");
            }
        } else {
            p.sendMessage(Chat.playerNotFound(args[0], playermatches));
        }
    }

//    @Override
//    public List<String> onTabComplete(Player p, String[] args) {
//        List<String> matches = new ArrayList<>();
//        if (args.length == 1) {
//            for (Player target : Bukkit.getOnlinePlayers()) {
//                if (target.getDisplayName().toLowerCase().startsWith(args[args.length-1].toLowerCase())) matches.add(target.getDisplayName());
//            }
//        } else if (args.length == 2) {
//            for (Rank rank : Rank.values()) {
//                if (rank.name.toLowerCase().startsWith(args[args.length-1].toLowerCase())) matches.add(rank.name);
//            }
//        }
//        return matches;
//    }
}
