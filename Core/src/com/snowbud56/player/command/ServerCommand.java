package com.snowbud56.player.command;

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
import org.bukkit.entity.Player;

public class ServerCommand extends CommandBase {

    public ServerCommand() {
        super(Rank.ALL, "server");
    }

    @Override
    public void execute(Player p, String[] args) {
        if (args.length == 0) {
            p.sendMessage(Chat.prefix + "Server comands:");
            p.sendMessage(Chat.help(aliasUsed, "<server>", "Sends you to the server.", Rank.ALL));
            p.sendMessage(Chat.prefix + "Servers: " + Chat.element(ServerUtil.getServers().toString().replace("[", "").replace("]", "")));
        } else {
            String server = null;
            for (String s : ServerUtil.getServers())
                if (s.toLowerCase().equals(args[0].toLowerCase()))
                    server = s;
            if (server != null) {
                ByteArrayDataOutput out = ByteStreams.newDataOutput();
                out.writeUTF("Connect");
                out.writeUTF(server);
                p.sendPluginMessage(Core.getPlugin(), "BungeeCord", out.toByteArray());
                p.sendMessage(Chat.prefix + "Sending you to " + Chat.element(server) + ".");
            } else {
                p.sendMessage(Chat.prefix + "Invalid server! Servers: " + Chat.element(ServerUtil.getServers().toString().replace("[", "").replace("]", "")));
            }
        }
    }
}
