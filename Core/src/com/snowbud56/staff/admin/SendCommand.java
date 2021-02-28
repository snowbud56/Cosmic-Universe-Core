package com.snowbud56.staff.admin;

/*
* Created by snowbud56 on March 30, 2018
* Do not change or use this code without permission
*/

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.snowbud56.Core;
import com.snowbud56.command.CommandBase;
import com.snowbud56.player.Rank;
import com.snowbud56.util.Chat;
import com.snowbud56.util.ServerUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class SendCommand extends CommandBase {

    public SendCommand() {
        super(Rank.ADMIN, "send");
    }

    @Override
    public void execute(Player p, String[] args) {
        if (args.length < 2) {
            p.sendMessage(Chat.prefix + "Send Commands");
            p.sendMessage(Chat.help(aliasUsed, "<player | current> <server>", "Sends the player or all players on your server to the server", Rank.ADMIN));
            p.sendMessage(Chat.prefix + "Servers: " + ServerUtil.getServers().toString().replace("[", "").replace("]", ""));
        } else {
            String server = null;
            for (String s : ServerUtil.getServers())
                if (s.toLowerCase().equals(args[1].toLowerCase()))
                    server = s;
            if (server == null) {
                p.sendMessage(Chat.prefix + Chat.element(args[1]) + " is not a valid server!");
                return;
            }
            if (args[0].toLowerCase().equals("current")) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    ByteArrayDataOutput out = ByteStreams.newDataOutput();
                    out.writeUTF("ConnectOther");
                    out.writeUTF(player.getName());
                    out.writeUTF(server);
                    p.sendPluginMessage(Core.getPlugin(), "BungeeCord", out.toByteArray());
                    p.sendMessage(Chat.prefix + "Sending " + Chat.element(player.getDisplayName()) + " to " + Chat.element(server) + ".");
                    player.sendMessage(Chat.prefix + Chat.element(p.getDisplayName()) + " has sent you to " + Chat.element(server));
                }
                return;
            }
            Player target = Bukkit.getPlayer(args[0]);
            if (target != null) {
                ByteArrayDataOutput out = ByteStreams.newDataOutput();
                out.writeUTF("ConnectOther");
                out.writeUTF(target.getName());
                out.writeUTF(server);
                p.sendPluginMessage(Core.getPlugin(), "BungeeCord", out.toByteArray());
                p.sendMessage(Chat.prefix + "Sending " + Chat.element(args[0]) + " to " + Chat.element(server) + ".");
                target.sendMessage(Chat.prefix + Chat.element(p.getDisplayName()) + " has sent you to " + Chat.element(server));
                return;
            }
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("ConnectOther");
            out.writeUTF(args[0]);
            out.writeUTF(server);
            p.sendPluginMessage(Core.getPlugin(), "BungeeCord", out.toByteArray());
            p.sendMessage(Chat.prefix + "Sending " + Chat.element(args[0]) + " to " + Chat.element(server) + " if they're online.");
        }
    }
}
