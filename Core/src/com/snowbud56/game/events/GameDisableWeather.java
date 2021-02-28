package com.snowbud56.game.events;

/*
 * Created by snowbud56 on April 14, 2019
 * Do not change or use this code without permission
 */

import com.snowbud56.Core;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.WeatherChangeEvent;

public class GameDisableWeather implements Listener {

    @EventHandler
    public void onWeatherChange(WeatherChangeEvent e) {
        if (Core.isGameServer()) {
            if (e.getWorld().isThundering())
                e.getWorld().setThundering(false);
        }
    }
}
