package com.snowbud56.staff;

/*
* Created by snowbud56 on February 07, 2018
* Do not change or use this code without permission
*/

import com.snowbud56.command.CommandBase;
import com.snowbud56.player.PlayerManager;
import com.snowbud56.player.Rank;
import com.snowbud56.util.Chat;
import com.snowbud56.util.managers.LogManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class StaffChat extends CommandBase {

    public StaffChat() {
        super(Rank.HELPER, "s", "sc");
    }

    @Override
    public void execute(Player p, String[] args) {
        if (args.length == 0) p.sendMessage(Chat.prefix + "Usage: /" + aliasUsed + " <message>");
        else {
            StringBuilder message = new StringBuilder();
            for (String msg : args) message.append(msg).append(" ");
            Rank rank = PlayerManager.getPlayer(p).getRank();
            for (Player target : Bukkit.getOnlinePlayers()) {
                if (PlayerManager.getPlayer(target).getRank().Has(target, Rank.HELPER, false)) {
                    target.sendMessage(Chat.cRed + "[STAFF CHAT] " + rank.getTag(false, true, true) + Chat.cGray + p.getName() + Chat.cDGray + " » " + Chat.cWhite + message.toString());
                }
            }
            LogManager.logConsole("[STAFF CHAT] [" + rank.name.toUpperCase() + "] " + p.getName() + ": " + message);
        }
    }
}
