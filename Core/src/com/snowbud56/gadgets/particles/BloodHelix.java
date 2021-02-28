package com.snowbud56.gadgets.particles;

/*
* Created by snowbud56 on February 10, 2018
* Do not change or use this code without permission
*/

import com.snowbud56.gadgets.GadgetManager;
import com.snowbud56.gadgets.types.ParticleGadget;
import com.snowbud56.updater.UpdateEvent;
import com.snowbud56.updater.UpdateType;
import com.snowbud56.util.Chat;
import com.snowbud56.util.PacketUtil;
import net.minecraft.server.v1_8_R3.EnumParticle;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Set;

public class BloodHelix extends ParticleGadget implements Listener {

    public BloodHelix() {
        super("Blood Helix", new String[] {
                Chat.cGray + "Ancient legend says this magic",
                Chat.cGray + "empowers the blood of its user,",
                Chat.cGray + "giving them godly powers."
        }, Material.REDSTONE_BLOCK);
    }

    @EventHandler
    public void updateEvent(UpdateEvent e) {
        if (e.getType() != UpdateType.FASTEST) return;
        Set<Player> active = GadgetManager.instance.getGadgetByName(getName()).getActive();
        for (Player p : active) {
            if (!shouldDisplay(p)) continue;
            if (GadgetManager.instance.isMoving(p)) {
                PacketUtil.displayParticle(p.getLocation(), EnumParticle.REDSTONE, 0.35F, 0.45F, 0.35F, 8);
//                PacketPlayOutWorldParticles particles = new PacketPlayOutWorldParticles(EnumParticle.REDSTONE, true, (float) p.getLocation().getX(), (float) p.getLocation().getY() + 0.75F, (float) p.getLocation().getZ(), 0.35F, 0.45F, 0.35F, 0, 8);
//                for (Player target : p.getWorld().getPlayers()) ((CraftPlayer) target).getHandle().playerConnection.sendPacket(particles);
            } else {
                for (int height = 0; height <= 20; height++) {
                    for (int i = 0; i < 2; i++) {
                        double lead = i * ((2d * Math.PI) / 2);
                        double heightLead = height * ((2d * Math.PI) / 20);
                        float x = (float) (Math.sin(p.getTicksLived() / 20d + lead + heightLead) * 1.2f);
                        float z = (float) (Math.cos(p.getTicksLived() / 20d + lead + heightLead) * 1.2f);
                        float y = 0.15f * height;
                        Location loc = p.getLocation().add(x * (1d - height / 22d), y, z * (1d - height / 22d));
                        PacketUtil.displayParticle(loc, EnumParticle.REDSTONE, 1);
//                        PacketPlayOutWorldParticles particles = new PacketPlayOutWorldParticles(EnumParticle.REDSTONE, true, (float) loc.getX(), (float) loc.getY(), (float) loc.getZ(), 0F, 0F, 0F, 0, 1);
//                        for (Player target : Bukkit.getOnlinePlayers()) ((CraftPlayer) target).getHandle().playerConnection.sendPacket(particles);
                    }
                }
            }
        }
    }
}
