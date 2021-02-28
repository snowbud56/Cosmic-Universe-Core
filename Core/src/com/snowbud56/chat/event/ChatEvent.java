package com.snowbud56.chat.event;

/*
* Created by snowbud56 on January 08, 2018
* Do not change or use this code without permission
*/

import com.snowbud56.chat.ChatManager;
import com.snowbud56.disguise.DisguiseManager;
import com.snowbud56.disguise.PlayerDisguise;
import com.snowbud56.player.CorePlayer;
import com.snowbud56.player.PlayerManager;
import com.snowbud56.player.Rank;
import com.snowbud56.util.Chat;
import com.snowbud56.util.TimeUtil;
import com.snowbud56.util.managers.LogManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatEvent implements Listener {

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        CorePlayer player = PlayerManager.getPlayer(p);
        if (!player.getRank().Has(p, Rank.YOUTUBE, false)) {
            if (ChatManager.silenced) {
                p.sendMessage(Chat.prefix + Chat.mBody + "Chat is currently silenced.");
                e.setCancelled(true);
                return;
            }
            if (ChatManager.lastChat.containsKey(p) && ChatManager.lastChat.get(p) > System.currentTimeMillis() - (ChatManager.delay * 1000)) {
                p.sendMessage(Chat.prefix + Chat.mBody + "You can't talk for another " + Chat.cRed + TimeUtil.convertmstoTime(ChatManager.lastChat.get(p) - (System.currentTimeMillis() - (ChatManager.delay * 1000))));
                e.setCancelled(true);
                return;
            }
        }
        ChatManager.lastChat.put(p, System.currentTimeMillis());
        if (DisguiseManager.disguises.containsKey(p) && DisguiseManager.disguises.get(p) != null && DisguiseManager.getPlayerDisguise(p).isActivated()) {
            PlayerDisguise disguise = DisguiseManager.getPlayerDisguise(p);
            e.setFormat(disguise.getRank().getTag(false, true, true) + Chat.cGray + p.getDisplayName() + Chat.cDGray + " » " + Chat.cWhite + e.getMessage());
        } else e.setFormat(player.getDisplayRank().getTag(false, true, true) + Chat.cGray + p.getDisplayName() + Chat.cDGray + " » " + Chat.cWhite + e.getMessage());
        if (!e.isCancelled()) LogManager.logFile((!p.getDisplayName().equals(p.getName()) ? "[NICKED] " : "") + "[" + player.getRank().name.toUpperCase() + "] " + p.getName() + ": " + e.getMessage());
    }
}
