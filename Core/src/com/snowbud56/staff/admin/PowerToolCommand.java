package com.snowbud56.staff.admin;

/*
 * Created by snowbud56 on May 26, 2019
 * Do not change or use this code without permission
 */

import com.snowbud56.command.CommandBase;
import com.snowbud56.player.Rank;
import com.snowbud56.util.Chat;
import com.snowbud56.util.ItemFactory;
import com.snowbud56.util.managers.LogManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.Map;

public class PowerToolCommand extends CommandBase implements Listener {

    private static Map<Player, String> powercommands = new HashMap<>();

    public PowerToolCommand() {
        super(Rank.OWNER, "pt", "powertool");
    }

    @Override
    public void execute(Player p, String[] args) {
        if (args.length == 0) {
            p.sendMessage(Chat.prefix + "Power Tool commands:");
            p.sendMessage(Chat.help(aliasUsed, "<command>", "Sets the command to run whenever you left-click a stick.", Rank.OWNER));
        } else {
            StringBuilder command = new StringBuilder();
            for (String arg : args)
                command.append(arg).append(" ");
            powercommands.put(p, command.toString());
            p.sendMessage(Chat.prefix + "Your power tool command has been set to " + Chat.element("/" + command.toString()));
            p.getInventory().addItem(new ItemFactory(Material.STICK).displayName("§3PowerTool").buildItem());
        }
    }

    @EventHandler
    public void onPowerToolUse(PlayerInteractEvent e) {
        if (e.getPlayer().getInventory().getItemInHand() == null || e.getPlayer().getInventory().getItemInHand().getType() == Material.AIR) return;
        if ((e.getAction().equals(Action.LEFT_CLICK_AIR) || e.getAction().equals(Action.LEFT_CLICK_BLOCK)) && (e.getItem().getType().equals(Material.STICK))) {
            if (!powercommands.containsKey(e.getPlayer())) return;
            if (e.getItem().getItemMeta().getDisplayName().equals("§3PowerTool")) {
                PlayerCommandPreprocessEvent event = new PlayerCommandPreprocessEvent(e.getPlayer(), "/" + powercommands.get(e.getPlayer()));
                Bukkit.getPluginManager().callEvent(event);
                LogManager.logConsole(e.getPlayer().getName() + " executed Power Tool Command: /" + powercommands.get(e.getPlayer()));
            }
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        powercommands.remove(e.getPlayer());
    }
}
