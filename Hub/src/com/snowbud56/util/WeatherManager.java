package com.snowbud56.util;

/*
* Created by snowbud56 on March 18, 2018
* Do not change or use this code without permission
*/

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.WeatherChangeEvent;

public class WeatherManager implements Listener {

    @EventHandler
    public void onWeatherChange(WeatherChangeEvent e) {
        if (e.getWorld().isThundering())
            e.getWorld().setThundering(false);
    }
}
