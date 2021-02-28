package com.snowbud56.game.games;

/*
 * Created by snowbud56 on April 08, 2019
 * Do not change or use this code without permission
 */

import com.snowbud56.Core;
import com.snowbud56.game.GameBase;
import com.snowbud56.game.GameManager;
import com.snowbud56.game.GameState;
import com.snowbud56.game.GameType;
import com.snowbud56.util.Chat;
import com.snowbud56.util.ItemFactory;
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
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.Vector;

import java.util.Collections;
import java.util.HashMap;

public class QuakeMinigame extends GameBase implements Listener {
    private HashMap<Player, Integer> points;
    private boolean explosiveMode = false;
    private GameType gameType = GameType.QUAKE;
    private Scoreboard gameBoard;
    private Objective pointsObj;

    public QuakeMinigame(String mapName) {
        super("quake", mapName);
        points = new HashMap<>();
        gameBoard = GameManager.gameScoreboard;
        pointsObj = gameBoard.getObjective("points");
        if (pointsObj == null) pointsObj = gameBoard.registerNewObjective("points", "dummy");
    }

    @Override
    public GameType getType() {
        return gameType;
    }

    @Override
    public void postLobbyCountdown() {
        gameBoard.getObjective("points").setDisplaySlot(DisplaySlot.SIDEBAR);
        gameBoard.getObjective("points").setDisplayName(Chat.cGold + Chat.Bold + "QUAKECRAFT");
        ItemStack item = new ItemFactory(Material.WOOD_HOE).displayName(Chat.cGray + "Basic Railgun").lore(Collections.singletonList(Chat.cGray + "Pew! Pew!")).buildItem();
        for (Player player : players) {
            player.getInventory().clear();
            player.getInventory().addItem(item);
            gameBoard.getObjective("points").getScore(player.getName()).setScore(0);
            Team pTeam = gameBoard.getTeam(player.getName());
            if (pTeam == null) pTeam = gameBoard.registerNewTeam(player.getName());
            pTeam.setPrefix(Chat.cGray);
            pTeam.addEntry(player.getName());
        }
    }

    @Override
    public void gameStart() {
        for (Player p : players) {
            p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1));
        }
    }

    @Override
    public void preEndGame() {
        points.clear();
    }

    @Override
    public void postEndGame() {
        explosiveMode = false;
    }

    public void addPoint(Player p) {
        Integer value = points.get(p);
        if (value == null) value = 0;
        points.put(p, value + 1);
        GameManager.gameScoreboard.getObjective("points").getScore(p.getName()).setScore(value + 1);
        if (getPoints(p) >= 20) endGame(false, p);
    }

    public Integer getPoints(Player p) {
        points.putIfAbsent(p, 0);
        return points.get(p);
    }

    public void setPoints(Player p, Integer points) {
        this.points.put(p, points);
        GameManager.gameScoreboard.getObjective("points").getScore(p.getName()).setScore(points);
    }

    public boolean isExplosiveMode() {
        return explosiveMode;
    }

    public void setExplosiveMode(boolean explosiveMode) {
        this.explosiveMode = explosiveMode;
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        Player p = e.getPlayer();
        if (p.getGameMode().equals(GameMode.SURVIVAL))
            e.setCancelled(true);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        Player p = e.getPlayer();
        if (p.getGameMode().equals(GameMode.SURVIVAL))
            e.setCancelled(true);
    }

    @EventHandler
    public void FoodChange(FoodLevelChangeEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            if (isState(GameState.ACTIVE)){
                EntityDamageEvent.DamageCause cause = e.getCause();
                if (cause.equals(EntityDamageEvent.DamageCause.ENTITY_ATTACK) || cause.equals(EntityDamageEvent.DamageCause.FALL) || cause.equals(EntityDamageEvent.DamageCause.BLOCK_EXPLOSION) || cause.equals(EntityDamageEvent.DamageCause.ENTITY_EXPLOSION)) {
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerDeath(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            if (p.getHealth() - e.getDamage() <= 0) {
                if (isState(GameState.ACTIVE)) {
                    sendMessage(p.getDisplayName() + " died!");
                    respawn(p);
                }
            }
        }
    }

    private HashMap<Player, Long> dashcooldown = new HashMap<>();
    private HashMap<Player, Long> guncooldown = new HashMap<>();

    @EventHandler
    public void ShootGun(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        QuakeMinigame game = (QuakeMinigame) GameManager.getActiveGame();
        if (!game.isState(GameState.ACTIVE)) return;
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
                }.runTaskTimer(Core.getPlugin(), 0, 2);
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
                                    player.sendMessage(Chat.prefix + "You killed " + Chat.element(((Player) target).getDisplayName()) + "!");
                                    tp.sendMessage(Chat.prefix + "You were killed by " + Chat.element(player.getDisplayName()) + "!");
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
                game.sendMessage(Chat.prefix + Chat.element(player.getDisplayName()) + " got a " + killAmount + "-kill!");
            } else {
                player.sendMessage(Chat.prefix + "You can't shoot your gun yet!");
            }
        }
    }

    @EventHandler
    public void Dash(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        QuakeMinigame game = (QuakeMinigame) GameManager.getActiveGame();
        if (!game.isState(GameState.ACTIVE)) return;
        if (e.getPlayer().getInventory().getItemInHand() == null || e.getPlayer().getInventory().getItemInHand().getType() == Material.AIR) return;
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