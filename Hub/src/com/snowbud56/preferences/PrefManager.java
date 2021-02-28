package com.snowbud56.preferences;

/*
* Created by snowbud56 on January 09, 2018
* Do not change or use this code without permission
*/

import com.snowbud56.Hub;
import com.snowbud56.preferences.menu.PreferenceMenu;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PrefManager implements Listener {

//    private static YamlConfiguration config;
//    private static File file;
    private static Map<UUID, PlayerPref> preferences = new HashMap<>();

    public PrefManager() {
    }

    public static void updatePlayerbyUUID(UUID uuid) {
        String u = uuid.toString();
        File file = new File(Hub.getInstance().getDataFolder() + "/playerdata/" + u + ".yml");
        YamlConfiguration config;
        try {
            if (!file.exists()) file.createNewFile();
            config = YamlConfiguration.loadConfiguration(file);
            config.save(file);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        if (!config.contains("prefs.playerVis")) config.set("prefs.playerVis", true);
        if (!config.contains("prefs.chatVis")) config.set("prefs.chatVis", true);
        if (!config.contains("prefs.PMs")) config.set("prefs.PMs", true);
        if (!config.contains("prefs.noVelocity")) config.set("prefs.noVelocity", false);
        if (!config.contains("prefs.gamemode")) config.set("prefs.gamemode", false);
        if (!config.contains("prefs.joinMsg")) config.set("prefs.joinMsg", true);
        if (!config.contains("prefs.fly")) config.set("prefs.fly", false);
        if (!config.contains("prefs.forcefield")) config.set("prefs.forcefield", false);
        PlayerPref prefs = new PlayerPref(
                config.getBoolean("prefs.playerVis"), config.getBoolean("prefs.chatVis"),
                config.getBoolean("prefs.PMs"), config.getBoolean("prefs.noVelocity"),
                config.getBoolean("prefs.gamemode"), config.getBoolean("prefs.joinMsg"),
                config.getBoolean("prefs.fly"), config.getBoolean("prefs.forcefield"));
        preferences.put(uuid, prefs);
        savePrefs(u);
    }

    public static PlayerPref getPlayerPrefs(UUID uuid) {
        return preferences.get(uuid);
    }

    public static void savePrefs(String uuid) {
        File file = new File(Hub.getInstance().getDataFolder() + "/playerdata/" + uuid + ".yml");
        YamlConfiguration config;
        try {
            if (!file.exists()) file.createNewFile();
            config = YamlConfiguration.loadConfiguration(file);
            config.save(file);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        try {
            config.save(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void savePlayerPrefs(UUID player) {
        String u = player.toString();
        File file = new File(Hub.getInstance().getDataFolder() + "/playerdata/" + u + ".yml");
        YamlConfiguration config;
        try {
            if (!file.exists()) file.createNewFile();
            config = YamlConfiguration.loadConfiguration(file);
            config.save(file);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        try {
            PlayerPref prefs = getPlayerPrefs(player);
            config.set("prefs.forcefield", prefs.getForcefield());
            config.set("prefs.fly", prefs.getFly());
            config.set("prefs.joinMsg", prefs.getJoin());
            config.set("prefs.gamemode", prefs.getGamemode());
            config.set("prefs.noVelocity", prefs.getNovelocity());
            config.set("prefs.PMs", prefs.getPms());
            config.set("prefs.chatVis", prefs.getChat());
            config.save(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setPlayerPrefs(UUID uuid, PlayerPref prefs) {
        preferences.put(uuid, prefs);
    }

//    @EventHandler
//    public void onPlayerPreJoinEvent(AsyncPlayerPreLoginEvent e) {
//        System.out.println("[Hub] Received Join");
//        updatePlayerbyUUID(e.getUniqueId());
//    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        Action a = e.getAction();
        if (!(a == Action.RIGHT_CLICK_BLOCK || a == Action.RIGHT_CLICK_AIR || a == Action.LEFT_CLICK_AIR || a == Action.LEFT_CLICK_BLOCK)) return;
        if (p.getItemInHand() != null && p.getItemInHand().getType() == Material.REDSTONE_COMPARATOR) {
            e.setCancelled(true);
            PreferenceMenu.openMainGUI(p);
        }
    }
//
//    @EventHandler
//    public void PrefManagerOnQuit(PlayerQuitEvent e) {
//        Player p = e.getPlayer();
////        CorePlayer player = PlayerManager.getPlayer(p);
//        savePlayerPrefs(p.getUniqueId());
//        e.setQuitMessage(null);
////        if (prefs.getJoin()) {
////            if (DisguiseManager.getPlayerDisguise(p) != null && DisguiseManager.getPlayerDisguise(p).isActivated()) e.setQuitMessage(Chat.cDGray + "Quit> " + p.getDisplayName());
////            else e.setQuitMessage(Chat.cDGray + "Quit> " + Chat.cGray + p.getDisplayName());
////        } else e.setQuitMessage(null);
//    }
}
