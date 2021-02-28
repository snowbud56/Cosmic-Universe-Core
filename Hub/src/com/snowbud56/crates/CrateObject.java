package com.snowbud56.crates;

/*
* Created by snowbud56 on March 28, 2018
* Do not change or use this code without permission
*/


import com.snowbud56.Hub;
import com.snowbud56.util.Chat;
import com.snowbud56.util.ItemFactory;
import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;

public class CrateObject {

    private Location loc;
    private ArmorStand as;

    public CrateObject(Location loc) {
        this.loc = loc;
    }

    public void spawn() {
        final Location l = loc.clone().add(0, -1, 0);
        as = (ArmorStand) loc.getWorld().spawnEntity(l, EntityType.ARMOR_STAND);
        as.setVisible(false);
        as.setGravity(false);
        as.setCustomNameVisible(true);
        as.setCustomName(Chat.cDPurple + Chat.Bold + "Crate Opener");
        as.setHelmet(new ItemFactory(Material.SKULL_ITEM).playerName("Chesthead").buildItem());
        new BukkitRunnable() {
            private Double d = 0.0D;
            public void run() {
                if (as == null || as.isDead()) cancel();
                else {
                    as.setHeadPose(new EulerAngle(0, d, 0));
                    d = d + 0.12;
                    PacketPlayOutWorldParticles p = new PacketPlayOutWorldParticles(EnumParticle.SPELL_WITCH, false, (float) loc.getX(), (float) loc.getY() + 0.5F, (float) loc.getZ(), 0.15F, 0.15F, 0.15F, 0.3F, 2);
                    for (Player player : Bukkit.getOnlinePlayers()) ((CraftPlayer) player).getHandle().playerConnection.sendPacket(p);
                }
            }
        }.runTaskTimer(Hub.getInstance(), 0, 1);
    }

    public void despawn() {
        as.teleport(new Location(loc.getWorld(), 0, 0, 0));
        as.setHealth(0);
    }
}
