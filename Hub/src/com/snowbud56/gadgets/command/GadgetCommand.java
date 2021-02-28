package com.snowbud56.gadgets.command;

/*
* Created by snowbud56 on February 10, 2018
* Do not change or use this code without permission
*/

import com.snowbud56.command.CommandBase;
import com.snowbud56.gadgets.GadgetManager;
import com.snowbud56.player.Rank;
import com.snowbud56.util.Chat;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class GadgetCommand extends CommandBase {

    public GadgetCommand() {
        super(Rank.ADMIN, new Rank[] {Rank.SNOW}, "gadget", "g", "google");
    }

    @Override
    public void execute(Player player, String[] strings) {
        GadgetManager.gadgetsEnabled = !GadgetManager.gadgetsEnabled;
        if (!GadgetManager.gadgetsEnabled) GadgetManager.instance.disableAllGadgets();
        Bukkit.broadcastMessage(Chat.prefix + "Gadgets have been " + (GadgetManager.gadgetsEnabled ? "enabled!" : "disabled!"));
    }
}
