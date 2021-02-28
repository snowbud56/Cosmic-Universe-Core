package com.snowbud56.updater;

/*
* Created by snowbud56 on February 09, 2018
* Do not change or use this code without permission
*/

import org.bukkit.plugin.java.JavaPlugin;

public class Updater implements Runnable {
    private JavaPlugin _plugin;

    public Updater(JavaPlugin plugin) {
        _plugin = plugin;
        _plugin.getServer().getScheduler().scheduleSyncRepeatingTask(_plugin, this, 0L, 1L);
    }

    @Override
    public void run() {
        for (UpdateType updateType : UpdateType.values()) {
            if (updateType.elapsed()) {
                _plugin.getServer().getPluginManager().callEvent(new UpdateEvent(updateType));
            }
        }
    }
}
