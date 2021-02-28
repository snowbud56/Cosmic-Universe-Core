package com.snowbud56.util.managers;

/*
 * Created by snowbud56 on February 13, 2019
 * Do not change or use this code without permission
 */

import com.snowbud56.player.Rank;
import com.snowbud56.util.Chat;
import com.snowbud56.util.TimeUtil;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LogManager {

    public static void logConsole(String message) {
        System.out.println(/*"[" + TimeUtil.getDate() + "] " + */message);
        logFile(message);
    }

    public static void logError(String message) {
        System.out.println("[ERROR]");
        System.out.println("[ERROR]" + "[" + TimeUtil.getDate() + "] " + message);
        System.out.println("[ERROR]");
        logFile("[ERROR]");
        logFile("[ERROR]" + "[" + TimeUtil.getDate() + "] " + message);
        logFile("[ERROR]");
    }

    public static void logWarning(String message) {
        System.out.println("[WARNING]" + "[" + TimeUtil.getDate() + "] " + message);
        logFile("[WARNING]" + "[" + TimeUtil.getDate() + "] " + message);
    }

    public static void messageAdmins(String message) {
        Chat.messageRank(Chat.AdminPrefix + message, Rank.ADMIN);
        logConsole("[Admin Message] " + ChatColor.stripColor(message));
    }

    public static void logFile(String message) {
        SimpleDateFormat fileDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String FILENAME = Bukkit.getServer().getWorldContainer().getAbsolutePath() + "/coreLogs/" + fileDateFormat.format(new Date()) + ".log";
        BufferedWriter bw = null;
        FileWriter fw = null;
        try {
            fw = new FileWriter(FILENAME, true);
            bw = new BufferedWriter(fw);
            SimpleDateFormat messageDateFormat = new SimpleDateFormat("HH:mm:ss");
            bw.write( "[" + messageDateFormat.format(new Date()) + "] " + message + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bw != null)
                    bw.close();
                if (fw != null)
                    fw.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
//        Logger logger = Logger.getLogger("CoreLogger");
//        logger.setUseParentHandlers(false);
//        FileHandler fh;
//        try {
//            SimpleDateFormat fileDateFormat = new SimpleDateFormat("yyyy-MM-dd");
//            File file = new File(Bukkit.getServer().getWorldContainer().getAbsolutePath(), "/coreLogs/" + fileDateFormat.format(new Date()) + ".log");
//            if (!file.exists()) file.createNewFile();
//            fh = new FileHandler(Bukkit.getServer().getWorldContainer().getAbsolutePath() + "/coreLogs/" + fileDateFormat.format(new Date()) + ".log", true);
//            SimpleFormatter formatter = new SimpleFormatter();
//            fh.setFormatter(formatter);
//            logger.addHandler(fh);
//            SimpleDateFormat messageDateFormat = new SimpleDateFormat("HH:mm:ss");
//            logger.log(Level.INFO, "[" + messageDateFormat.format(new Date()) + "] " + message);
//        } catch (SecurityException | IOException e) {
//            e.printStackTrace();
//        }
    }
}
