package com.snowbud56.gadgets.particles;

/*
* Created by snowbud56 on February 11, 2018
* Do not change or use this code without permission
*/

import com.snowbud56.gadgets.GadgetManager;
import com.snowbud56.gadgets.types.ParticleGadget;
import com.snowbud56.updater.UpdateEvent;
import com.snowbud56.updater.UpdateType;
import com.snowbud56.util.Chat;
import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Set;

public class SmokeFumes extends ParticleGadget implements Listener {

    public SmokeFumes() {
        super("Smoke Fumes", new String[] {
                Chat.cGray + "Harness the power that the",
                Chat.cGray + "power of smoke contains!"
        }, Material.COAL);
    }

    @EventHandler
    public void updateEvent(UpdateEvent e) {
        if (e.getType() != UpdateType.TICK) return;
        Set<Player> active = GadgetManager.instance.getGadgetByName(getName()).getActive();
        for (Player p : active) {
            if (!shouldDisplay(p)) continue;
            Location loc = p.getLocation();
            if (GadgetManager.instance.isMoving(p)) {
                PacketPlayOutWorldParticles particles = new PacketPlayOutWorldParticles(EnumParticle.SMOKE_NORMAL, true, (float) loc.getX(), (float) loc.getY(), (float) loc.getZ(), 0.25F, 0.05F, 0.25F, 0, 3);
                for (Player target : loc.getWorld().getPlayers()) ((CraftPlayer) target).getHandle().playerConnection.sendPacket(particles);
            } else {
                PacketPlayOutWorldParticles particles = new PacketPlayOutWorldParticles(EnumParticle.SMOKE_LARGE, true, (float) loc.getX(), (float) loc.getY(), (float) loc.getZ(), 1F, 0.05F, 1F, 0, 4);
                for (Player target : loc.getWorld().getPlayers()) ((CraftPlayer) target).getHandle().playerConnection.sendPacket(particles);
            }
        }
    }
}
