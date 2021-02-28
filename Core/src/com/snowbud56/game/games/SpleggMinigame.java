package com.snowbud56.game.games;

/*
 * Created by snowbud56 on April 16, 2019
 * Do not change or use this code without permission
 */

import com.snowbud56.Core;
import com.snowbud56.game.API.GameLeaveEvent;
import com.snowbud56.game.*;
import com.snowbud56.util.Chat;
import com.snowbud56.util.ItemFactory;
import com.snowbud56.util.ScoreboardUtil;
import com.snowbud56.util.TimeUtil;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.Vector;

import java.util.Arrays;
import java.util.HashMap;

public class SpleggMinigame extends GameBase implements Listener {
    private HashMap<Player, Scoreboard> playerGameBoards = new HashMap<>();
    private HashMap<Player, Long> shootTimer = new HashMap<>();
    private HashMap<Player, Integer> jumpCounts = new HashMap<>();
    private HashMap<Player, Long> jumpTimer = new HashMap<>();
    private long timeLeft;

    //Settings
    private static final int shootDelay = 100;
    private static final int jumpDelay = 5000;
    private static final int gameLength = 300000;

    public SpleggMinigame(String mapName) {
        super("splegg", mapName);
        setupScoreboard();
    }

    private void setupScoreboard() {
        Scoreboard board = GameManager.gameScoreboard;
        Team timeLeft = board.getTeam("timeLeft");
        if (timeLeft == null) timeLeft = board.registerNewTeam("timeLeft");
        timeLeft.addEntry(Chat.cGreen);
    }

    @EventHandler
    public void onGameQuit(GameLeaveEvent e) {
        Player player = e.getPlayer();
        jumpCounts.remove(player);
        jumpTimer.remove(player);
        shootTimer.remove(player);
        playerGameBoards.remove(player);
    }

    @Override
    public GameType getType() {
        return null;
    }

    @Override
    public void postLobbyCountdown() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.getInventory().clear();
            playerGameBoards.put(player, Bukkit.getServer().getScoreboardManager().getNewScoreboard());
            Scoreboard board = playerGameBoards.get(player);
            Objective obj = board.getObjective(player.getName());
            if (obj == null) obj = board.registerNewObjective(player.getName(), "dummy");
            obj.setDisplaySlot(DisplaySlot.SIDEBAR);
            obj.setDisplayName(Chat.cGold + Chat.Bold + "SPLEGG");
            ScoreboardUtil.setupScores(obj, new String[] {
                    Chat.cYellow + Chat.Bold + "TIME LEFT",
                    Chat.cGreen + "",
                    Chat.cDBlue + "",
                    Chat.cGreen + Chat.Bold + "DOUBLE JUMPS",
                    Chat.cAqua
            });
            Team team = board.getTeam(player.getName());
            if (team == null) team = board.registerNewTeam(player.getName());
            team.setSuffix(Chat.cWhite + "3");
            team.addEntry(Chat.cAqua);
            Team team1 = board.getTeam("timeLeft");
            if (team1 == null) team1 = board.registerNewTeam("timeLeft");
            team1.setSuffix(Chat.cWhite + TimeUtil.convertmstoTime(gameLength));
            team1.addEntry(Chat.cGreen);
            player.setScoreboard(playerGameBoards.get(player));
        }
        for (Player player : Bukkit.getOnlinePlayers()) {
            for (Player target : Bukkit.getOnlinePlayers()) {
                Team pTeam = playerGameBoards.get(target).getTeam(player.getName());
                if (pTeam == null) pTeam = playerGameBoards.get(target).registerNewTeam(player.getName());
                pTeam.setPrefix(Chat.cGray);
                pTeam.addEntry(player.getName());
            }
        }
    }

    @Override
    public void gameStart() {
        timeLeft = System.currentTimeMillis() + gameLength;
        for (Player player : players) {
            player.setAllowFlight(true);
            player.getInventory().addItem(new ItemFactory(Material.DIAMOND_SPADE)
                    .displayName(Chat.cBlue + Chat.Bold + "The Snowballer")
                    .lore(Arrays.asList(Chat.cGray + "Right-click this to shoot a snowball", Chat.cGray + "Snowballs will destroy any blocks it hits."))
                    .unbreakable(true)
                    .buildItem());
        }
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!isState(GameState.ACTIVE)) cancel();
                else {
                    for (Player player : Bukkit.getOnlinePlayers())
                        playerGameBoards.get(player).getTeam("timeLeft").setSuffix(Chat.cWhite + TimeUtil.convertmstoTime(timeLeft - System.currentTimeMillis()));
                    if (timeLeft - System.currentTimeMillis() <= 0) {
                        endGame(false, null);
                    }
                }
            }
        }.runTaskTimer(Core.getPlugin(), 0, 2);
    }

    @Override
    public void preEndGame() {
        timeLeft = 0;
    }

    @Override
    public void postEndGame() {
        playerGameBoards.clear();
        shootTimer.clear();
        jumpCounts.clear();
        jumpTimer.clear();
        for (Player player : Bukkit.getOnlinePlayers())
            player.setAllowFlight(false);
    }

    @EventHandler
    public void onShoot(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        Game game = GameManager.getActiveGame();
        if (!game.isState(GameState.ACTIVE)) return;
        if ((e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) && (e.getItem().getType() == Material.DIAMOND_SPADE)) {
            shootTimer.putIfAbsent(player, (long) 0);
            if (shootTimer.get(player) < System.currentTimeMillis() - 1) {
                shootTimer.put(player, System.currentTimeMillis() + shootDelay);
                Location location = player.getLocation().add(0, 2, 0);
                Vector direction = player.getLocation().getDirection().normalize().multiply(2);
                Snowball snowball = (Snowball) Bukkit.getWorld("main_active").spawnEntity(location, EntityType.SNOWBALL);
                snowball.setShooter(player);
                snowball.setCustomName("spleggSnowball");
                snowball.setCustomNameVisible(false);
                snowball.setVelocity(direction);
            }
        }
    }

    @EventHandler
    public void onProjectileLand(ProjectileHitEvent e) {
        if (!isState(GameState.ACTIVE)) return;
        if (e.getEntity().getShooter() instanceof Player && e.getEntityType() == EntityType.SNOWBALL) {
            Player shooter = (Player) e.getEntity().getShooter();
            if (players.contains(shooter)) {
//                Location loc = e.getEntity().getLocation();
//                Vector vec = e.getEntity().getVelocity().multiply(0.25);
//                Block block = new Location(loc.getWorld(), loc.getX()+vec.getX(), loc.getY()-0.45, loc.getZ()+vec.getZ()).getBlock();
                Block block = e.getEntity().getLocation().getBlock().getRelative(BlockFace.DOWN);
                e.getEntity().getWorld().playEffect(block.getLocation(), Effect.STEP_SOUND, block.getType().getId());
                block.setType(Material.AIR);
            }
        }
    }

    @EventHandler
    public void onLeap(PlayerToggleFlightEvent e) {
        Player player = e.getPlayer();
        Game game = GameManager.getActiveGame();
        if (!game.isState(GameState.ACTIVE) || player.getGameMode() != GameMode.SURVIVAL) return;
        if (!player.isFlying() && players.contains(player)) {
            e.setCancelled(true);
            player.setAllowFlight(false);
            int count = jumpCounts.getOrDefault(player, 3);
            if (count > 0) {
                jumpTimer.putIfAbsent(player, (long) 0);
                if (jumpTimer.get(player) < System.currentTimeMillis() - 1) {
                    jumpTimer.put(player, System.currentTimeMillis() + jumpDelay);
                    player.setVelocity(player.getEyeLocation().getDirection().normalize().multiply(1.2));
                    jumpCounts.put(player, count - 1);
                    playerGameBoards.get(player).getTeam(player.getName()).setPrefix(jumpCounts.get(player) + "");
                    if (count - 1 > 0) {
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                player.sendMessage(Chat.prefix + "You may now use " + Chat.element("double jump") + ".");
                                player.setAllowFlight(true);
                            }
                        }.runTaskLater(Core.getPlugin(), (jumpDelay / 1000) * 20);
                    }
                }
            } else
                player.sendMessage(Chat.prefix + "You have no more jumps left!");
        }
    }

    @EventHandler
    public void onPlayerDeath(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            if (p.getHealth() - e.getDamage() <= 0) {
                if (isState(GameState.ACTIVE)) {
                    sendMessage(p.getDisplayName() + " died!");
                    setSpectator(p);
                    if (players.size() <= 1) endGame(false, (players.size() == 1 ? players.iterator().next() : null));
                }
            }
        }
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            if (isState(GameState.ACTIVE)) {
                if (e.getCause() == EntityDamageEvent.DamageCause.FALL || e.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK || e.getCause() == EntityDamageEvent.DamageCause.PROJECTILE)
                    e.setCancelled(true);
            }
        }
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
}
