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

public class Silence extends CommandBase {

    public Silence() {
        super(Rank.MODERATOR, new Rank[] {Rank.SNOW}, "silence");
    }

    @Override
    public void execute(Player p, String[] args) {
        ChatManager.silenced = !ChatManager.silenced;
        if (!ChatManager.silenced) ChatManager.delay = -1;
        p.sendMessage(Chat.prefix + Chat.mBody + "You have " + (ChatManager.silenced ? "silenced" : "unsilenced") + " chat");
        Bukkit.broadcastMessage(Chat.prefix + Chat.mElem + p.getDisplayName() + Chat.mBody + " has " + (ChatManager.silenced ? "silenced" : "unsilenced") + " chat.");
        LogManager.logFile("[" + PlayerManager.getPlayer(p).getRank().name.toUpperCase() + "] " + p.getName() + " has " + (ChatManager.silenced ? "silenced" : "unsilenced") + " chat.");
    }
}
