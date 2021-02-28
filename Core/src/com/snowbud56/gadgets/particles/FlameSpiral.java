package com.snowbud56.gadgets.particles;

/*
* Created by snowbud56 on February 11, 2018
* Do not change or use this code without permission
*/

import com.snowbud56.gadgets.GadgetManager;
import com.snowbud56.gadgets.types.ParticleGadget;
import com.snowbud56.player.Rank;
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

public class FlameSpiral extends ParticleGadget implements Listener {
    public FlameSpiral() {
        super("Flame Spiral", new String[] {
                Chat.cGray + "Grant yourself your own",
                Chat.cGray + "privacy shield with these flames!",
                "",
                Chat.cGray + "Unlocked with " + Rank.TESTER.getTag(false, false, true) + Chat.cGray + "rank."
        }, Material.FIREBALL);
    }

    @EventHandler
    public void updateEvent(UpdateEvent e) {
        if (e.getType() != UpdateType.TICK) return;
        Set<Player> active = GadgetManager.instance.getGadgetByName(getName()).getActive();
        for (Player p : active) {
            if (!shouldDisplay(p)) continue;
            double t = (double) p.getTicksLived();
            Location ploc = p.getLocation();
            Location loc = new Location(ploc.getWorld(), ploc.getX(), ploc.getY() + 0.1, ploc.getZ());
            if (GadgetManager.instance.isMoving(p)) {
                PacketPlayOutWorldParticles particles = new PacketPlayOutWorldParticles(EnumParticle.FLAME, true, (float) loc.getX(), (float) loc.getY(), (float) loc.getZ(), 0.25F, 0.05F, 0.25F, 0, 3);
                for (Player target : loc.getWorld().getPlayers()) ((CraftPlayer) target).getHandle().playerConnection.sendPacket(particles);
            } else {
                for (int i = 0; i <= 45; i += 15) {
                    double x = loc.getX() + ((Math.sin((t - i) / 10d)) * 2);
                    double z = loc.getZ() + ((Math.cos((t - i) / 10d)) * 2);
                    PacketPlayOutWorldParticles particles = new PacketPlayOutWorldParticles(EnumParticle.FLAME, true, (float) x, (float) loc.getY(), (float) z, 0F, 0.2F, 0F, 1, 0);
                    for (Player target : loc.getWorld().getPlayers()) ((CraftPlayer) target).getHandle().playerConnection.sendPacket(particles);
                }
            }
        }
    }
}
