package com.snowbud56.gadgets.killeffects;

/*
* Created by snowbud56 on February 18, 2018
* Do not change or use this code without permission
*/

import com.snowbud56.gadgets.GadgetManager;
import com.snowbud56.gadgets.types.KillEffectGadget;
import com.snowbud56.util.Chat;
import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class SmokeyKill extends KillEffectGadget implements Listener {
    public SmokeyKill() {
        super("Smokey Kill Effect", new String[] {Chat.cGray + "See your opponents disappear",
        Chat.cGray + "into thin air!"}, Material.COAL);
    }

    @EventHandler
    public void onKill(PlayerDeathEvent e) {
        Player killer = e.getEntity().getKiller();
        if (killer == null) return;
        if (GadgetManager.instance.getActiveGadget(killer, getType()) instanceof LightningKillEffect) {
            activate(killer);
        }
    }

    @Override
    public void activate(Player p) {
        Location loc = p.getLocation();
        PacketPlayOutWorldParticles particles = new PacketPlayOutWorldParticles(EnumParticle.SMOKE_LARGE, true, (float) loc.getX(), (float) loc.getY() + 1, (float) loc.getZ(), 0.3F, 0.8F, 0.3F, .5F, 1);
        for (Player player : Bukkit.getOnlinePlayers()) ((CraftPlayer) player).getHandle().playerConnection.sendPacket(particles);
    }
}
