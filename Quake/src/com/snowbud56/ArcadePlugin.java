package com.snowbud56;

import com.snowbud56.game.commands.ExplosiveCommand;
import com.snowbud56.game.commands.SetPointsCommand;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;

public class ArcadePlugin extends JavaPlugin {
    private static Core core;

    @Override
    public void onEnable() {
        core = new Core(this);
        Core.setIsGameServer(true);
        core.addCommands(new ExplosiveCommand(), new SetPointsCommand());
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.removePotionEffect(PotionEffectType.SPEED);
            p.getInventory().clear();
            p.setMaxHealth(20);
            p.setHealth(p.getMaxHealth());
            p.setFoodLevel(25);
            p.setGameMode(GameMode.SURVIVAL);
        }
    }
}
