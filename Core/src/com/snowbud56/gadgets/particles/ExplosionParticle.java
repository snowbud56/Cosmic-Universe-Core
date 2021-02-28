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

import java.util.Set;

public class ExplosionParticle extends ParticleGadget {

    public ExplosionParticle() {
        super("Explosions", new String[] {
                Chat.cGray + "Show how explosive you can be",
                Chat.cGray + "with these explosive particles!",
        }, Material.TNT);
    }

    @EventHandler
    public void updateEvent(UpdateEvent e) {
        if (e.getType() != UpdateType.FASTER) return;
        Set<Player> active = GadgetManager.instance.getGadgetByName(getName()).getActive();
        for (Player p : active) {
            if (!shouldDisplay(p)) continue;
            Location loc = p.getLocation().add(0, 3, 0);
            PacketPlayOutWorldParticles particles = new PacketPlayOutWorldParticles(EnumParticle.EXPLOSION_LARGE, true, (float) loc.getX(), (float) loc.getY(), (float) loc.getZ(), 1F, 0.5F, 1F, 0, 1);
            for (Player target : loc.getWorld().getPlayers()) ((CraftPlayer) target).getHandle().playerConnection.sendPacket(particles);
        }
    }
}
