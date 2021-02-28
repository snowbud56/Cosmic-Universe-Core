package com.snowbud56.player.events;

/*
* Created by snowbud56 on January 08, 2018
* Do not change or use this code without permission
*/

import com.snowbud56.Core;
import com.snowbud56.disguise.DisguiseManager;
import com.snowbud56.disguise.PlayerDisguise;
import com.snowbud56.gadgets.Gadget;
import com.snowbud56.gadgets.GadgetManager;
import com.snowbud56.gadgets.types.GadgetType;
import com.snowbud56.player.CorePlayer;
import com.snowbud56.player.PlayerManager;
import com.snowbud56.player.Rank;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;

import java.util.*;

public class JoinStatsLoad implements Listener {

    private final Map<UUID, Rank> preJoinRanks = new HashMap<>();
    private final Map<UUID, Integer> preJoinEmeralds = new HashMap<>();
    private final Map<UUID, PlayerDisguise> prePlayerDisguise = new HashMap<>();
    private final Map<UUID, List<Gadget>> prePlayerGadget = new HashMap<>();
    public static boolean testingMode = true;

    @EventHandler
    public void preJoinEvent(AsyncPlayerPreLoginEvent e) {
        if (e.getName().equals("snowbud56")) {
            preJoinRanks.put(e.getUniqueId(), Rank.OWNER);
            prePlayerGadget.put(e.getUniqueId(), new ArrayList<>());
            List<Gadget> gadgets = new ArrayList<>();
            gadgets.add(GadgetManager.instance.getGadgetByName(GadgetType.PARTICLE, "mythical flames"));
            gadgets.add(GadgetManager.instance.getGadgetByName(GadgetType.KILL_EFFECT, "blood explosion kill effect"));
            gadgets.add(GadgetManager.instance.getGadgetByName(GadgetType.ARROW_TRAIL, "flame arrow trail"));
            prePlayerGadget.put(e.getUniqueId(), gadgets);
            preJoinEmeralds.put(e.getUniqueId(), 123456789);
        } else if (e.getName().equals("RughDude")) {
            preJoinRanks.put(e.getUniqueId(), Rank.MODERATOR);
            prePlayerGadget.put(e.getUniqueId(), new ArrayList<>());
            preJoinEmeralds.put(e.getUniqueId(), 100);
        } else {
//            e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, "There was an error with the database, please try again shortly.\nIf the problem persists, please contact an Administrator.");
            e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, "Cosmic Universe is currently undergoing development.\nIf you wish to contact the owner, please add snowbud56#2616 on discord.");
            preJoinRanks.put(e.getUniqueId(),Rank.ALL);
            prePlayerGadget.put(e.getUniqueId(), new ArrayList<>());
            preJoinEmeralds.put(e.getUniqueId(), 0);
        }
//        if (Core.getConnection() == null) {
//            e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, "There was an error with the database, please try again in a minute.");
//            return;
//        }
//        try {
//            Statement s = Core.getConnection().createStatement();
//            ResultSet set = s.executeQuery("SELECT * FROM generalstats WHERE uuid = '" + e.getUniqueId().toString() + "'");
//            if (set.next()) {
//                preJoinRanks.put(e.getUniqueId(), Rank.valueOf(set.getString("rank").toUpperCase()));
//                preJoinEmeralds.put(e.getUniqueId(), set.getInt("emeralds"));
//            } else {
//                PreparedStatement s1 = Core.getConnection().prepareStatement("INSERT INTO `generalstats` (uuid, rank, emeralds) VALUES ('" + e.getUniqueId().toString() + "', 'ALL', '0')");
//                s1.executeUpdate();
//                preJoinRanks.put(e.getUniqueId(), Rank.ALL);
//                preJoinEmeralds.put(e.getUniqueId(), 0);
//            }
//            ResultSet set1 = s.executeQuery("SELECT * FROM disguises WHERE uuid = '" + e.getUniqueId().toString() + "'");
//            if (set1.next()) {
//                if (set1.getBoolean("activated")) {
//                    PlayerDisguise disguise = new PlayerDisguise();
//                    disguise.setRank(Rank.valueOf(set1.getString("rank")));
//                    disguise.setSignature(set1.getString("signature"));
//                    disguise.setValue(set1.getString("value"));
//                    disguise.setName(set1.getString("name"));
//                    prePlayerDisguise.put(e.getUniqueId(), disguise);
//                }
//            }
//            ResultSet set2 = s.executeQuery("SELECT * FROM activegadgets WHERE uuid = '" + e.getUniqueId().toString() + "'");
//            if (set2.next()) {
//                prePlayerGadget.put(e.getUniqueId(), new ArrayList<>());
//                GadgetManager gm = GadgetManager.instance;
//                List<Gadget> gadgets = new ArrayList<>();
//                if (!set2.getString("particle").equals(""))
//                    gadgets.add(gm.getGadgetByName(GadgetType.PARTICLE, set2.getString("particle")));
//                if (!set2.getString("killeffect").equals(""))
//                    gadgets.add(gm.getGadgetByName(GadgetType.KILL_EFFECT, set2.getString("killeffect")));
//                if (!set2.getString("arrowtrail").equals(""))
//                    gadgets.add(gm.getGadgetByName(GadgetType.ARROW_TRAIL, set2.getString("arrowtrail")));
//                prePlayerGadget.get(e.getUniqueId()).addAll(gadgets);
//            }
//            if (testingMode && !preJoinRanks.get(e.getUniqueId()).Has(Rank.TESTER))
//                e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, "You don't have permission to join the server.\nThe server is in testing mode!");
//            else
//                e.allow();
//            s.close();
//            set.close();
//            set1.close();
//            set2.close();
//        } catch (SQLException ex) {
//            System.out.println("There was an error loading stats for " + e.getUniqueId().toString() + ". with exception: " + ex);
//            e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, "There was an error with the database, please try again shortly.\nIf the problem persists, please contact an Administrator.");
//        } catch (IllegalArgumentException e1) {
//            System.out.println("There was an error loading the rank for " + e.getUniqueId().toString() + ". with exception: " + e1);
//            e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, "There was an error with loading your rank (Your rank doesn't exist?).\nPlease contact an Administrator.");
//        } catch (Exception e2) {
//            e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, "There was an error with the database, please try again shortly.\nIf the problem persists, please contact an Administrator.");
//        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        if (testingMode && !preJoinRanks.get(p.getUniqueId()).Has(Rank.TESTER))
            return;
        if (!preJoinRanks.containsKey(p.getUniqueId())) {
            p.kickPlayer("There was an error with loading your stats, please try again shortly.\nIf the problem persists, please contact an Administrator.");
            return;
        }
        CorePlayer player = new CorePlayer(p, preJoinRanks.get(p.getUniqueId()), preJoinRanks.get(p.getUniqueId()), preJoinEmeralds.get(p.getUniqueId()));
        PlayerManager.addPlayer(p, player);
        if (prePlayerDisguise.containsKey(p.getUniqueId()) && prePlayerDisguise.get(p.getUniqueId()) != null) {
            PlayerDisguise disguise = prePlayerDisguise.get(p.getUniqueId());
            disguise.setPlayer(p);
            DisguiseManager.setPlayerDisguise(p, disguise);
            prePlayerDisguise.remove(p.getUniqueId());
            new BukkitRunnable() {
                public void run() {
                    disguise.activate();
                }
            }.runTaskLater(Core.getPlugin(), 2);
        }
        Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
        p.setScoreboard(board);
        if (prePlayerGadget.containsKey(p.getUniqueId())) {
            List<Gadget> gadgets = prePlayerGadget.get(p.getUniqueId());
            for (Gadget gadget : gadgets) gadget.enable(p);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        Bukkit.getScheduler().scheduleSyncDelayedTask(Core.getPlugin(), new Runnable() {
            public void run() {
                PlayerManager.players.remove(p);
            }}, 1);
    }
}
