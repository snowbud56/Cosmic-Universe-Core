package com.snowbud56.player.command;

/*
* Created by snowbud56 on February 07, 2018
* Do not change or use this code without permission
*/

import com.snowbud56.command.CommandBase;
import com.snowbud56.player.Rank;
import org.bukkit.entity.Player;

public class EffectCommand extends CommandBase {
    public EffectCommand() {
        super(Rank.ADMIN, "effect");
    }

    @Override
    public void execute(Player p, String[] args) {
        //TODO
    }
}
