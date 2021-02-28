package com.snowbud56.gadgets;

/*
* Created by snowbud56 on February 03, 2018
* Do not change or use this code without permission
*/

import com.snowbud56.gadgets.arrowtrails.FlameTrail;
import com.snowbud56.gadgets.arrowtrails.SmokeyTrail;
import com.snowbud56.gadgets.event.GadgetActivateEvent;
import com.snowbud56.gadgets.killeffects.BloodExplosion;
import com.snowbud56.gadgets.killeffects.LightningKillEffect;
import com.snowbud56.gadgets.killeffects.SmokeyKill;
import com.snowbud56.gadgets.particles.*;
import com.snowbud56.gadgets.types.GadgetType;
import com.snowbud56.util.TimeUtil;
import com.snowbud56.util.managers.LogManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GadgetManager implements Listener {

    public static GadgetManager instance;
    private static Map<GadgetType, List<Gadget>> gadgets = new HashMap<>();
    private static Map<Player, Map<GadgetType, Gadget>> activeGadgets = new HashMap<>();
    public static Boolean gadgetsEnabled = true;
    private HashMap<Player, Long> _lastMove = new HashMap<>();

    public GadgetManager() {
        instance = this;
        loadGadgets();
    }

    private void loadGadgets() {
        long startTime = System.currentTimeMillis();
        LogManager.logConsole("Loading Gadgets...");
        //Particles
        addGadget(new MythicalFlames());
        addGadget(new BloodHelix());
        addGadget(new GreenRing());
        addGadget(new SmokeFumes());
        addGadget(new ExplosionParticle());
        addGadget(new FlameSpiral());

        //Kill Effects
        addGadget(new BloodExplosion());
        addGadget(new LightningKillEffect());
        addGadget(new SmokeyKill());

        //Arrow Trails
        addGadget(new FlameTrail());
        addGadget(new SmokeyTrail());
        LogManager.logConsole("Gadgets loaded in " + (TimeUtil.convertmstoTime(System.currentTimeMillis() - startTime)) + "!");
    }

    public void addGadget(Gadget gadget) {
        if (!gadgets.containsKey(gadget.getGadgetType()))
            gadgets.put(gadget.getGadgetType(), new ArrayList<>());
        gadgets.get(gadget.getType()).add(gadget);
    }

    public List<Gadget> getGadgetList(GadgetType type) {
        return gadgets.get(type);
    }

    public void setActiveGadget(Player p, Gadget gadget) {
        activeGadgets.get(p).put(gadget.getType(), gadget);
    }

    static void removeActive(Player p, Gadget gadget) {
        activeGadgets.get(p).remove(gadget.getType());
    }

    public Gadget getActiveGadget(Player p, GadgetType type) {
        if (!activeGadgets.containsKey(p)) {
            activeGadgets.put(p, new HashMap<>());
            return null;
        }
        return activeGadgets.get(p).get(type);
    }

    public void disableAllGadgets() {
        for (Map.Entry<GadgetType, List<Gadget>> gadget : gadgets.entrySet()) {
            for (Gadget g : gadget.getValue()) {
                g.disableForAll();
            }
        }
    }

    public void disablePlayerGadgets(Player p) {
        for (Map.Entry<GadgetType, List<Gadget>> gadget : gadgets.entrySet()) {
            for (Gadget g : gadget.getValue()) {
                g.disable(p);
            }
        }
    }

    public Gadget getGadgetByName(String name) {
        for (Map.Entry<GadgetType, List<Gadget>> gadgetEntry : gadgets.entrySet()) {
            for (Gadget gadget : gadgetEntry.getValue()) {
                if (name.toLowerCase().equals(gadget.getName().toLowerCase())) return gadget;
            }
        }
        return null;
    }

    public Gadget getGadgetByName(GadgetType type, String name) {
        for (Gadget gadget : gadgets.get(type)) {
            if (name.toLowerCase().equals(gadget.getName().toLowerCase())) return gadget;
        }
        return null;
    }

    @EventHandler
    public void setMoving(PlayerMoveEvent event) {
        if (event.getFrom().distance(event.getTo()) <= 0.1) return;
        _lastMove.put(event.getPlayer(), System.currentTimeMillis());
    }

    public boolean isMoving(Player player) {
        if (!_lastMove.containsKey(player)) return false;
        return !(System.currentTimeMillis() - _lastMove.get(player) > 1000);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        if (activeGadgets.containsKey(e.getPlayer())) return;
        activeGadgets.put(e.getPlayer(), new HashMap<>());
        _lastMove.put(e.getPlayer(), System.currentTimeMillis() - 500);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        if (!activeGadgets.containsKey(e.getPlayer())) return;
        List<Gadget> gadgets = new ArrayList<>();
        for (Map.Entry<GadgetType, Gadget> enabled : activeGadgets.get(e.getPlayer()).entrySet()) {
            gadgets.add(enabled.getValue());
        }
        for (Gadget g : gadgets)
            g.disable(e.getPlayer());
        activeGadgets.remove(e.getPlayer());
    }

    @EventHandler
    public void onGadgetActivate(GadgetActivateEvent e) {
        if (!gadgetsEnabled) e.setCancelled(true);
    }
}
