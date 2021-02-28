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

public class GreenRing extends ParticleGadget implements Listener {
    public GreenRing() {
        super("Green Swirl", new String[] {
                Chat.cGray + "All you need is this swirl",
                Chat.cGray + "and you're all good to shine!"
        }, Material.EMERALD);
    }

    @EventHandler
    public void playParticle(UpdateEvent event) {
        if (event.getType() != UpdateType.TICK) return;
        Set<Player> active = GadgetManager.instance.getGadgetByName(getName()).getActive();
        for (Player p : active) {
            if (!shouldDisplay(p)) continue;
            float x = (float) (Math.sin(p.getTicksLived() / 7d) * 1f);
            float z = (float) (Math.cos(p.getTicksLived() / 7d) * 1f);
            float y = (float) (Math.cos(p.getTicksLived() / 17d) * 1f + 1f);
            Location loc = p.getLocation();
            PacketPlayOutWorldParticles particles = new PacketPlayOutWorldParticles(EnumParticle.VILLAGER_HAPPY, true, (float) loc.getX() + x, (float) loc.getY() + y, (float) loc.getZ() + z, 0F, 0F, 0F, 0, 0);
            for (Player target : p.getWorld().getPlayers()) ((CraftPlayer) target).getHandle().playerConnection.sendPacket(particles);
        }
    }
}
