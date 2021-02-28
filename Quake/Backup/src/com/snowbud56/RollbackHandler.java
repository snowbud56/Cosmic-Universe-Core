package com.snowbud56;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;
import java.io.*;

public class RollbackHandler {

    private static RollbackHandler rollbackHandler = new RollbackHandler();
    public static RollbackHandler getRollbackHandler() {
        return rollbackHandler;
    }

    public void rollback() {
        World world = Bukkit.getWorld("main_active");
        if (world.getPlayers().size() > 0) {
            for (Player player : world.getPlayers()) player.teleport(Bukkit.getWorld("world").getSpawnLocation());
        }
        QuakeMinigame.getInstance().getServer().unloadWorld(world, false);
        String rootDirectory = QuakeMinigame.getInstance().getServer().getWorldContainer().getAbsolutePath();
        File srcFolder = new File(rootDirectory + "/main");
        File destFolder = new File(rootDirectory + "/main_active");
        delete(destFolder);
        try {
            copyFolder(srcFolder, destFolder);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Bukkit.createWorld(new WorldCreator("main_active"));
    }

    public void delete(File delete){
        if (delete.isDirectory()) {
            String[] files = delete.list();
            if (files != null) {
                for (String file : files) {
                    File toDelete = new File(file);
                    delete(toDelete);
                }
            }
        } else {
            delete.delete();
        }
    }

    private void copyFolder(File src, File dest) throws IOException {
        if (src.isDirectory()) {
            if (!dest.exists()) {
                dest.mkdir();
            }
            String files[] = src.list();
            if (files != null) {
                for (String file : files) {
                    File srcFile = new File(src, file);
                    File destFile = new File(dest, file);
                    copyFolder(srcFile, destFile);
                }
            }
        } else {
            InputStream in = new FileInputStream(src);
            OutputStream out = new FileOutputStream(dest);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = in.read(buffer)) > 0){
                out.write(buffer, 0, length);
            }
            in.close();
            out.close();
        }
    }
}