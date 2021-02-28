package com.snowbud56.staff.admin;

/*
* Created by snowbud56 on February 03, 2018
* Do not change or use this code without permission
*/

import com.snowbud56.command.CommandBase;
import com.snowbud56.disguise.DisguiseManager;
import com.snowbud56.disguise.PlayerDisguise;
import com.snowbud56.player.CorePlayer;
import com.snowbud56.player.PlayerManager;
import com.snowbud56.player.Rank;
import com.snowbud56.util.Chat;
import com.snowbud56.util.PlayersUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.List;

public class TrollCommands extends CommandBase {
    public TrollCommands() {
        super(Rank.ADMIN, "kaboom", "launch", "cansee", "freeze");
    }

    @Override
    public void execute(Player p, String[] args) {
        if (aliasUsed.equals("kaboom")) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.setVelocity(new Vector(0, 5, 0));
                player.getWorld().strikeLightningEffect(player.getLocation());
                p.sendMessage(Chat.prefix + "Launched " + player.getDisplayName());
            }
        } else if (aliasUsed.equals("launch")) {
            if (args.length == 0) p.sendMessage(Chat.prefix + "Usage: /launch <player>");
            else {
                List<String> matches = PlayersUtil.findPlayer(p, args[0], false);
                Double launchSpeed = 5.0D;
                if (matches.size() == 1) {
                    if (args.length >= 2) {
                        try {
                            launchSpeed = Double.parseDouble(args[1]);
                        } catch (Exception e) {
                            p.sendMessage(Chat.prefix + Chat.element(args[1]) + " is not a valid number. Defaulting to 5.");
                        }
                    }
                    Player target = Bukkit.getPlayer(matches.get(0));
                    target.setVelocity(new Vector(0, launchSpeed, 0));
                    p.sendMessage(Chat.prefix + "Launched " + target.getDisplayName() + " with speed " + launchSpeed);
                } else {
                    p.sendMessage(Chat.prefix + Chat.playerNotFound(args[0], matches));
                }
            }
        } else if (aliasUsed.equals("cansee")) {
            if (args.length == 0) p.sendMessage(Chat.prefix + "Usage: /cansee <player>");
            else {
                List<String> matches = PlayersUtil.findPlayer(p, args[0], false);
                if (matches.size() == 1) {
                    PlayerDisguise disguise = DisguiseManager.getPlayerDisguise(Bukkit.getPlayer(matches.get(0)));
                    if (disguise == null) p.sendMessage(Chat.AdminPrefix + "That player doesn't have a disguise enabled.");
                    else disguise.update(Bukkit.getPlayer(matches.get(0)), p);
                } else
                    p.sendMessage(Chat.prefix + Chat.playerNotFound(args[0], matches));

            }
        } else if (aliasUsed.equals("freeze")) {
            if (args.length == 0) p.sendMessage(Chat.prefix + "Usage: /freeze <player>");
            else {
                List<String> matches = PlayersUtil.findPlayer(p, args[0], false);
                if (matches.size() == 1) {
                    Player target = Bukkit.getPlayer(matches.get(0));
                    CorePlayer CoreTarget = PlayerManager.getPlayer(target);
                    CoreTarget.setCanMove(!CoreTarget.canMove());
                    p.sendMessage(Chat.AdminPrefix + "You have " + (CoreTarget.canMove() ? "frozen " : "unfrozen ") + Chat.adminElement(target.getDisplayName()));
                    target.sendMessage(Chat.prefix + "You have been " + (CoreTarget.canMove() ? "frozen" : "unfrozen") + " by " + Chat.element(p.getDisplayName()));
                } else
                    p.sendMessage(Chat.prefix + Chat.playerNotFound(args[0], matches));

            }
        }
    }
}
