package com.snowbud56.util;

/*
* Created by snowbud56 on February 08, 2018
* Do not change or use this code without permission
*/

import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;

import java.io.File;

public class RollbackHandler {

    public static void rollbackWorld() {
        System.out.println("[Hub] Restoring world...");
        try {
            File srcFolder = new File("world_backup");
            File destFolder = new File("world_main");
            FileUtils.deleteDirectory(destFolder);
            FileUtils.copyDirectory(srcFolder, destFolder);
            World world = Bukkit.createWorld(new WorldCreator("world_main"));
            world.setGameRuleValue("keepInventory", "true");
            world.setGameRuleValue("doDaylightCycle", "false");
            world.setGameRuleValue("doFireTick", "false");
            world.setGameRuleValue("doMobSpawning", "false");
            world.setGameRuleValue("doMobLoot", "false");
            world.setStorm(false);
            world.setThundering(false);
            System.out.println("[Hub] Successfully restored the world.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
