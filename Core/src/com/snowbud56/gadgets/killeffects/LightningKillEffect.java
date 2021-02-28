package com.snowbud56.gadgets.killeffects;

/*
* Created by snowbud56 on February 11, 2018
* Do not change or use this code without permission
*/

import com.snowbud56.gadgets.types.KillEffectGadget;
import com.snowbud56.util.Chat;
import com.snowbud56.gadgets.GadgetManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class LightningKillEffect extends KillEffectGadget implements Listener {

    public LightningKillEffect() {
        super("Lightning Kill Effect", new String[] {
                Chat.cGray + "Harness the power of the",
                Chat.cGray + "gods with this kill effect!"
        }, Material.GLASS);
    }

    @EventHandler
    public void onKill(PlayerDeathEvent e) {
        Player killer = e.getEntity().getKiller();
        if (killer == null) return;
        if (GadgetManager.instance.getActiveGadget(killer, getType()) instanceof LightningKillEffect) activate(killer);
    }

    @Override
    public void activate(Player p) {
        p.getWorld().strikeLightningEffect(p.getLocation());
    }
}
