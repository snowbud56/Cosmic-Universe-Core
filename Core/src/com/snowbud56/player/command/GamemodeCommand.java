package com.snowbud56.player.command;

/*
* Created by snowbud56 on February 19, 2018
* Do not change or use this code without permission
*/

import com.snowbud56.command.CommandBase;
import com.snowbud56.player.Rank;
import com.snowbud56.util.Chat;
import com.snowbud56.util.PlayersUtil;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.List;

public class GamemodeCommand extends CommandBase {
    public GamemodeCommand() {
        super(Rank.ADMIN, "gm", "gamemode");
    }

    @Override
    public void execute(Player p, String[] args) {
        if (args.length == 0) {
            p.sendMessage(Chat.prefix + "Gamemode commands:");
            p.sendMessage(Chat.prefix + "/" + aliasUsed + " <gamemode>: Change your gamemode. " + Rank.ADMIN.getTag(false, false, false));
            p.sendMessage(Chat.prefix + "/" + aliasUsed + " <gamemode> <player>: Change someone else's gamemode. " + Rank.ADMIN.getTag(false, false, false));
        } else if (args.length == 1) {
            handle(p, args[0], null);
        } else {
            handle(p, args[0], args[1]);
        }
    }

    private void handle(Player p, String gamemode, String tname) {
        GameMode gm;
        switch (gamemode.toLowerCase()) {
            case "s":
            case "survival":
            case "0":
                gm = GameMode.SURVIVAL;
                break;
            case "c":
            case "creative":
            case "1":
                gm = GameMode.CREATIVE;
                break;
            case "a":
            case "adventure":
            case "2":
                gm = GameMode.ADVENTURE;
                break;
            case "spec":
            case "spectator":
            case "3":
                gm = GameMode.SPECTATOR;
                break;
            default:
                p.sendMessage(Chat.prefix + Chat.element(gamemode) + " isn't a valid gamemode!");
                return;
        }
        if (tname != null) {
            List<String> matches = PlayersUtil.findPlayer(p, tname, false);
            if (matches.size() == 1) {
                Player target = Bukkit.getPlayer(matches.get(0));
                target.setGameMode(gm);
                target.sendMessage(Chat.prefix + Chat.element(p.getDisplayName()) + " has set your gamemode to " + Chat.element(gm.name()) + ".");
                p.sendMessage(Chat.prefix + Chat.element(target.getDisplayName()) + "'s gamemode is now " + Chat.element(gm.name()) + ".");
            } else
                p.sendMessage(Chat.playerNotFound(tname, matches));
        } else {
            p.setGameMode(gm);
            p.sendMessage(Chat.prefix + "Your gamemode is now " + Chat.element(gm.name()) + ".");
        }
    }
}
