package com.snowbud56.gadgets.killeffects;

/*
* Created by snowbud56 on February 10, 2018
* Do not change or use this code without permission
*/

import com.snowbud56.gadgets.types.KillEffectGadget;
import com.snowbud56.util.Chat;
import com.snowbud56.gadgets.GadgetManager;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class BloodExplosion extends KillEffectGadget implements Listener {

    public BloodExplosion() {
        super("Blood Explosion Kill Effect", new String[] {Chat.cGray + "Witness the explosion of your enemies!"}, Material.REDSTONE_BLOCK);
    }

    @EventHandler
    public void onKill(PlayerDeathEvent e) {
        Player killer = e.getEntity().getKiller();
        if (killer == null)
            return;
        if (GadgetManager.instance.getActiveGadget(killer, getType()) instanceof BloodExplosion)
            activate(e.getEntity());
    }

    @Override
    public void activate(Player p) {
        p.getWorld().playEffect(p.getLocation().add(0, 1, 0), Effect.STEP_SOUND, 152);
    }
}