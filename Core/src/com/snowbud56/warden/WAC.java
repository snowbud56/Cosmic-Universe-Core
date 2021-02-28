package com.snowbud56.warden;

/*
* Created by snowbud56 on March 17, 2018
* Do not change or use this code without permission
*/

import com.snowbud56.Core;
import com.snowbud56.player.CorePlayer;
import com.snowbud56.player.PlayerManager;
import com.snowbud56.player.Rank;
import com.snowbud56.util.Chat;
import com.snowbud56.util.PacketUtil;
import com.snowbud56.warden.checks.CheckResult;
import com.snowbud56.warden.util.WACPlayer;
import com.snowbud56.warden.util.WACSettings;
import net.minecraft.server.v1_8_R3.EnumParticle;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.UUID;

public class WAC implements Listener {

    private static HashMap<UUID, WACPlayer> players = new HashMap<>();

    public static void updatePlayers() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            players.put(p.getUniqueId(), new WACPlayer(p));
        }
    }

    public static void log(CheckResult cr, WACPlayer p) {
        if (!WACSettings.WACEnabled) return;
        if (((p.getViolations(cr.getType()) + 1) % cr.getType().getNotifyInterval()) == 0) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                CorePlayer target = PlayerManager.getPlayer(player);
                if (target.getRank().Has(player, Rank.HELPER, false)) {
                    player.sendMessage(Chat.Wprefix + Chat.element(p.getPlayer().getName()) + " suspected of " + Chat.element(cr.getType().getName()));
                }
            }
        }
        System.out.println(Chat.Wprefix + Chat.element(p.getPlayer().getName()) + " failed " + Chat.element(cr.getType().getName()) + ": " + cr.getMessage());
        p.addLog(cr.getType());
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        players.put(p.getUniqueId(), new WACPlayer(p));
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        players.remove(p.getUniqueId());
    }

    public static void banPlayer(WACPlayer player, Boolean banPlayer) {
        Player p = player.getPlayer();
        PlayerManager.getPlayer(p).setCanMove(false);
        //TODO come up with something cool to do
        new BukkitRunnable() {
            private Location origLoc = p.getLocation().add(0, 1, 0);
            private int time = 100;
            public void run() {
                Location loc = origLoc.clone();
                for (double i = 0; i < 49; i += 1) {
                    PacketUtil.displayParticle(new Location(loc.getWorld(), (loc.getX() + (Math.sin(i) * (time*0.1))), loc.getY(), (loc.getZ() + (Math.cos(i) * (time*0.1)))), EnumParticle.FLAME, 1);
                }
                time--;
                if (time <= 0) {
                    if (banPlayer) {
                        ((CraftPlayer) p).setBanned(true);
                        p.kickPlayer(Chat.Wprefix + "You have been banned by Warden for cheating.\n" + Chat.Wprefix + "If you think this was an error, please contact support.");
                    }
                    PlayerManager.getPlayer(p).setCanMove(true);
                    this.cancel();

                }
            }
        }.runTaskTimer(Core.getPlugin(), 0, 2);
    }

    public static WACPlayer getPlayer(Player p) {
        return players.get(p.getUniqueId());
    }
}
