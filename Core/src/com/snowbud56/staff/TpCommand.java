package com.snowbud56.staff;

/*
* Created by snowbud56 on February 07, 2018
* Do not change or use this code without permission
*/

import com.snowbud56.command.CommandBase;
import com.snowbud56.player.Rank;
import com.snowbud56.util.Chat;
import org.bukkit.entity.Player;

public class TpCommand extends CommandBase {

    public TpCommand() {
        super(Rank.MODERATOR, "tp", "tphere", "tpall");
    }

    @Override
    public void execute(Player p, String[] args) {
        p.sendMessage(Chat.prefix + "This command is under development.");
    }
}
