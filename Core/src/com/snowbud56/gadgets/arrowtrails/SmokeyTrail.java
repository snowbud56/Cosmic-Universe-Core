package com.snowbud56.gadgets.arrowtrails;

/*
 * Created by snowbud56 on July 14, 2020
 * Do not change or use this code without permission
 */

import com.snowbud56.Core;
import com.snowbud56.gadgets.GadgetManager;
import com.snowbud56.gadgets.types.ArrowTrailGadget;
import com.snowbud56.util.Chat;
import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class SmokeyTrail extends ArrowTrailGadget {
    public SmokeyTrail() {
        super("Smokey Arrow Trail", new String[] {
                Chat.cGray + "Select to have smoke follow",
                Chat.cGray + "your arrows."}, Material.COAL);
    }

    @EventHandler
    public void onArrowShoot(EntityShootBowEvent e) {
        if (!(e.getEntity() instanceof Player) || !(e.getProjectile() instanceof Arrow)) return;
        Player p = (Player) e.getEntity();
        if (GadgetManager.instance.getActiveGadget(p, getType()) instanceof FlameTrail) {
            new BukkitRunnable() {
                Arrow a = (Arrow) e.getProjectile();
                public void run() {
                    if (a.isDead() || a.isOnGround() || a == null) cancel();
                    Location loc = a.getLocation();
                    PacketPlayOutWorldParticles p = new PacketPlayOutWorldParticles(EnumParticle.SMOKE_LARGE, true, (float) loc.getX(), (float) loc.getY(), (float) loc.getZ(), 0F, 0F, 0F, 0, 1);
                    for (Player t : Bukkit.getOnlinePlayers()) ((CraftPlayer) t).getHandle().playerConnection.sendPacket(p);
                }
            }.runTaskTimer(Core.getPlugin(), 2, 1);
        }
    }
}
