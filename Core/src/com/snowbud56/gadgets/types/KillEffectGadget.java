package com.snowbud56.gadgets.types;

/*
* Created by snowbud56 on February 10, 2018
* Do not change or use this code without permission
*/

import com.snowbud56.util.Chat;
import com.snowbud56.gadgets.Gadget;
import com.snowbud56.gadgets.GadgetManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public abstract class KillEffectGadget extends Gadget implements Listener {

    public KillEffectGadget(String name, String[] lore, Material material) {
        super(name, lore, material, com.snowbud56.gadgets.types.GadgetType.KILL_EFFECT);
    }

    @Override
    public void enableCustom(Player player) {
        if (getActive().contains(player)) return;
        getActive().add(player);
        GadgetManager.instance.setActiveGadget(player, this);
        player.sendMessage(Chat.prefix + "Enabled " + getName());
    }

    @Override
    public void disableCustom(Player player) {
        if (GadgetManager.instance.getGadgetByName(getName()).getActive().remove(player))
            player.sendMessage(Chat.prefix + "Disabled " + getName());
    }

    public abstract void activate(Player p);
}
