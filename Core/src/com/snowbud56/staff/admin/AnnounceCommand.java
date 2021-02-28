package com.snowbud56.staff.admin;

/*
* Created by snowbud56 on March 30, 2018
* Do not change or use this code without permission
*/

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.snowbud56.Core;
import com.snowbud56.command.CommandBase;
import com.snowbud56.player.Rank;
import com.snowbud56.util.Chat;
import com.snowbud56.util.PacketUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

public class AnnounceCommand extends CommandBase implements PluginMessageListener {

    public AnnounceCommand() {
        super(Rank.ADMIN, "announce", "alert");
    }

    @Override
    public void execute(Player p, String[] args) {
        if (args.length == 0) {
            p.sendMessage(Chat.prefix + "Announce Commands:");
            p.sendMessage(Chat.help(aliasUsed, "<message>", "Announces a message to the entire server.", Rank.ADMIN));
        } else {
            StringBuilder message = new StringBuilder();
            for (String msg : args) message.append(msg).append(" ");
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("Forward");
            out.writeUTF("ALL");
            out.writeUTF("BroadcastMessage");
            out.writeUTF(System.currentTimeMillis() + ">" + message.toString());
            p.sendPluginMessage(Core.getPlugin(), "BungeeCord", out.toByteArray());
            Bukkit.broadcastMessage(Chat.prefix + Chat.cAqua + message);
            PacketUtil.sendTitle(Bukkit.getOnlinePlayers(), 20, 200, 20, Chat.cBlue + Chat.Bold + "Announcement", Chat.cAqua + message);
        }
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (!channel.equals("BungeeCord")) {
            return;
        }
        ByteArrayDataInput in = ByteStreams.newDataInput(message);
        String subchannel = in.readUTF();
        if (subchannel.equals("BroadcastMessage")) {
            String[] timeMsg = in.readUTF().split(">");
            if (Long.parseLong(timeMsg[0]) > System.currentTimeMillis() - 1000) {
                String msg = timeMsg[1];
                Bukkit.broadcastMessage(Chat.prefix + Chat.cAqua + msg);
                PacketUtil.sendTitle(Bukkit.getOnlinePlayers(), 20, 200, 20, Chat.cBlue + Chat.Bold + "Announcement", Chat.cAqua + msg);
            }
        }
    }
}