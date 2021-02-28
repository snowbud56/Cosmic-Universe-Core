package com.snowbud56.player.command;

/*
* Created by snowbud56 on February 11, 2018
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
import org.bukkit.entity.Player;

import java.util.List;

public class TempRank extends CommandBase {

    public TempRank() {
        super(Rank.ADMIN, "temprank");
    }

    @Override
    public void execute(Player p, String[] args) {
        if (args.length < 2) p.sendMessage(Chat.prefix + "Usage: /temprank <player> <rank>");
        else {
            Rank rank;
            try {
                rank = Rank.valueOf(args[1]);
            } catch (IllegalArgumentException e) {
                p.sendMessage(Chat.prefix + "Invalid Rank!");
                return;
            }
            List<String> matches = PlayersUtil.findPlayer(p, args[0], false);
            if (matches.size() == 1) {
                Player target = Bukkit.getPlayer(matches.get(0));
                CorePlayer t = PlayerManager.getPlayer(target);
                t.setRank(rank);
                t.setDisplayRank(rank);
                t.setTestingRank(true);
                p.sendMessage(Chat.prefix + "Successfully set " + Chat.cRed + target.getName() + Chat.cGray + "'s rank to " + Chat.cRed + rank.name + Chat.cGray + " temporarily.");
                target.sendMessage(Chat.prefix + "Your rank is now " + rank.name + " until you relog.");
                TabUtils.updatePlayerPrefixRank(target);
                if (target.getScoreboard().getTeam("sb-rank") != null) target.getScoreboard().getTeam("sb-rank").setSuffix(rank.color + rank.name);
                Chat.messageRank(Chat.prefix + p.getName() + " gave " + target.getName() + " temporary " + rank.name + ".", Rank.ADMIN);
            } else p.sendMessage(Chat.playerNotFound(args[0], matches));
        }
    }
}
