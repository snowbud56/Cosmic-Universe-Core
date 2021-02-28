package com.snowbud56;

/*
* Created by snowbud56 on February 08, 2018
* Do not change or use this code without permission
*/

import com.snowbud56.crates.commands.CrateCommand;
import com.snowbud56.disguise.command.Disguise;
import com.snowbud56.gadgets.GadgetUpdater;
import com.snowbud56.gadgets.command.GadgetCommand;
import com.snowbud56.gadgets.event.GadgetMenu;
import com.snowbud56.gameselector.events.GameSelector;
import com.snowbud56.gameselector.menu.GameMenu;
import com.snowbud56.player.event.*;
import com.snowbud56.preferences.PrefManager;
import com.snowbud56.preferences.command.FlyCommand;
import com.snowbud56.preferences.command.PrefCommand;
import com.snowbud56.preferences.command.RadiusCommand;
import com.snowbud56.preferences.menu.PreferenceMenu;
import com.snowbud56.preferences.prefs.Forcefield;
import com.snowbud56.preferences.prefs.HubFly;
import com.snowbud56.preferences.prefs.IgnoreVelocity;
import com.snowbud56.staff.admin.AnnounceCommand;
import com.snowbud56.util.RollbackHandler;
import com.snowbud56.util.WeatherManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Hub extends JavaPlugin {

    private static Core core;
    private static Hub instance;

    @Override
    public void onEnable() {
        RollbackHandler.rollbackWorld();
        core = new Core(this);
        if (!Core.isValidServer()) return;
        instance = this;
        new GadgetUpdater();
        core.addCommands(new PrefCommand(), new FlyCommand(), new Disguise(),
                new GadgetCommand(), new CrateCommand(), new RadiusCommand());
        core.registerListeners(new PreferenceMenu(), new PrefManager(), new Forcefield(),
                new HubFly(), new IgnoreVelocity(),
                new LobbyJoin(), new Disguise(), new EntityDamage(),
                new FoodLevelChange(), new BlockBreak(), new GadgetMenu(),
                new InventoryManager(), new WeatherManager(), new GameSelector(),
                new PlayerInteraction());
        Bukkit.getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new GameMenu(null));
        Bukkit.getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new AnnounceCommand());
    }

    @Override
    public void onDisable() {
        if (!Core.isValidServer()) return;
        core.disable();
        core = null;
        instance = null;
    }

    public static Hub getInstance() {
        return instance;
    }

    public static Core getCore() {
        return core;
    }
}
