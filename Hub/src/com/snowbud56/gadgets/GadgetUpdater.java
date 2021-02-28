package com.snowbud56.gadgets;

/*
* Created by snowbud56 on February 12, 2018
* Do not change or use this code without permission
*/

import com.snowbud56.Core;
import com.snowbud56.Hub;
import com.snowbud56.gadgets.types.GadgetType;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class GadgetUpdater {

    public GadgetUpdater() {
        File file = new File(Hub.getInstance().getDataFolder() + "/playerdata/");
        if (!file.exists()) file.mkdir();
    }

    public static void saveGadgets(Player p) {
        new BukkitRunnable() {
            private GadgetManager gm = GadgetManager.instance;
            public void run() {
                try {
                    Statement s = Core.getConnection().createStatement();
                    ResultSet set = s.executeQuery("SELECT * FROM activegadgets WHERE uuid = '" + p.getUniqueId().toString() + "'");
                    if (set.next()) {
                        s.execute("UPDATE activegadgets SET particle = '" + (gm.getActiveGadget(p, GadgetType.PARTICLE) != null ? gm.getActiveGadget(p, GadgetType.PARTICLE).getName() : "") + "', killeffect = '" + (gm.getActiveGadget(p, GadgetType.KILL_EFFECT) != null ? gm.getActiveGadget(p, GadgetType.KILL_EFFECT).getName() : "") + "', arrowtrail = '" + (gm.getActiveGadget(p, GadgetType.ARROW_TRAIL) != null ? gm.getActiveGadget(p, GadgetType.ARROW_TRAIL).getName() : "") + "' WHERE uuid = '" + p.getUniqueId().toString() + "'");
                    } else {
                        s.execute("INSERT INTO activegadgets (uuid, particle, killeffect, arrowtrail) VALUES ('" + p.getUniqueId().toString() + "', '" + (gm.getActiveGadget(p, GadgetType.PARTICLE) != null ? gm.getActiveGadget(p, GadgetType.PARTICLE).getName() : "") + "', '" + (gm.getActiveGadget(p, GadgetType.KILL_EFFECT) != null ? gm.getActiveGadget(p, GadgetType.KILL_EFFECT).getName() : "") + "', '" + (gm.getActiveGadget(p, GadgetType.ARROW_TRAIL) != null ? gm.getActiveGadget(p, GadgetType.ARROW_TRAIL).getName() : "") + "')");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }.runTaskAsynchronously(Hub.getInstance());
    }

    public static List<String> getPlayerGadgets(Player p) {
        try {
            File file = new File(Hub.getInstance().getDataFolder() + "/playerdata/" + p.getUniqueId().toString() + ".yml");
            if (!file.exists()) file.createNewFile();
            YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
            return config.getStringList("gadgets");
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    public static void setPlayerGadgets(Player p, List<String> gadgets) {
        try {
            File file = new File(Hub.getInstance().getDataFolder() + "/playerdata/" + p.getUniqueId().toString() + ".yml");
            if (!file.exists()) file.createNewFile();
            YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
            config.set("gadgets", gadgets);
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
