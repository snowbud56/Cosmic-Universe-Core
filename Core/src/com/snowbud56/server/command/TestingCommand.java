package com.snowbud56.server.command;

/*
* Created by snowbud56 on February 11, 2018
* Do not change or use this code without permission
*/

import com.snowbud56.command.CommandBase;
import com.snowbud56.player.CorePlayer;
import com.snowbud56.player.PlayerManager;
import com.snowbud56.player.Rank;
import com.snowbud56.player.events.JoinStatsLoad;
import com.snowbud56.util.Chat;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class TestingCommand extends CommandBase {

    public TestingCommand() {
        super(Rank.ADMIN, "testing", "test");
    }

    @Override
    public void execute(Player p, String[] args) {
        JoinStatsLoad.testingMode = !JoinStatsLoad.testingMode;
        if (JoinStatsLoad.testingMode) {
            for (Player target : Bukkit.getOnlinePlayers()) {
                CorePlayer player = PlayerManager.getPlayer(target);
                if (player.getRank().Has(target, Rank.TESTER, false))
                    target.sendMessage(Chat.prefix + "Testing mode has been enabled by " + p.getDisplayName());
                else
                    target.kickPlayer("Testing mode has been enabled!\nYou are not an official tester for Cosmic Universe.\nIf you wish to apply, You may do so at https://goo.gl/forms/xPcDdzgv6dFrtVyk1");
            }
        } else
            Bukkit.broadcastMessage(Chat.prefix + "Testing mode has been disabled by " + p.getDisplayName());
    }
}
