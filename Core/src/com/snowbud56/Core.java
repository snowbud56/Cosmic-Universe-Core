package com.snowbud56;

/*
* Created by snowbud56 on January 08, 2018
* Do not change or use this code without permission
*/

import com.snowbud56.chat.command.Silence;
import com.snowbud56.chat.command.SlowChat;
import com.snowbud56.chat.event.ChatEvent;
import com.snowbud56.command.Command;
import com.snowbud56.command.CommandManager;
import com.snowbud56.disguise.DisguiseManager;
import com.snowbud56.disguise.commands.ToggleNick;
import com.snowbud56.gadgets.GadgetManager;
import com.snowbud56.gadgets.TestGadgetCommand;
import com.snowbud56.gadgets.arrowtrails.FlameTrail;
import com.snowbud56.gadgets.killeffects.BloodExplosion;
import com.snowbud56.gadgets.killeffects.LightningKillEffect;
import com.snowbud56.gadgets.killeffects.SmokeyKill;
import com.snowbud56.gadgets.particles.*;
import com.snowbud56.game.GameManager;
import com.snowbud56.game.GameMaps;
import com.snowbud56.game.GameType;
import com.snowbud56.game.commands.GameCommand;
import com.snowbud56.game.events.*;
import com.snowbud56.npc.command.MobCommand;
import com.snowbud56.player.command.*;
import com.snowbud56.player.events.JoinStatsLoad;
import com.snowbud56.player.events.PlayerMoveEvent;
import com.snowbud56.player.events.ServerListPing;
import com.snowbud56.server.command.TestingCommand;
import com.snowbud56.staff.FindCommand;
import com.snowbud56.staff.StaffChat;
import com.snowbud56.staff.admin.*;
import com.snowbud56.updater.Updater;
import com.snowbud56.util.Chat;
import com.snowbud56.util.ServerUtil;
import com.snowbud56.util.TimeUtil;
import com.snowbud56.util.managers.LogManager;
import com.snowbud56.warden.WAC;
import com.snowbud56.warden.commands.WACCommand;
import com.snowbud56.warden.events.MoveListener;
import com.snowbud56.warden.events.PlayerListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.sql.Connection;

public class Core {

    private static JavaPlugin plugin;
    private static Connection connection = null;
    private static boolean validServer;
    private static boolean isGameServer = false;
//    public static EnabledEnchant enabledEnchant = new EnabledEnchant();

    public Core(JavaPlugin plugin) {
        long startTime = System.currentTimeMillis();
        LogManager.logConsole("Starting Core...");
        Core.plugin = plugin;
        establishConnection(true);
        File file = new File(plugin.getServer().getWorldContainer().getAbsolutePath(), "/validation.dat");
        if (!file.exists()) {
            validServer = false;
//            try {
//                PreparedStatement s = connection.prepareStatement("INSERT INTO `invalidservers` (`IP`,`Time`) VALUES ('" + InetAddress.getLocalHost().getHostAddress() + "',NOW())");
//                s.executeUpdate();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
            System.out.println("ATTENTION: THIS IS NOT A VALID SERVER.");
            System.out.println("PIRACY IS A CRIME, AND VIOLATORS WILL BE PROSECUTED.");
            System.out.println("YOUR IP HAS BEEN LOGGED.");
            System.out.println("Enjoy your lawsuit <3");
            Bukkit.shutdown();
            return;
        }
        validServer = true;
        if (!plugin.getDataFolder().exists()) plugin.getDataFolder().mkdir();
        plugin.saveDefaultConfig();
        new Updater(plugin);
        WAC.updatePlayers();
        registerListeners(new CommandManager(), new JoinStatsLoad(), new ChatEvent(),
                new ServerListPing(), new BloodExplosion(),
                new MythicalFlames(), new BloodHelix(), new GreenRing(),
                new SmokeFumes(), new LightningKillEffect(), new GadgetManager(),
                new ExplosionParticle(), new FlameSpiral(), new SmokeyKill(),
                new FlameTrail(), new MoveListener(), new PlayerListener(),
                new WAC(), new ServerUtil(), new PlayerMoveEvent(),
                new GameJoinEvent(), new GameMoveEvent(), new GameDeathEvent(),
                new GameBlockEvent(), new GameDisableWeather(), new GameQuitEvent(),
                new GameLobbyDamage(), new PowerToolCommand(), new DisguiseManager());
        addCommands(new setRankCommand(), new SlowChat(),
                new Silence(), new AdminCommands(), new TrollCommands(),
                new StaffChat(), new MobCommand(), new ReloadCommand(),
                new TestingCommand(), new EmeraldCommand(), new TempRank(),
                new GiveCommand(), new GamemodeCommand(), new WACCommand(),
                new ToggleNick(), new SendCommand(), new AnnounceCommand(),
                new ServerCommand(), new DisplayRankCommand(), new GameCommand(),
                new FindCommand(), new PowerToolCommand(), new TestGadgetCommand());
        plugin.getServer().getMessenger().registerIncomingPluginChannel(Core.plugin, "BungeeCord", new ServerUtil());
        plugin.getServer().getMessenger().registerIncomingPluginChannel(Core.plugin, "BungeeCord", new AnnounceCommand());
        plugin.getServer().getMessenger().registerIncomingPluginChannel(Core.plugin, "BungeeCord", new FindCommand());
        plugin.getServer().getMessenger().registerOutgoingPluginChannel(Core.plugin, "BungeeCord");
//        loadEnchantments();
        LogManager.logConsole("Core successfully started in " + (TimeUtil.convertmstoTime(System.currentTimeMillis() - startTime)) + "!");
    }

    public void registerListeners(Listener... listeners) {
        PluginManager pm = plugin.getServer().getPluginManager();
        for (Listener l : listeners) {
            pm.registerEvents(l, plugin);
            LogManager.logConsole("Loaded Listener: " + l.getClass().getSimpleName());
        }
    }

    public static void setIsGameServer(boolean isGameServer) {
        Core.isGameServer = isGameServer;
        if (isGameServer) {
            GameMaps.loadGameMaps();
            GameManager.setNextMap(GameMaps.maps.get(GameType.QUAKE)[0]);
            GameManager.setActiveGame(GameType.QUAKE);
        }
    }

    public static boolean isGameServer() {
        return isGameServer;
    }

    public void addCommands(Command... cmds) {
        CommandManager cm = CommandManager.instance;
        for (Command cmd : cmds) {
            cm.addCommand(cmd);
            for (String command : cmd.getAliases()) LogManager.logConsole("Loaded Command: /" + command);
        }
    }

    public void disable() {
        if (!validServer) return;
//        try {
//            Field byIdField = Enchantment.class.getDeclaredField("byId");
//            Field byNameField = Enchantment.class.getDeclaredField("byName");
//            byIdField.setAccessible(true);
//            byNameField.setAccessible(true);
//            HashMap<Integer, Enchantment> byId = (HashMap<Integer, Enchantment>) byIdField.get(null);
//            HashMap<Integer, Enchantment> byName = (HashMap<Integer, Enchantment>) byNameField.get(null);
//            if (byId.containsKey(enabledEnchant.getId())) byId.remove(enabledEnchant.getId());
//            if (byName.containsKey(enabledEnchant.getName())) byName.remove(enabledEnchant.getName());
//        } catch (Exception ignored) { }
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.kickPlayer(Chat.cDRed + "Server restart. Please join back in a few minutes.");
        }
    }

//    private static void loadEnchantments() {
//        System.out.println("[Core] Loading Enchantment(s)");
//        try {
//            try {
//                Field f = Enchantment.class.getDeclaredField("acceptingNew");
//                f.setAccessible(true);
//                f.set(null, true);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            try {
//                Enchantment.registerEnchantment(enabledEnchant);
//                System.out.println("[Core] Loaded Enchantment: Enabled");
//            } catch (IllegalArgumentException e) {
//                e.printStackTrace();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    public static boolean isValidServer() {
        return validServer;
    }

    public static void establishConnection(boolean initial) {
        System.out.println("[Core] Trying to connect to the database...");
//        if (!initial) Bukkit.broadcastMessage(Chat.cAqua + Chat.Bold + "Attempting to reconnect to the database, expect lag...");
//        try {
//            Class.forName("com.mysql.jdbc.Driver");
////            connection = DriverManager.getConnection("jdbc:mysql://174.54.60.134:3306/cosmicuniverse?user=snowbud56&password=snowbud561&autoReconnect=true");
//            connection = DriverManager.getConnection("jdbc:postgresql://ec2-50-16-198-4.compute-1.amazonaws.com:5432/d3k7mslp3mh8ki?user=emlvjtiwovxxur&password=67f58ccbcf80d25051ba2310d3e1982937a3447fb36f0d7486b73ac8d7c53d86&autoReconnect=true");
////            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/cosmicuniverse?user=snowbud56&password=snowbud561&useSSL=false&debug=true");
            System.out.println("[Core] Successfully connected to the database.");
            if (!initial) Bukkit.broadcastMessage(Chat.cGreen + Chat.Bold + "Successfully connected to the database");
//        } catch (Exception e) {
//            connection = null;
//            System.out.println("[Core] Failed to connect to the database. Exception : " + e);
//            if (!initial) Bukkit.broadcastMessage(Chat.cDRed + Chat.Bold + "Failed to connect to the database");
//            return;
//        }
//        try {
//            Statement s = connection.createStatement();
//            s.execute("CREATE TABLE IF NOT EXISTS `cosmicuniverse`.`generalstats` (`uuid` TEXT NULL, `rank` TEXT NULL, `emeralds` TEXT NULL)");
//            s.execute("CREATE TABLE IF NOT EXISTS `cosmicuniverse`.`disguises` (`uuid` TEXT NULL, `activated` TEXT NULL, `rank` TEXT NULL, `name` TEXT NULL, `signature` TEXT NULL, `value` TEXT NULL)");
//            s.execute("CREATE TABLE IF NOT EXISTS `cosmicuniverse`.`invalidservers` (`id` INT NOT NULL AUTO_INCREMENT, `IP` MEDIUMTEXT, `Time` DATE, PRIMARY KEY (`id`))");
//            s.execute("CREATE TABLE IF NOT EXISTS `cosmicuniverse`.`activegadgets` (`uuid` TEXT NULL, `particle` TEXT NULL, `killeffect` TEXT NULL, `arrowtrail` TEXT NULL)");
//            s.execute("CREATE TABLE IF NOT EXISTS `cosmicuniverse`.`pitstats` (`uuid` TEXT NULL, `level` TEXT NULL, `xpneeded` TEXT NULL)");
//        } catch (SQLException ex1) {
//            ex1.printStackTrace();
//        }
    }

    public static Connection getConnection() {
        return connection;
    }

    public static JavaPlugin getPlugin() {
        return plugin;
    }
}