package com.snowbud56.player.event;

/*
* Created by snowbud56 on February 08, 2018
* Do not change or use this code without permission
*/

import com.snowbud56.Hub;
import com.snowbud56.gadgets.GadgetUpdater;
import com.snowbud56.player.CorePlayer;
import com.snowbud56.player.PlayerManager;
import com.snowbud56.player.Rank;
import com.snowbud56.preferences.PrefManager;
import com.snowbud56.preferences.prefs.PlayerVisibility;
import com.snowbud56.util.Chat;
import com.snowbud56.util.ItemFactory;
import com.snowbud56.util.ScoreboardUtil;
import com.snowbud56.util.TabUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.*;

import static com.snowbud56.preferences.PrefManager.savePlayerPrefs;
import static com.snowbud56.preferences.PrefManager.updatePlayerbyUUID;

public class LobbyJoin implements Listener {

    private static Map<Integer, ItemStack> spawnItems;
    static Location spawnPoint = new Location(Bukkit.getWorld("world_main"), 0.5, 37, 0.5);

    public LobbyJoin() {
        spawnItems = new HashMap<>();
        ItemStack serverSelector = new ItemFactory(Material.COMPASS).amount(1).displayName(Chat.cGreen + "Game Selector" + Chat.cGray + " [Right-Click]").lore(Arrays.asList(Chat.cGray + "Right-Click to open the", Chat.cGray + "Server Selection Menu")).unbreakable(false).buildItem();
        spawnItems.put(0, serverSelector);
        ItemStack gadgets = new ItemFactory(Material.CHEST).amount(1).displayName(Chat.cGreen + "Cosmetics" + Chat.cGray + " [Right-Click]").lore(Arrays.asList(Chat.cGray + "Right-Click to open the", Chat.cGray + "Cosmetics Menu")).unbreakable(false).buildItem();
        spawnItems.put(4, gadgets);
        ItemStack preferences = new ItemFactory(Material.REDSTONE_COMPARATOR).amount(1).displayName(Chat.cGreen + "Preferences" + Chat.cGray + " [Right-Click]").lore(Arrays.asList(Chat.cGray + "Right-Click to open the", Chat.cGray + "Preference Menu")).unbreakable(false).buildItem();
        spawnItems.put(8, preferences);
    }

    @EventHandler
    public void onPlayerPreJoinEvent(AsyncPlayerPreLoginEvent e) {
        updatePlayerbyUUID(e.getUniqueId());
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        e.setJoinMessage(null);
        Player p = e.getPlayer();
        p.setGameMode(GameMode.SURVIVAL);
        p.getInventory().clear();
        for (Map.Entry<Integer, ItemStack> item : spawnItems.entrySet()) {
            p.getInventory().setItem(item.getKey(), item.getValue());
        }
        p.teleport(spawnPoint);
        TabUtils.updatePlayerPrefixRank(p);
        new BukkitRunnable() {
            public void run() {
                CorePlayer player = PlayerManager.getPlayer(p);
                Scoreboard board = p.getScoreboard();
                Objective obj = board.registerNewObjective("lobby", "dummy");
                obj.setDisplayName(Chat.cBlue + Chat.Bold + "Cosmic" + Chat.cGold + Chat.Bold + " Universe");
                obj.setDisplaySlot(DisplaySlot.SIDEBAR);
                ScoreboardUtil.setupScores(obj, new String[] {
                        "Rank: ",
                        "Emeralds: ",
                        "" + Chat.cDBlue,
                        "Lobby: §a01"
                });
                Team rank = board.registerNewTeam("sb-rank");
                rank.setSuffix("§r" + player.getRank().color + player.getRank().name);
                rank.addEntry("Rank: ");
                Team emeralds = board.registerNewTeam("sb-emeralds");
                emeralds.setSuffix("§a" + player.getEmeralds());
                emeralds.addEntry("Emeralds: ");
            }
        }.runTaskLater(Hub.getInstance(), 5L);
        if (PlayerManager.getPlayer(p).getRank().Has(p, Rank.ADMIN, new Rank[] {Rank.TESTER}, false)) {
            if (!GadgetUpdater.getPlayerGadgets(p).contains("Flame Spiral")) {
                List<String> gadgets = GadgetUpdater.getPlayerGadgets(p);
                if (gadgets == null) gadgets = new ArrayList<>();
                gadgets.add("Flame Spiral");
                GadgetUpdater.setPlayerGadgets(p, gadgets);
            }
        }
    }

    @EventHandler
    public void PrefManagerOnQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        e.setQuitMessage(null);
        if (PlayerManager.getPlayer(p).isTestingRank())
            return;
        savePlayerPrefs(p.getUniqueId());
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) {
        e.getPlayer().teleport(spawnPoint);
        if (PrefManager.getPlayerPrefs(e.getPlayer().getUniqueId()).getPlayerVis()) PlayerVisibility.disablePlayerVis(e.getPlayer());
        else PlayerVisibility.enablePlayerVis(e.getPlayer());
    }
}
