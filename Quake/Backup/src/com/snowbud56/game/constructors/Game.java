package com.snowbud56.game.constructors;

import com.snowbud56.QuakeMinigame;
import com.snowbud56.RollbackHandler;
import com.snowbud56.game.data.DataHandler;
import com.snowbud56.util.Chat;
import com.snowbud56.util.ItemFactory;
import com.snowbud56.util.TimeUtil;
import com.snowbud56.util.managers.LogManager;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.io.File;
import java.util.*;

public class Game {

    private String displayName;
    private int maxPlayers;
    private int minPlayers;
    private World world;
    private List<Location> spawnPoints;
    private GameState gameState;
    private Location lobbyPoint;
    private Scoreboard gameboard = Bukkit.getScoreboardManager().getNewScoreboard();
    private Scoreboard lobbyboard = Bukkit.getScoreboardManager().getNewScoreboard();
    private Objective gameobj = gameboard.getObjective("points");
    private Objective lobbyobj = lobbyboard.getObjective("points");
    private Team playercount;
    private boolean explosiveMode = false;

    private Set<Player> players;
    private Set<Player> spectators;
    private HashMap<Player, Integer> points;
    private boolean isMovementFrozen = false;

    public Game(String gameName) {
        long starttime = System.currentTimeMillis();
        FileConfiguration fileConfiguration = DataHandler.getInstance().getGameInfo();
        this.spectators = new HashSet<>();
        this.players = new HashSet<>();
        this.spawnPoints = new ArrayList<>();
        this.points = new HashMap<>();
        if (gameobj == null) gameobj = gameboard.registerNewObjective("dummy", "points");
        if (lobbyobj == null) lobbyobj = lobbyboard.registerNewObjective("dummy", "main");
        gameobj.setDisplaySlot(DisplaySlot.SIDEBAR);
        this.displayName = fileConfiguration.getString("games." + gameName + ".displayName");
        gameobj.setDisplayName(Chat.cGold + Chat.Bold + displayName);
        this.maxPlayers = fileConfiguration.getInt("games." + gameName + ".maxPlayers");
        this.minPlayers = fileConfiguration.getInt("games." + gameName + ".minPlayers");
        File file = new File("main_active");
        if (file.exists()) RollbackHandler.getRollbackHandler().delete(file);
        this.world = Bukkit.createWorld(new WorldCreator("main_active"));
        RollbackHandler.getRollbackHandler().rollback();
        try {
            String[] values = fileConfiguration.getString("games.main.lobbyPoint").split(",");
            double x = Double.parseDouble(values[0].split(":")[1]);
            double y = Double.parseDouble(values[1].split(":")[1]);
            double z = Double.parseDouble(values[2].split(":")[1]);
            lobbyPoint = new Location(Bukkit.getWorld("world"), x, y, z);
        } catch (Exception e) {
            QuakeMinigame.getInstance().getLogger().severe("Failed to load lobbyPoint for game '" + gameName + "'. ExceptionType: " + e);
            e.printStackTrace();
        }
        for (String location : DataHandler.getInstance().getGameInfo().getStringList("games." + gameName + ".spawnPoints")) {
            try {
                String[] values = location.split(",");
                double x = Double.parseDouble(values[0].split(":")[1]);
                double y = Double.parseDouble(values[1].split(":")[1]);
                double z = Double.parseDouble(values[2].split(":")[1]);
                Location loc = new Location(world, x, y, z);
                spawnPoints.add(loc);
            } catch (Exception e) {
                QuakeMinigame.getInstance().getLogger().severe("Failed to load spawnPoint with metadata " + location + " for game + '" + gameName + "'. ExceptionType: " + e);
            }
        }
        playercount = lobbyboard.getTeam("playercount");
        if (playercount == null) playercount = lobbyboard.registerNewTeam("playercount");
        playercount.addEntry(" / ");
        playercount.setPrefix("" + players.size());
        playercount.setSuffix("" + maxPlayers);
        lobbyobj.setDisplaySlot(DisplaySlot.SIDEBAR);
        lobbyobj.setDisplayName(Chat.cGold + Chat.Bold + displayName);
        lobbyobj.getScore(Chat.cYellow + Chat.Bold + "Players").setScore(16);
        lobbyobj.getScore(" / ").setScore(15);
        lobbyobj.getScore(Chat.cDBlue).setScore(14);
        lobbyobj.getScore(Chat.cGreen + Chat.Bold + "Map").setScore(13);
        lobbyobj.getScore("Hypixel").setScore(12);
        lobbyobj.getScore("----------------").setScore(11);
        LogManager.logConsole("Completed game setup in " + TimeUtil.convertmstoTime(System.currentTimeMillis() - starttime));
        setState(GameState.LOBBY);
    }

    public void quitGame(Player player) {
        if (players.contains(player)) players.remove(player);
        else if (spectators.contains(player)) spectators.remove(player);
        sendMessage(player.getName() + " left.");
        player.setHealth(player.getMaxHealth());
        playercount.setPrefix("" + players.size());
        gameboard.resetScores(player.getName());
        if (isState(GameState.ACTIVE) && players.size() <= 1) endGame(false, (Player) players);
    }

    public void joinGame(Player player) {
        if (players.contains(player)) return;
        if (isState(GameState.LOBBY) || isState(GameState.STARTING)) {
            player.setScoreboard(lobbyboard);
            player.getInventory().clear();
            player.getInventory().setItem(7, new ItemFactory(Material.CHEST).amount(1).displayName(Chat.cGreen + "Shop" + Chat.cGray + " [Right-Click]").buildItem());
            this.players.add(player);
            playercount.setPrefix("" + players.size());
            player.teleport(lobbyPoint);
            player.setMaxHealth(20);
            player.setHealth(player.getMaxHealth());
            player.setFoodLevel(25);
            sendMessage(player.getName() + " joined.");
        }
        if (players.size() == minPlayers && !isState(GameState.STARTING)) {
            startCountDown(false);
        }
        if (isState(GameState.ACTIVE)) {
            spectators.add(player);
            player.setScoreboard(gameboard);
            sendMessage(player.getName() + " joined.");
            player.setGameMode(GameMode.SPECTATOR);
            player.teleport(spawnPoints.get(0));
        }
    }

    public void respawn(Player player) {
        if (players.contains(player)) {
            List<Location> spawns = getSpawnPoints();
            Random r = new Random();
            Integer v = r.nextInt(spawns.size() - 1);
            boolean foundSpawn = false;
            while (!foundSpawn) {
                for (Player target : players) {
                    if (target.getLocation().distance(spawns.get(v)) < 10) {
                        v = v + 1;
                        continue;
                    }
                }
                foundSpawn = true;
            }
            player.teleport(spawns.get(v));
            player.damage(1);
            player.setHealth(player.getMaxHealth());
        }
    }

    public void startCountDown(Boolean forceStart) {
        setState(GameState.STARTING);
        sendMessage("You will be teleported in 20 seconds!");
        if (forceStart) playSound(Sound.NOTE_PLING, 1);
        new BukkitRunnable() {
            private int time = 20;
            @Override
            public void run() {
                if (isState(GameState.STARTING)) {
                    if (players.size() < minPlayers && !forceStart) {
                        sendMessage("Stop cancelled. (Not enough players)");
                        lobbyobj.setDisplayName(Chat.cGold + Chat.Bold + displayName);
                        setState(GameState.LOBBY);
                        cancel();
                        return;
                    }
                    time--;
                    if (time <= 0) {
                        startGame(forceStart);
                        isMovementFrozen = true;
                        lobbyobj.setDisplayName(Chat.cGold + Chat.Bold + displayName);
                        int id = 0;
                        ItemStack item = new ItemStack(Material.WOOD_HOE, 1);
                        ItemMeta im = item.getItemMeta();
                        im.spigot().setUnbreakable(true);
                        im.setDisplayName(Chat.cGray + "Basic Railgun");
                        im.setLore(Collections.singletonList(Chat.cGray + "Pew! Pew!"));
                        item.setItemMeta(im);
                        for (Player player : players) {
                            player.getInventory().clear();
                            player.getInventory().addItem(item);
                            gameobj.getScore(player.getName()).setScore(0);
                            player.setScoreboard(gameboard);
                            try {
                                player.teleport(getSpawnPoints().get(id));
                                id += 1;
                            } catch (IndexOutOfBoundsException ex) {
                                player.teleport(spawnPoints.get(0));
                                id = 0;
                            }
                        }
                        cancel();
                    } else {
                        lobbyobj.setDisplayName("§lStarting in §a§l" + time + " second" + (time == 1 ? "" : "s"));
                        if (time % 5 == 0 || (time <= 5)) {
                            playSound(Sound.NOTE_PLING, 1);
                            sendMessage("You will be teleported in " + time + " second" + (time == 1 ? "" : "s") + "!");
                        }
                    }
                } else {
                    lobbyobj.setDisplayName(Chat.cGold + Chat.Bold + displayName);
                    cancel();
                }
            }
        }.runTaskTimer(QuakeMinigame.getInstance(), 0, 20);
    }

    private void startGame(Boolean forceStart) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(QuakeMinigame.getInstance(), () -> playSound(Sound.LEVEL_UP, 1), 2);
        new BukkitRunnable() {
            private int startIn = 11;
            @Override
            public void run() {
                if (players.size() < minPlayers && !forceStart) {
                    cancel();
                    endGame(false, null);
                    return;
                }
                if (startIn <= 1) {
                    this.cancel();
                    setState(Game.GameState.ACTIVE);
                    sendMessage("The game has started.");
                    isMovementFrozen = false;
                    for (Player p : players) p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1));
                } else {
                    startIn -= 1;
                    sendMessage("The game will begin in " + startIn + " second" + (startIn == 1 ? "" : "s") + ".");
                    playSound(Sound.CLICK, 1);
                }
            }
        }.runTaskTimer(QuakeMinigame.getInstance(), 0, 20);
    }

    public void endGame(Boolean forced, Player winner) {
        setState(GameState.ENDING);
        PacketPlayOutTitle times = new PacketPlayOutTitle(20, 160, 20);
        PacketPlayOutTitle title;
        PacketPlayOutTitle subtitle = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, IChatBaseComponent.ChatSerializer.a("{text:\"won the game!\",color:yellow,bold:true}"));
        if (winner != null) {
            title = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, IChatBaseComponent.ChatSerializer.a("{text:\"" + winner.getName() + "\",color:yellow,bold:true}"));
        } else {
            title = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, IChatBaseComponent.ChatSerializer.a("{text:\"No one\",color:yellow,bold:true}"));
        }
        for (Player p : Bukkit.getOnlinePlayers()) {
            ((CraftPlayer) p).getHandle().playerConnection.sendPacket(times);
            ((CraftPlayer) p).getHandle().playerConnection.sendPacket(title);
            ((CraftPlayer) p).getHandle().playerConnection.sendPacket(subtitle);
            gameboard.resetScores(p.getName());
        }
        new BukkitRunnable() {
            private int time = 40;
            private EnderDragon dragon;
            @Override
            public void run() {
                if (time <= 0) {
                    this.cancel();
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        player.removePotionEffect(PotionEffectType.SPEED);
                        player.teleport(lobbyPoint);
                        player.setScoreboard(lobbyboard);
                        player.setGameMode(GameMode.SURVIVAL);
                        player.setMaxHealth(20);
                        player.setHealth(player.getMaxHealth());
                        player.setFoodLevel(25);
                        player.getInventory().clear();
                    }
                    for (Player p : spectators) {
                        players.add(p);
                        spectators.remove(p);
                    }
                    points.clear();
                    explosiveMode = false;
                    RollbackHandler.getRollbackHandler().rollback();
                    setState(GameState.LOBBY);
                    if (players.size() >= minPlayers && !forced) startCountDown(false);
                } else if (winner != null && time >= 5) {
                    if (!winner.getName().equals("snowbud56")) {
                        Firework firework = (Firework) winner.getWorld().spawnEntity(winner.getLocation(), EntityType.FIREWORK);
                        FireworkMeta fm = firework.getFireworkMeta();
                        fm.setPower(0);
                        fm.addEffect(FireworkEffect.builder().withColor(Color.YELLOW).with(FireworkEffect.Type.BALL_LARGE).build());
                        firework.setFireworkMeta(fm);
                    } else {
                        if (time == 40) {
                            dragon = (EnderDragon) winner.getWorld().spawnEntity(winner.getLocation(), EntityType.ENDER_DRAGON);
                            dragon.setCustomNameVisible(false);
                            dragon.setNoDamageTicks(Integer.MAX_VALUE);
                            dragon.setPassenger(winner);
                        }
                        if (time % 2 == 0) {
                            winner.launchProjectile(Fireball.class, winner.getVelocity());
                        }
                    }
                }
                time = time - 1;
            }
        }.runTaskTimer(QuakeMinigame.getInstance(), 0, 5);
    }

    public void addPoint(Player p) {
        Integer value = points.get(p);
        if (value == null) value = 0;
        points.put(p, value + 1);
        gameobj.getScore(p.getName()).setScore(value + 1);
        if (getPoints(p) >= 20) endGame(false, p);
    }

    public Integer getPoints(Player p) {
        points.putIfAbsent(p, 0);
        return points.get(p);
    }
    public void setPoints(Player p, Integer points) {
        this.points.put(p, points);
        gameobj.getScore(p.getName()).setScore(points);
    }

    public boolean isExplosiveMode() {
        return explosiveMode;
    }

    public void setExplosiveMode(boolean explosiveMode1) {
        explosiveMode = explosiveMode1;
    }

    public boolean isMovementFrozen() {
        return isMovementFrozen;
    }

    public boolean isState(GameState state) {
        return gameState.equals(state);
    }

    public void setState(GameState gameState) {
        LogManager.logConsole("GameBase Status: " + gameState.name());
        this.gameState = gameState;
    }

    public List<Location> getSpawnPoints() {
        return spawnPoints;
    }

    public void sendMessage(String message) {
        Bukkit.broadcastMessage(Chat.prefix + message);
    }

    private void playSound(Sound sound, float pitch) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.playSound(player.getLocation(), sound, 1, pitch);
        }
    }

    public enum GameState {
        LOBBY, STARTING, ACTIVE, ENDING
    }
}
