package com.snowbud56.game.API;

import com.snowbud56.QuakeMinigame;
import com.snowbud56.game.constructors.Game;
import com.snowbud56.util.Chat;
import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.HashMap;

public class QuakeGuns implements Listener {

    private HashMap<Player, Long> dashcooldown;
    private HashMap<Player, Long> guncooldown;

    public QuakeGuns() {
        dashcooldown = new HashMap<>();
        guncooldown = new HashMap<>();
    }

    @EventHandler
    public void ShootGun(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        Game game = QuakeMinigame.getGame();
        if (!game.isState(Game.GameState.ACTIVE)) return;
        if ((e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) && (e.getItem().getType() == Material.WOOD_HOE)) {
            guncooldown.putIfAbsent(player, (long) 0);
            if (guncooldown.get(player) < System.currentTimeMillis() - 1) {
                guncooldown.put(player, System.currentTimeMillis() + 1500);
                Location location = player.getLocation();
                Vector direction = location.getDirection().normalize();
                new BukkitRunnable() {
                    Double time = 1.5;
                    @Override
                    public void run() {
                        if (time <= 0) {
                            String text = "§7You may shoot your gun";
                            PacketPlayOutChat actionbar = new PacketPlayOutChat(IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + text + "\"}"), (byte) 2);
                            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(actionbar);
                            cancel();
                        }
                        String text = "§7You may shoot your gun in §c" + (double) Math.round((time * 100) / 10) / 10 + "§7 second" + (time == 1 ? "" : "s") + ".";
                        PacketPlayOutChat actionbar = new PacketPlayOutChat(IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + text + "\"}"), (byte) 2);
                        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(actionbar);
                        time -= 0.1;
                    }
                }.runTaskTimer(QuakeMinigame.getInstance(), 0, 2);
                int kills = 0;
                for (double t = 0.5; t < 60; t = t + 0.5) {
                    float x = (float) direction.getX() * (float) t;
                    float y = (float) direction.getY() * (float) t + (float) 1.5;
                    float z = (float) direction.getZ() * (float) t;
                    location.add(x, y, z);
                    if (location.getBlock().getType() != Material.AIR) {
                        if (game.isExplosiveMode()) {
                            TNTPrimed tnt = (TNTPrimed) player.getWorld().spawn(location, EntityType.PRIMED_TNT.getEntityClass());
                            tnt.setFuseTicks(0);
                        }
                        return;
                    }
                    PacketPlayOutWorldParticles particles = new PacketPlayOutWorldParticles(EnumParticle.FIREWORKS_SPARK, true, (float) location.getX(), (float) location.getY(), (float) location.getZ(), 0F, 0F, 0F, 0, 1);
                    for (Player worldplayer : player.getWorld().getPlayers())
                        ((CraftPlayer) worldplayer).getHandle().playerConnection.sendPacket(particles);
                    if (!game.isExplosiveMode()) {
                        for (Entity target : location.getWorld().getNearbyEntities(location, 1, 3, 1)) {
                            if (target instanceof Player && target != player) {
                                Player tp = (Player) target;
                                if (!tp.getGameMode().equals(GameMode.SPECTATOR)) {
                                    player.sendMessage(Chat.prefix + "You killed " + Chat.element(target.getName()) + "!");
                                    tp.sendMessage(Chat.prefix + "You were killed by " + Chat.element(player.getName()) + "!");
                                    game.respawn((Player) target);
                                    game.addPoint(player);
                                    kills++;
                                }
                            }
                        }
                    }
                    location.subtract(x, y, z);
                }
                if (kills < 2) return;
                String killAmount;
                switch (kills) {
                    case 2:
                        killAmount = "Double";
                        break;
                    case 3:
                        killAmount = "Triple";
                        break;
                    case 4:
                        killAmount = "Quadruple";
                        break;
                    case 5:
                        killAmount = "Penta";
                        break;
                    default:
                        killAmount = "Multi";
                        break;
                }
                player.sendMessage(Chat.prefix + killAmount + "-kill!");
                game.sendMessage(Chat.prefix + Chat.element(player.getName()) + " got a " + killAmount + "-kill!");
            } else {
                player.sendMessage(Chat.prefix + "You can't shoot your gun yet!");
            }
        }
    }

    @EventHandler
    public void Dash(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        Game game = QuakeMinigame.getGame();
        if (!game.isState(Game.GameState.ACTIVE)) return;
        if ((e.getAction().equals(Action.LEFT_CLICK_AIR) || e.getAction().equals(Action.LEFT_CLICK_BLOCK)) && (e.getItem().getType().equals(Material.WOOD_HOE))) {
            dashcooldown.putIfAbsent(p, (long) 0);
            if (dashcooldown.get(p) < System.currentTimeMillis() - 1) {
                Vector direction = p.getLocation().getDirection();
                p.setVelocity(new Vector(direction.getX(), direction.getY()/2, direction.getZ()).multiply(1.5));
                dashcooldown.put(p, System.currentTimeMillis() + 2000);
            } else {
                p.sendMessage(Chat.prefix + "You can't use your dash ability yet!");
            }
        }
    }
}
