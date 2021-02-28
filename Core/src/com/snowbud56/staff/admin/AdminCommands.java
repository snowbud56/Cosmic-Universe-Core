package com.snowbud56.staff.admin;

/*
* Created by snowbud56 on January 28, 2018
* Do not change or use this code without permission
*/

import com.snowbud56.Core;
import com.snowbud56.command.CommandBase;
import com.snowbud56.player.Rank;
import com.snowbud56.util.Chat;
import com.snowbud56.util.PlayersUtil;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;

public class AdminCommands extends CommandBase {

    public AdminCommands() {
        super(Rank.ADMIN, "tps", "playerdata", "colormsg", "database");
    }

    @Override
    public void execute(Player p, String[] args) {
        if (aliasUsed.equals("colormsg")) {
            if (args.length == 0) p.sendMessage(Chat.prefix + "Usage: /" + aliasUsed + " <message>");
            else {
                StringBuilder msg = new StringBuilder();
                for (String arg : args) msg.append(arg).append(" ");
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', msg.toString()));
            }
            return;
        } else if (aliasUsed.equals("database")) {
            Core.establishConnection(false);
            return;
        } else if (aliasUsed.equals("playerdata")) {
            if (args.length == 0) p.sendMessage(Chat.prefix + "Usage: /playerdata <player>");
            else {
                List<String> matches = PlayersUtil.findPlayer(p, args[0], true);
                if (matches.size() == 1) {
                    p.sendMessage(Chat.prefix + "Under development.");
                } else p.sendMessage(Chat.playerNotFound(args[0], matches));
            }
            return;
        }
        if (args.length == 0) p.sendMessage(Chat.prefix + "That command isn't set up yet.");
    }
}
