package com.snowbud56.gadgets;

/*
* Created by snowbud56 on January 28, 2018
* Do not change or use this code without permission
*/

import com.snowbud56.gadgets.event.GadgetActivateEvent;
import com.snowbud56.gadgets.types.GadgetType;
import com.snowbud56.util.Chat;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashSet;

public abstract class Gadget implements Listener {

    private GadgetType type;
    private String name;
    private String[] lore;
    private Material material;
    protected HashSet<Player> _active = new HashSet<>();

    public Gadget(String name, String[] lore, Material material, GadgetType type) {
        this.name = name;
        this.lore = lore;
        this.type = type;
        this.material = material;
    }

    public GadgetType getGadgetType() {
        return type;
    }

    public HashSet<Player> getActive() {
        return _active;
    }

    public boolean isActive(Player player) {
        return _active.contains(player);
    }

    @EventHandler
    public void PlayerQuit(PlayerQuitEvent event) {
        disable(event.getPlayer());
        if (_active.contains(event.getPlayer()))
            _active.remove(event.getPlayer());
    }

    public void enable(Player player) {
        if (GadgetManager.instance.getActiveGadget(player, this.getType()) != null)
            GadgetManager.instance.getActiveGadget(player, this.getType()).disable(player);
        GadgetActivateEvent gadgetEvent = new GadgetActivateEvent(player, this);
        Bukkit.getServer().getPluginManager().callEvent(gadgetEvent);
        if (gadgetEvent.isCancelled()) {
            player.sendMessage(Chat.prefix + getName() + " is not enabled.");
            return;
        }
        GadgetManager.instance.setActiveGadget(player, this);
        enableCustom(player);
        _active.add(player);
    }

    void disableForAll() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (isActive(player))
                GadgetManager.removeActive(player, this);
            disable(player);
        }
    }

    public void disable(Player player) {
        if (isActive(player)) GadgetManager.removeActive(player, this);
        disableCustom(player);
    }

    public abstract void enableCustom(Player player);
    public abstract void disableCustom(Player player);

    public String getName() {
        return name;
    }
    public GadgetType getType() {
        return type;
    }

    public String[] getLore() {
        return lore;
    }

    public Material getMaterial() {
        return material;
    }
}
