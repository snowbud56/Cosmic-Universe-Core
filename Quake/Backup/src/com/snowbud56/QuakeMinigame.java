package com.snowbud56;

import com.snowbud56.game.constructors.Game;
import com.snowbud56.game.data.DataHandler;
import com.snowbud56.game.API.*;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;

public class QuakeMinigame extends JavaPlugin {
    private static QuakeMinigame instance;
    private static Game game;
    private static Core core;

    @Override
    public void onEnable() {
        instance = this;
        getConfig().options().copyDefaults(true);
        getConfig().options().copyHeader(true);
        saveDefaultConfig();
        if (DataHandler.getInstance().getGameInfo().getConfigurationSection("games") != null) {
            game = new Game("main");
            for (Player p : Bukkit.getOnlinePlayers()) game.joinGame(p);
        } else {
            getLogger().info("There are no games set up! Please create one.");
        }
        core = new Core(this);
        core.addCommands(new QuakeCommand());
        core.registerListeners(new PlayerMove(), new PlayerJoin(), new FoodLevelChange(), new PlayerDamage(), new PlayerDeath(), new PlayerQuit(), new QuakeGuns(), new BlockInteract());
        for (Player p : Bukkit.getOnlinePlayers()) {
            //p.teleport(Bukkit.getWorld("world").getSpawnLocation());
            p.removePotionEffect(PotionEffectType.SPEED);
            p.getInventory().clear();
            p.setMaxHealth(20);
            p.setHealth(p.getMaxHealth());
            p.setFoodLevel(25);
            p.setGameMode(GameMode.SURVIVAL);
        }
    }

    @Override
    public void onDisable() {
        instance = null;
        game = null;
    }

    public static QuakeMinigame getInstance() {
        return instance;
    }


    public static Game getGame() {
        return game;
    }
}
