package com.snowbud56.disguise.commands;

/*
* Created by snowbud56 on March 19, 2018
* Do not change or use this code without permission
*/

import com.snowbud56.command.CommandBase;
import com.snowbud56.disguise.DisguiseManager;
import com.snowbud56.disguise.PlayerDisguise;
import com.snowbud56.player.Rank;
import com.snowbud56.util.Chat;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ToggleNick extends CommandBase {

    public ToggleNick() {
        super(Rank.ADMIN, "nicktoggle", "togglenick", "togglenicks");
    }

    @Override
    public void execute(Player p, String[] args) {
        DisguiseManager.setNicksDisabled(!DisguiseManager.getNicksDisabled());
        if (DisguiseManager.getNicksDisabled()) {
            p.sendMessage(Chat.prefix + "Nicknames successfully disabled.");
            for (Player target : Bukkit.getOnlinePlayers()) {
                PlayerDisguise disguise = DisguiseManager.getPlayerDisguise(target);
                if (disguise == null) continue;
                if (disguise.isActivated()) {
                    DisguiseManager.disguises.get(p).deactivate();
                    DisguiseManager.disguises.put(p, null);
                    target.sendMessage(Chat.prefix + Chat.element(p.getDisplayName()) + " has disabled nicknames, so your nickname was reset.");
                }
            }
            Chat.messageRank(Chat.prefix + "Nicknames have been disabled", Rank.MODERATOR, new Rank[] {Rank.SNOW, Rank.YOUTUBE});
        } else
            Chat.messageRank(Chat.prefix + "Nicknames have been re-enabled", Rank.MODERATOR, new Rank[] {Rank.SNOW, Rank.YOUTUBE});
    }
}
