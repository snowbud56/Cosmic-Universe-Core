package com.snowbud56.gadgets.types;

/*
* Created by snowbud56 on February 09, 2018
* Do not change or use this code without permission
*/

import com.snowbud56.gadgets.Gadget;
import com.snowbud56.gadgets.GadgetManager;
import com.snowbud56.util.Chat;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class ParticleGadget extends Gadget {

    public ParticleGadget(String name, String[] lore, Material mat) {
        super(name, lore, mat, GadgetType.PARTICLE);
    }
    protected boolean shouldDisplay(Player player) {
        return player.getGameMode() != GameMode.SPECTATOR;
    }

    @Override
    public void enableCustom(Player player) {
        if (_active.contains(player)) return;
        GadgetManager.instance.setActiveGadget(player, this);
        player.sendMessage(Chat.prefix + "Enabled " + getName());
    }

    @Override
    public void disableCustom(Player player) {
        if (GadgetManager.instance.getGadgetByName(getName()).getActive().remove(player))
            player.sendMessage(Chat.prefix + "Disabled " + getName());
    }
}
