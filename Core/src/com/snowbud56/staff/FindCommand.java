package com.snowbud56.staff;

/*
 * Created by snowbud56 on April 19, 2019
 * Do not change or use this code without permission
 */

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.snowbud56.Core;
import com.snowbud56.command.CommandBase;
import com.snowbud56.player.Rank;
import com.snowbud56.util.Chat;
import com.snowbud56.util.ServerUtil;
import com.snowbud56.util.managers.LogManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

public class FindCommand extends CommandBase implements PluginMessageListener {

    public FindCommand() {
        super(Rank.MODERATOR, "find");
    }

    @Override
    public void execute(Player p, String[] args) {
        if (args.length == 0) {
            p.sendMessage(Chat.prefix + "Find commands:");
            p.sendMessage(Chat.help(aliasUsed, "<player>", "Tells you the server the player is on.", Rank.MODERATOR));
        } else {
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("Forward");
            out.writeUTF("ALL");
            out.writeUTF("FindPlayer");
            out.writeUTF(p.getName() + "," + args[0]);
            p.sendPluginMessage(Core.getPlugin(), "BungeeCord", out.toByteArray());
            p.sendMessage(Chat.prefix + "Finding " + Chat.element(args[0]) + "...");
            Player target = Bukkit.getPlayer(args[0]);
            if (target != null)
                p.sendMessage(Chat.prefix + Chat.element(target.getName()) + " is on " + Chat.element(ServerUtil.currentServer) + ".");
        }
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (!channel.equals("BungeeCord")) {
            return;
        }
        ByteArrayDataInput in = ByteStreams.newDataInput(message);
        String subchannel = in.readUTF();
        if (subchannel.equals("FindPlayer")) {
            String[] playerTarget = in.readUTF().split(",");
            String targetName = playerTarget[1];
            Player target = Bukkit.getPlayer(targetName);
            if (target != null) {
                ByteArrayDataOutput out = ByteStreams.newDataOutput();
                out.writeUTF("Forward");
                out.writeUTF("ALL");
                out.writeUTF("FoundPlayer");
                out.writeUTF(playerTarget[0] + "," + target.getName() + "," + ServerUtil.currentServer);
                ((Player) Bukkit.getOnlinePlayers().toArray()[0]).sendPluginMessage(Core.getPlugin(), "BungeeCord", out.toByteArray());
            }
        } else if (subchannel.equals("FoundPlayer")) {
            String[] splitMessage = in.readUTF().split(",");
            Player p = Bukkit.getPlayer(splitMessage[0]);
            String targetName = splitMessage[1];
            String serverName = splitMessage[2];
            p.sendMessage(Chat.prefix + Chat.element(targetName) + " is on " + Chat.element(serverName) + ".");
        }
    }
}
