package com.snowbud56.game;

import com.snowbud56.Core;
import com.snowbud56.game.API.GameJoinEvent;
import com.snowbud56.game.API.GameLeaveEvent;
import com.snowbud56.game.API.GameStateChangeEvent;
import com.snowbud56.player.PlayerManager;
import com.snowbud56.util.Chat;
import com.snowbud56.util.ItemFactory;
import com.snowbud56.util.TimeUtil;
import com.snowbud56.util.game.DataHandler;
import com.snowbud56.util.game.RollbackHandler;
import com.snowbud56.util.managers.LogManager;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.io.File;
import java.util.*;

public abstract class GameBase implements Game, Listener {

    private String gameName, gameDisplayName, mapDisplayName, mapName;
    private int countdown = 20;
    private int maxPlayers;
    private int minPlayers;
    protected World world;
    private List<Location> spawnPoints;
    private GameState gameState;
//    protected Scoreboard gameboard = Bukkit.getScoreboardManager().getNewScoreboard();
//    protected Scoreboard lobbyboard = Bukkit.getScoreboardManager().getNewScoreboard();
//    protected Objective gameobj = gameboard.getObjective("points");
//    protected Objective lobbyobj = lobbyboard.getObjective("points");
//    protected Team playercount;

    protected Set<Player> players;
    protected Set<Player> spectators;
    protected boolean isMovementFrozen = false;

    public GameBase(String gameName, String mapName) {
        long starttime = System.currentTimeMillis();
        loadMap(gameName, mapName);
        setupVariables(gameName, mapName);
        setupScoreboards();
        Bukkit.getPluginManager().registerEvents(this, Core.getPlugin());
        LogManager.logConsole("Completed game setup in " + TimeUtil.convertmstoTime(System.currentTimeMillis() - starttime));
        setState(GameState.LOBBY);
        for (Player player : Bukkit.getOnlinePlayers()) joinGame(player, true, false);
    }

    private void setupVariables(String gameName, String mapName) {
        FileConfiguration fileConfiguration = DataHandler.getInstance().getGameInfo();
        this.spectators = new HashSet<>();
        this.players = new HashSet<>();
        this.gameName = gameName;
        this.mapDisplayName = fileConfiguration.getString("mapName");
        this.gameDisplayName = fileConfiguration.getString("displayName");
        this.mapName = mapName;
        this.maxPlayers = fileConfiguration.getInt("maxPlayers");
        this.minPlayers = fileConfiguration.getInt("minPlayers");
    }

    private void setupScoreboards() {
        Scoreboard lobbyboard = GameManager.lobbyScoreboard;
        lobbyboard.getTeam("gameName").setPrefix(gameDisplayName);
        lobbyboard.getTeam("mapName").setPrefix(mapDisplayName);
        lobbyboard.getTeam("playercount").setPrefix("0");
        lobbyboard.getTeam("playercount").setSuffix(maxPlayers + "");
    }

    private void loadMap(String gameName, String mapName) {
        long startTime = System.currentTimeMillis();
        this.spawnPoints = new ArrayList<>();
        File file = new File("main_active");
        if (file.exists()) RollbackHandler.getRollbackHandler().delete(file);
        this.world = Bukkit.createWorld(new WorldCreator("main_active"));
        RollbackHandler.getRollbackHandler().rollback(gameName, mapName);
        for (String location : DataHandler.getInstance().getGameInfo().getStringList("spawnPoints")) {
            try {
                String[] values = location.split(",");
                double x = Double.parseDouble(values[0].split(":")[1]);
                double y = Double.parseDouble(values[1].split(":")[1]);
                double z = Double.parseDouble(values[2].split(":")[1]);
                Location loc = new Location(world, x, y, z);
                spawnPoints.add(loc);
            } catch (Exception e) {
                LogManager.logWarning("Failed to load spawnPoint with metadata " + location + " for game '" + gameName + "' and map '" + mapName + "'. ExceptionType: " + e);
            }
        }
        LogManager.logConsole("Completed map load in " + TimeUtil.convertmstoTime(System.currentTimeMillis() - startTime));
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(this);
        this.spectators = null;
        this.players = null;
        this.spawnPoints = null;
        this.gameName = null;
        this.mapName = null;
        if (isState(GameState.ACTIVE)) endGame(true, null);
    }

    @Override
    public void quitGame(Player player) {
        if (players.contains(player)) players.remove(player);
        else if (spectators.contains(player)) spectators.remove(player);
        sendMessage(player.getName() + " left.");
        player.setHealth(player.getMaxHealth());
        GameManager.lobbyScoreboard.getTeam("playercount").setPrefix("" + players.size());
        GameManager.gameScoreboard.resetScores(player.getName());
        if (isState(GameState.ACTIVE) && players.size() <= 1) endGame(false, (players.size() == 1 ? (Player) players : null));
        GameLeaveEvent event = new GameLeaveEvent(player);
        Bukkit.getServer().getPluginManager().callEvent(event);
    }

    @Override
    public void joinGame(Player player, boolean newGame, boolean initialSetup) {
        if (players.contains(player)) return;
        if (isState(GameState.LOBBY) || isState(GameState.STARTING)) {
            player.setScoreboard(GameManager.lobbyScoreboard);
            player.getInventory().clear();
            player.getInventory().setItem(7, new ItemFactory(Material.CHEST).amount(1).displayName(Chat.cGreen + "Shop" + Chat.cRed + " [Development]").buildItem());
            this.players.add(player);
            GameManager.lobbyScoreboard.getTeam("playercount").setPrefix("" + players.size());
            player.teleport(GameManager.lobbyPoint);
            player.setMaxHealth(20);
            player.setHealth(player.getMaxHealth());
            player.setFoodLevel(25);
            if (!newGame) sendMessage(player.getName() + " joined.");
            Team team = GameManager.lobbyScoreboard.getTeam(player.getName());
            if (team == null) team = GameManager.lobbyScoreboard.registerNewTeam(player.getName());
            team.setPrefix(PlayerManager.getPlayer(player).getDisplayRank().getTag(false, true, true) + "§f");
            team.addEntry(player.getName());
            Team team1 = GameManager.gameScoreboard.getTeam(player.getName());
            if (team1 == null) team1 = GameManager.gameScoreboard.registerNewTeam(player.getName());
            team1.setPrefix("§7");
            team1.addEntry(player.getName());
        }
        if (players.size() == minPlayers && isState(GameState.LOBBY)) {
            setCountdown(20);
            startCountDown(false);
        }
        if (isState(GameState.ACTIVE)) {
            spectators.add(player);
            player.setScoreboard(GameManager.gameScoreboard);
            if (!newGame) sendMessage(player.getName() + " joined.");
            setSpectator(player);
        }
        GameJoinEvent event = new GameJoinEvent(player);
        Bukkit.getServer().getPluginManager().callEvent(event);
    }

    @Override
    public void respawn(Player player) {
        if (players.contains(player)) {
            List<Location> spawns = getSpawnPoints();
            Random r = new Random();
            int v = r.nextInt(spawns.size() - 1);
            boolean foundSpawn = false;
            int i = 0;
            while (!foundSpawn) {
                i++;
                for (Player target : players) {
                    if (target == player) continue;
//                    if (target.getWorld().getName().toLowerCase().equals(spawns.get(v).getWorld().getName().toLowerCase())) continue;
                    Location targetLocation = target.getLocation().clone();
                    targetLocation.setWorld(Bukkit.getWorld("world"));
                    Location spawnLocation = spawns.get(v).clone();
                    spawnLocation.setWorld(Bukkit.getWorld("world"));
                    if (targetLocation.distance(spawnLocation) < 10) {
                        v = v + 1;
                        if (v > spawns.size()) {
                            v = r.nextInt(spawns.size() - 1);
                            foundSpawn = true;
                        }
                    }
                }
                if (i >= 100)  {
                    v = 0;
                    break;
                }
            }
            player.teleport(spawns.get(v));
            player.damage(1);
            player.setHealth(player.getMaxHealth());
        } else
            setSpectator(player);
    }

    @Override
    public void setSpectator(Player player) {
        spectators.add(player);
        players.remove(player);
        player.teleport(spawnPoints.get(0));
        for (Player target : Bukkit.getOnlinePlayers())
            target.hidePlayer(player);
        player.getInventory().clear();
        player.setAllowFlight(true);
        player.setFlying(true);
        player.setCanPickupItems(false);
    }

    @EventHandler
    public void spectatorInteraction(PlayerInteractEvent e) {
        if (isState(GameState.ACTIVE))
            e.setCancelled(true);
    }

    @Override
    public void setCountdown(int countdown) {
        this.countdown = countdown;
    }

    @Override
    public void startCountDown(Boolean forceStart) {
        setState(GameState.STARTING);
        if (countdown <= 0) countdown = 20;
        sendMessage("You will be teleported in " + countdown + " second" + (countdown == 1 ? "" : "s") + "!");
        if (forceStart) playSound(Sound.NOTE_PLING, 1);
        new BukkitRunnable() {
            @Override
            public void run() {
                if (isState(GameState.STARTING)) {
                    if (players.size() < minPlayers && !forceStart) {
                        sendMessage("Stop cancelled. (Not enough players)");
                        GameManager.lobbyObj.setDisplayName(Chat.cBlue + Chat.Bold + "Cosmic" + Chat.cGold + Chat.Bold + " Universe");
                        setState(GameState.LOBBY);
                        cancel();
                        return;
                    }
                    if (countdown <= 0) {
                        this.cancel();
                        startGame(forceStart);
                        isMovementFrozen = true;
                        int point = 0;
                        for (Player player : Bukkit.getOnlinePlayers()) {
                            player.teleport(spawnPoints.get(point));
                            point++;
                            if (point > spawnPoints.size()) point = 0;
                            player.setScoreboard(GameManager.gameScoreboard);
                        }
                        setState(GameState.ACTIVE);
                        postLobbyCountdown();
                        GameManager.lobbyObj.setDisplayName(Chat.cBlue + Chat.Bold + "Cosmic" + Chat.cGold + Chat.Bold + " Universe");
                    } else {
                        GameManager.lobbyObj.setDisplayName("§lStarting in §a§l" + countdown + " second" + (countdown == 1 ? "" : "s"));
                        if (countdown % 5 == 0 || (countdown <= 5)) {
                            playSound(Sound.NOTE_PLING, 1);
                            sendMessage("You will be teleported in " + countdown + " second" + (countdown == 1 ? "" : "s") + "!");
                        }
                    }
                    countdown--;
                } else {
                    GameManager.lobbyObj.setDisplayName(Chat.cBlue + Chat.Bold + "Cosmic" + Chat.cGold + Chat.Bold + " Universe");
                    cancel();
                }
            }
        }.runTaskTimer(Core.getPlugin(), 0, 20);
    }

    @Override
    public void startGame(Boolean forceStart) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(Core.getPlugin(), () -> playSound(Sound.LEVEL_UP, 1), 2);
        for (Player player : players) player.setGameMode(GameMode.SURVIVAL);
        for (Player player : spectators) setSpectator(player);
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
                    sendMessage("The game has started.");
                    isMovementFrozen = false;
                    gameStart();
                } else {
                    if (isState(GameState.ENDING)) this.cancel();
                    else {
                        startIn -= 1;
                        sendMessage("The game will begin in " + startIn + " second" + (startIn == 1 ? "" : "s") + ".");
                        playSound(Sound.CLICK, 1);
                    }
                }
            }
        }.runTaskTimer(Core.getPlugin(), 0, 20);
    }

    @Override
    public void endGame(Boolean forced, Player winner) {
        setState(GameState.ENDING);
        preEndGame();
        isMovementFrozen = false;
        PacketPlayOutTitle times = new PacketPlayOutTitle(20, 160, 20);
        PacketPlayOutTitle title;
        PacketPlayOutTitle subtitle = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, IChatBaseComponent.ChatSerializer.a("{text:\"won the game!\",color:yellow,bold:true}"));
        if (winner != null || !forced) {
            title = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, IChatBaseComponent.ChatSerializer.a("{text:\"" + (winner == null ? "No one" : winner.getName()) + "\",color:yellow,bold:true}"));
            for (Player p : Bukkit.getOnlinePlayers()) {
                ((CraftPlayer) p).getHandle().playerConnection.sendPacket(times);
                ((CraftPlayer) p).getHandle().playerConnection.sendPacket(title);
                ((CraftPlayer) p).getHandle().playerConnection.sendPacket(subtitle);
                GameManager.gameScoreboard.resetScores(p.getName());
            }
        }
        new BukkitRunnable() {
            private int time = 40;
//            private EnderDragon dragon;
            @Override
            public void run() {
                if (time <= 0) {
                    this.cancel();
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        for (PotionEffect effect : player.getActivePotionEffects())
                            player.removePotionEffect(effect.getType());
                        for (Player target : Bukkit.getOnlinePlayers()) //TODO MAKE COMPATIBLE WITH VANISH WHEN VANISH IS MADE
                            player.showPlayer(target);
                        player.teleport(GameManager.lobbyPoint);
                        player.setScoreboard(GameManager.lobbyScoreboard);
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
                    RollbackHandler.getRollbackHandler().rollback(gameName, mapName);
                    setState(GameState.LOBBY);
                    postEndGame();
                    GameManager.goToNextGame();
                } else if (winner != null && time >= 5) {
//                    if (!winner.getName().equals("snowbud56")) {
                        Firework firework = (Firework) winner.getWorld().spawnEntity(winner.getLocation(), EntityType.FIREWORK);
                        FireworkMeta fm = firework.getFireworkMeta();
                        fm.setPower(0);
                        fm.addEffect(FireworkEffect.builder().withColor(Color.YELLOW).with(FireworkEffect.Type.BALL_LARGE).build());
                        firework.setFireworkMeta(fm);
//                    } else {
//                        if (time == 40) {
//                            dragon = (EnderDragon) winner.getWorld().spawnEntity(winner.getLocation(), EntityType.ENDER_DRAGON);
//                            dragon.setCustomNameVisible(false);
//                            dragon.setNoDamageTicks(Integer.MAX_VALUE);
//                            dragon.setPassenger(winner);
//                        }
//                        if (time % 2 == 0) {
//                            winner.launchProjectile(Fireball.class, winner.getVelocity());
//                        }
//                    }
                }
                time = time - 1;
            }
        }.runTaskTimer(Core.getPlugin(), 0, 5);
    }

    @Override
    public boolean canMove(Player p) {
        if (spectators.contains(p)) return true;
        return !isMovementFrozen;
    }

    @Override
    public boolean isState(GameState state) {
        return gameState.equals(state);
    }

    @Override
    public void setState(GameState gameState) {
        GameStateChangeEvent event = new GameStateChangeEvent(this.gameState, gameState);
        Bukkit.getServer().getPluginManager().callEvent(event);
        LogManager.logConsole("GameBase Status: " + gameState.name() + " (previous: " + this.gameState + ")");
        this.gameState = gameState;
    }

    @Override
    public List<Location> getSpawnPoints() {
        return spawnPoints;
    }

    @Override
    public String getMapName() {
        return mapName;
    }

    @Override
    public void sendMessage(String message) {
        Bukkit.broadcastMessage(Chat.prefix + message);
    }

    private void playSound(Sound sound, float pitch) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.playSound(player.getLocation(), sound, 1, pitch);
        }
    }
}
