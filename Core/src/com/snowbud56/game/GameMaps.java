package com.snowbud56.game;

/*
 * Created by snowbud56 on April 15, 2019
 * Do not change or use this code without permission
 */

import com.snowbud56.Core;
import com.snowbud56.util.TimeUtil;
import com.snowbud56.util.managers.LogManager;

import java.io.File;
import java.util.HashMap;
import java.util.Random;

public class GameMaps {

    public static HashMap<GameType, String[]> maps = new HashMap<>();

    public static void loadGameMaps() {
        long startTime = System.currentTimeMillis();
        LogManager.logConsole("Loading game maps...");
        for (GameType type : GameType.values()) {
            File f = new File(Core.getPlugin().getServer().getWorldContainer().getAbsoluteFile() + "/maps/" + type.getGameName());
            maps.put(type, f.list());
        }
        LogManager.logConsole("Maps loaded in " + (TimeUtil.convertmstoTime(System.currentTimeMillis() - startTime)) + "!");
    }

    public static boolean gameHasMap(GameType type, String mapName) {
        for (String name : maps.get(type)) {
            if (name.toLowerCase().equals(mapName.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    public static String getNextMap(GameType type, String mapName) {
        for (int i = 0; i < maps.get(type).length - 1; i++) {
            if (maps.get(type)[i].toLowerCase().equals(mapName.toLowerCase())) {
                return maps.get(type)[i + 1];
            }
        }
        return maps.get(type)[0];
    }

    static String getRandomMap(GameType type) {
        Random r = new Random();
        String[] mapList = GameMaps.maps.get(type);
        return mapList[r.nextInt(mapList.length)];
    }
}
