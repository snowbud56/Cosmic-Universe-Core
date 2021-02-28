package com.snowbud56.crates.commands;

/*
* Created by snowbud56 on March 28, 2018
* Do not change or use this code without permission
*/

import com.snowbud56.Hub;
import com.snowbud56.command.CommandBase;
import com.snowbud56.crates.CrateObject;
import com.snowbud56.player.Rank;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class CrateCommand extends CommandBase {

    public CrateCommand() {
        super(Rank.ADMIN, "crates", "crate");
    }

    @Override
    public void execute(Player player, String[] strings) {
        CrateObject crate = new CrateObject(player.getLocation());
        crate.spawn();
        new BukkitRunnable() {
            public void run() {
                crate.despawn();
            }
        }.runTaskLater(Hub.getInstance(), 200);
    }
}
