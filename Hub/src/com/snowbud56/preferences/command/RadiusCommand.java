package com.snowbud56.preferences.command;

/*
 * Created by snowbud56 on May 26, 2019
 * Do not change or use this code without permission
 */

import com.snowbud56.command.CommandBase;
import com.snowbud56.player.Rank;
import com.snowbud56.preferences.prefs.Forcefield;
import com.snowbud56.util.Chat;
import org.bukkit.entity.Player;

public class RadiusCommand extends CommandBase {
    public RadiusCommand() {
        super(Rank.OWNER, "radius");
    }

    @Override
    public void execute(Player p, String[] args) {
        if (args.length == 0) {
            p.sendMessage(Chat.prefix + "Radius commands:");
            p.sendMessage(Chat.help(aliasUsed, "<radius>", "Sets the forcefield radius", Rank.OWNER));
        } else {
            try {
                Integer radius = Integer.valueOf(args[0]);
                Forcefield.radius = radius;
                p.sendMessage(Chat.prefix + "The forcefield radius has been set to " + Chat.element(radius + "") + ".");
            } catch (Exception e) {
                p.sendMessage(Chat.prefix + "That's not a valid number!");
            }
        }
    }
}
