package com.snowbud56.staff.admin;

/*
* Created by snowbud56 on February 11, 2018
* Do not change or use this code without permission
*/

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.snowbud56.Core;
import com.snowbud56.command.CommandBase;
import com.snowbud56.player.Rank;
import com.snowbud56.util.Chat;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class ReloadCommand extends CommandBase {
    public ReloadCommand() {
        super(Rank.OWNER, "reload", "rl", "stop", "restart");
    }

    @Override
    public void execute(Player p, String[] args) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Connect");
        out.writeUTF("Hub");
        Bukkit.broadcastMessage(Chat.prefix + "The server has been stopped, you have been connected to " + Chat.element("Hub") + ".");
        for (Player target : Bukkit.getOnlinePlayers()) {
            target.sendPluginMessage(Core.getPlugin(), "BungeeCord", out.toByteArray());
        }
        new BukkitRunnable() {
            @Override
            public void run() {
                Bukkit.shutdown();
            }
        }.runTaskLater(Core.getPlugin(), 20L);
    }
}
