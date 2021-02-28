package com.snowbud56.chat.command;

/*
* Created by snowbud56 on January 08, 2018
* Do not change or use this code without permission
*/

import com.snowbud56.chat.ChatManager;
import com.snowbud56.command.CommandBase;
import com.snowbud56.player.PlayerManager;
import com.snowbud56.player.Rank;
import com.snowbud56.util.Chat;
import com.snowbud56.util.managers.LogManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class SlowChat extends CommandBase {

    public SlowChat() {
        super(Rank.MODERATOR, new Rank[] {Rank.SNOW}, "slowchat");
    }

    @Override
    public void execute(Player p, String[] args) {
        if (args.length == 0) {
                p.sendMessage(Chat.prefix + Chat.mBody + "Chat Manager Commands:");
                p.sendMessage(Chat.help(aliasUsed, "<integer>", "Sets the delay that players can speak (-1 to reset)", Rank.MODERATOR));
        } else {
            Integer seconds;
            try {
                seconds = Integer.parseInt(args[0]);
            } catch (Exception ex) {
                p.sendMessage(Chat.prefix + Chat.mBody + "I had issues making that a number, are you positive that's a whole number?");
                return;
            }
            ChatManager.delay = seconds;
            ChatManager.silenced = false;
            if (seconds == 0) {
                ChatManager.delay = -1;
                ChatManager.silenced = true;
                Bukkit.broadcastMessage(Chat.prefix + Chat.mElem + p.getDisplayName() + Chat.mBody + " has silenced chat.");
                LogManager.logFile("[" + PlayerManager.getPlayer(p).getRank().name.toUpperCase() + "] " + p.getName() + " has silenced chat.");
            } else if (seconds < 0) {
                ChatManager.delay = -1;
                Bukkit.broadcastMessage(Chat.prefix + Chat.mElem + p.getDisplayName() + Chat.mBody + " has unsilenced chat.");
                LogManager.logFile("[" + PlayerManager.getPlayer(p).getRank().name.toUpperCase() + "] " + p.getName() + " has unsilenced chat.");
            }
            else {
                Bukkit.broadcastMessage(Chat.prefix + Chat.mElem + p.getDisplayName() + Chat.mBody + " has set the chat delay to " + Chat.cRed + ChatManager.delay + Chat.mBody + " seconds.");
                LogManager.logFile("[" + PlayerManager.getPlayer(p).getRank().name.toUpperCase() + "] " + p.getName() + " has set the chat delay to " + ChatManager.delay + " seconds.");
            }
        }
    }
}
