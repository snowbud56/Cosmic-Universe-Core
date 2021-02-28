package com.snowbud56.preferences.prefs;

/*
* Created by snowbud56 on February 05, 2018
* Do not change or use this code without permission
*/

import com.snowbud56.Core;
import com.snowbud56.player.CorePlayer;
import com.snowbud56.player.PlayerManager;
import com.snowbud56.player.Rank;
import com.snowbud56.preferences.PlayerPref;
import com.snowbud56.preferences.PrefManager;
import com.snowbud56.preferences.events.ForcefieldEnableEvent;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class Forcefield implements Listener {

    public static int radius = 5;

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        PlayerPref prefs = PrefManager.getPlayerPrefs(p.getUniqueId());
        if (prefs.getForcefield()) {
            ForcefieldEnableEvent forcefieldEnableEvent = new ForcefieldEnableEvent(p);
            Bukkit.getPluginManager().callEvent(forcefieldEnableEvent);
        }
    }

    @EventHandler
    public void onForcefieldEnable(ForcefieldEnableEvent e) {
        Player p = e.getPlayer();
        System.out.println("[Core] Enabling " + p.getName() + "'s forcefield.");
        new BukkitRunnable() {
            public void run() {
                PlayerPref prefs = PrefManager.getPlayerPrefs(p.getUniqueId());
                if (!prefs.getForcefield() || !p.isOnline()) cancel();
                for (Entity target : p.getNearbyEntities(radius, radius, radius)) {
                    if (target instanceof Player && target != p) {
                        CorePlayer t = PlayerManager.getPlayer((Player) target);
                        if (!t.getRank().Has((Player) target, Rank.ADMIN, new Rank[] {Rank.SNOW, Rank.YOUTUBE}, false)) {
                            Location tloc = target.getLocation();
                            Vector v = new Vector((tloc.getX() - p.getLocation().getX()) / 5, 0.75, (tloc.getZ() - p.getLocation().getZ()) / 5);
                            target.setVelocity(v);
                            target.getWorld().playSound(tloc, Sound.CHICKEN_EGG_POP, 2, 0);
                        } else {
                            PacketPlayOutChat actionbar = new PacketPlayOutChat(IChatBaseComponent.ChatSerializer.a("{text:\"§7Ignoring §c" + p.getDisplayName() + "'s §7forcefield.\"}"), (byte) 2);
                            ((CraftPlayer) target).getHandle().playerConnection.sendPacket(actionbar);
                        }
                    }
                }
            }
        }.runTaskTimerAsynchronously(Core.getPlugin(), 0L, 5L);
    }
}
