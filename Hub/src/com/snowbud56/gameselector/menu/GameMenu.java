package com.snowbud56.gameselector.menu;

/*
* Created by snowbud56 on March 21, 2018
* Do not change or use this code without permission
*/

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.snowbud56.Hub;
import com.snowbud56.gameselector.GameType;
import com.snowbud56.util.Chat;
import com.snowbud56.util.ItemFactory;
import com.snowbud56.util.managers.LogManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class GameMenu implements PluginMessageListener {

    private Player p;
    private Inventory inv;
    private HashMap<String, GameType> items = new HashMap<>();

    public GameMenu(Player p) {
        this.p = p;
        for (GameType type : GameType.values())
            items.put(type.getServer().toLowerCase(), type);
    }

    public void openMenu() {
        inv = Bukkit.getServer().createInventory(null, 9, "Game Selector");
        for (GameType type : GameType.values()) {
            List<String> lore = new ArrayList<>();
            lore.addAll(Arrays.asList(type.getLore()));
            lore.add("");
            lore.add(Chat.element("Updating...") + " players online");
            inv.setItem(type.getSlot(), new ItemFactory(type.getMaterial()).displayName(Chat.cRed + type.getName()).lore(lore).buildItem());
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("PlayerCount");
            out.writeUTF(type.getServer());
            p.sendPluginMessage(Hub.getInstance(), "BungeeCord", out.toByteArray());
            p.openInventory(inv);
        }
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] bytes) {
        if (!channel.equals("BungeeCord")) {
            return;
        }
        ByteArrayDataInput in = ByteStreams.newDataInput(bytes);
        String messagetype = in.readUTF();
        if (messagetype.equals("PlayerCount")) {
            String server = in.readUTF();
            int playercount = in.readInt();
            GameType type = items.get(server.toLowerCase());
            if (type == null)
                LogManager.logError("Something went wrong when updating player list! GameType could not be found!");
            else {
                List<String> lore = new ArrayList<>();
                lore.addAll(Arrays.asList(type.getLore()));
                lore.add("");
                lore.add(Chat.element(playercount + "") + " players online");
                player.getOpenInventory().getTopInventory().setItem(type.getSlot(), new ItemFactory(type.getMaterial()).displayName(Chat.cRed + type.getName()).lore(lore).buildItem());
            }
        }
    }
}
