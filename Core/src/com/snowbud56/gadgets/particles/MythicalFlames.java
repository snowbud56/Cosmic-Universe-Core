package com.snowbud56.gadgets.particles;

/*
* Created by snowbud56 on January 28, 2018
* Do not change or use this code without permission
*/

import com.snowbud56.gadgets.GadgetManager;
import com.snowbud56.gadgets.types.ParticleGadget;
import com.snowbud56.player.PlayerManager;
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

public class MythicalFlames extends ParticleGadget implements Listener {

    public MythicalFlames() {
        super("Mythical Flames", new String[] {
                Chat.cGray + "These flames are said to be",
                Chat.cGray + "the spirit of a mythical creature..."}, Material.BLAZE_POWDER);
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
                Rank rank = PlayerManager.getPlayer(p).getRank();
                int iAdd = 20;
                int iLessThan = 40;
                if (rank.Has(p, Rank.HELPER, false)) {
                    iAdd = 15;
                    iLessThan = 45;
                }
                for (int i = 0; i <= iLessThan; i += iAdd) {
                    double x = loc.getX() + ((Math.sin((t - i) / 10d)) * 3.4);
                    double z = loc.getZ() + ((Math.cos((t - i) / 10d)) * 3.4);
                    PacketPlayOutWorldParticles particles = new PacketPlayOutWorldParticles(EnumParticle.FLAME, true, (float) x, (float) loc.getY(), (float) z, (float) (Math.sin(((t - i) / 10d) - 3.15) / 5), 0F, (float) (Math.cos(((t - i) / 10d) - 3.15) / 5), 1, 0);
                    for (Player target : loc.getWorld().getPlayers()) ((CraftPlayer) target).getHandle().playerConnection.sendPacket(particles);
                    x = loc.getX() + ((Math.cos((t - i) / 10d)) * 3.4);
                    z = loc.getZ() + ((Math.sin((t - i) / 10d)) * 3.4);
                    particles = new PacketPlayOutWorldParticles(EnumParticle.FLAME, true, (float) x, (float) loc.getY(), (float) z, (float) (Math.cos(((t - i) / 10d) - 3.15) / 5), 0F, (float) (Math.sin(((t - i) / 10d) - 3.15) / 5), 1, 0);
                    for (Player target : loc.getWorld().getPlayers()) ((CraftPlayer) target).getHandle().playerConnection.sendPacket(particles);
                }
            }
        }
    }
}
