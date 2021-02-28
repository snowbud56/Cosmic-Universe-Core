package com.snowbud56.gadgets.types;

/*
* Created by snowbud56 on February 18, 2018
* Do not change or use this code without permission
*/

import com.snowbud56.gadgets.Gadget;
import com.snowbud56.gadgets.GadgetManager;
import com.snowbud56.util.Chat;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public class ArrowTrailGadget extends Gadget implements Listener {
    public ArrowTrailGadget(String name, String[] lore, Material material) {
        super(name, lore, material, GadgetType.ARROW_TRAIL);
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
}
