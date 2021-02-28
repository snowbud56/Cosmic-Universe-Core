package com.snowbud56.util.game;

import com.snowbud56.Core;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class DataHandler {
    private File gameInfoFile;
    private FileConfiguration gameInfo;

    private static DataHandler instance = new DataHandler();

    public static DataHandler getInstance() {
        return instance;
    }

    public FileConfiguration getGameInfo() {
        this.gameInfoFile = new File(Core.getPlugin().getServer().getWorldContainer().getAbsolutePath() + "/main_active/gameInfo.yml");
        if (!this.gameInfoFile.exists()) {
            try {
//                this.gameInfoFile.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return YamlConfiguration.loadConfiguration(this.gameInfoFile);
    }

    /*public void saveGameInfo() {
        try {
            this.gameInfo.save(this.gameInfoFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/
}
