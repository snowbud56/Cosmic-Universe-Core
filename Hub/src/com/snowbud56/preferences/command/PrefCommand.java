package com.snowbud56.preferences.command;

/*
* Created by snowbud56 on January 09, 2018
* Do not change or use this code without permission
*/

import com.snowbud56.command.CommandBase;
import com.snowbud56.player.Rank;
import com.snowbud56.preferences.menu.PreferenceMenu;
import org.bukkit.entity.Player;

public class PrefCommand extends CommandBase {

    public PrefCommand() {
        super(Rank.ALL, "prefs", "preferences");
    }

    @Override
    public void execute(Player caller, String[] args) {
        PreferenceMenu.openMainGUI(caller);
    }
}
