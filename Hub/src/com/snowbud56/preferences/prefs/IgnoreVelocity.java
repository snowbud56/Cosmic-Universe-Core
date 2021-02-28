package com.snowbud56.preferences.prefs;

/*
* Created by snowbud56 on February 05, 2018
* Do not change or use this code without permission
*/

import com.snowbud56.preferences.PlayerPref;
import com.snowbud56.preferences.PrefManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerVelocityEvent;

public class IgnoreVelocity implements Listener {

    @EventHandler
    public void onVelocity(PlayerVelocityEvent e) {
        Player p = e.getPlayer();
        PlayerPref prefs = PrefManager.getPlayerPrefs(p.getUniqueId());
        if (prefs.getNovelocity()) e.setCancelled(true);
    }
}
