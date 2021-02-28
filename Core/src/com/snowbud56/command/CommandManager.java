package com.snowbud56.command;

/*
* Created by snowbud56 on January 10, 2018
* Do not change or use this code without permission
*/

import com.snowbud56.player.PlayerManager;
import com.snowbud56.util.Chat;
import com.snowbud56.util.managers.LogManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.HashMap;

public class CommandManager implements Listener {
    public static CommandManager instance;

    private static HashMap<String, Command> commands;

    public CommandManager() {
        instance = this;
        commands = new HashMap<>();
    }

    @EventHandler
    public void preProcessCommand(PlayerCommandPreprocessEvent e) {
        LogManager.logFile(e.getPlayer().getName() + " executed server command: " + e.getMessage());
        String commandName = e.getMessage().substring(1);
        String[] args = null;
        if (commandName.contains(" ")) {
            commandName = commandName.split(" ")[0];
            args = e.getMessage().substring(e.getMessage().indexOf(' ') + 1).split(" ");
        }
        if (commandName.toLowerCase().startsWith("bukkit:") || commandName.toLowerCase().startsWith("minecraft:") ||
                commandName.toLowerCase().equals("plugin") || commandName.toLowerCase().equals("plugins") || commandName.toLowerCase().equals("pl") || commandName.toLowerCase().equals("ver") || commandName.toLowerCase().equals("version")) {
            e.setCancelled(true);
            e.getPlayer().sendMessage(Chat.prefix + "That command has been disabled.");
            return;
        }
        Command command = commands.get(commandName.toLowerCase());
        if (command != null) {
            e.setCancelled(true);
            if (args == null) args = new String[] {};
            if (PlayerManager.getPlayer(e.getPlayer()).getRank().Has(e.getPlayer(), command.getRequiredRank(), command.getSpecificRanks(), true)) {
                command.setAliasUsed(commandName.toLowerCase());
                command.execute(e.getPlayer(), args);
            }
        }
    }

    public void addCommand(Command cmd) {
        for (String alias : cmd.getAliases()) {
            commands.put(alias.toLowerCase(), cmd);
        }
    }
}
