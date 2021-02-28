package com.snowbud56.util;

/*
* Created by snowbud56 on March 30, 2018
* Do not change or use this code without permission
*/

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.snowbud56.Core;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ServerUtil implements Listener, PluginMessageListener {

    public static String currentServer;
    private static List<String> servers = new ArrayList<>();

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        if (servers.isEmpty()) {
            new BukkitRunnable() {
                public void run() {
                    ByteArrayDataOutput out = ByteStreams.newDataOutput();
                    out.writeUTF("GetServers");
                    ByteArrayDataOutput out1 = ByteStreams.newDataOutput();
                    out1.writeUTF("GetServer");
                    p.sendPluginMessage(Core.getPlugin(), "BungeeCord", out.toByteArray());
                    p.sendPluginMessage(Core.getPlugin(), "BungeeCord", out1.toByteArray());
                }
            }.runTaskLater(Core.getPlugin(), 10L);
        }
    }

    @Override
    public void onPluginMessageReceived(String channel, Player p, byte[] message) {
        if (!channel.equals("BungeeCord")) {
            return;
        }
        ByteArrayDataInput in = ByteStreams.newDataInput(message);
        String subchannel = in.readUTF();
        if (subchannel.equals("GetServers")) {
            String[] serverList = in.readUTF().split(", ");
            if (!servers.isEmpty()) servers = new ArrayList<>();
            servers.addAll(Arrays.asList(serverList));
        }
        if (subchannel.equals("GetServer")) {
            currentServer = in.readUTF();
        }
    }

    public static List<String> getServers() {
        return servers;
    }

}
