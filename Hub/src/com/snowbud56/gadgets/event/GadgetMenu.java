package com.snowbud56.gadgets.event;

/*
* Created by snowbud56 on February 01, 2018
* Do not change or use this code without permission
*/

import com.snowbud56.Hub;
import com.snowbud56.gadgets.Gadget;
import com.snowbud56.gadgets.GadgetUpdater;
import com.snowbud56.gadgets.GadgetManager;
import com.snowbud56.gadgets.types.GadgetType;
import com.snowbud56.util.Chat;
import com.snowbud56.util.ItemFactory;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class GadgetMenu implements Listener {

    private Random random = new Random();

    @EventHandler
    public void onRightClick(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (p.getItemInHand() != null && p.getItemInHand().getType() == Material.CHEST) {
            Action a = e.getAction();
            if (!(a == Action.RIGHT_CLICK_BLOCK || a == Action.RIGHT_CLICK_AIR || a == Action.LEFT_CLICK_AIR || a == Action.LEFT_CLICK_BLOCK)) return;
            e.setCancelled(true);
            openMenu(p, null);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if (e.getInventory().getTitle().equals("Gadget Menu")) {
            e.setCancelled(true);
            ItemStack clicked = e.getCurrentItem();
            if (clicked == null || clicked.getType() == Material.AIR || clicked.getType() == Material.STAINED_GLASS_PANE) return;
            if (clicked.getItemMeta().getDisplayName().equals(Chat.cGreen + "Disable all Gadgets")) {
                GadgetManager.instance.disablePlayerGadgets(p);
                return;
            }
            GadgetType type = GadgetType.getBySlot(e.getSlot());
            if (type == null) return;
            openMenu(p, type);
        } else {
            for (GadgetType type : GadgetType.values()) {
                if (e.getInventory().getTitle().toLowerCase().equals(type.getName().toLowerCase())) {
                    e.setCancelled(true);
                    ItemStack clicked = e.getCurrentItem();
                    if (clicked == null || clicked.getType() == Material.AIR || clicked.getType() == Material.STAINED_GLASS_PANE) return;
                    if (clicked.getItemMeta().getDisplayName().equals(Chat.cGreen + "Back to main menu")) openMenu(p, null);
                    else {
                        for (GadgetType t : GadgetType.values()) {
                            if (e.getInventory().getTitle().toLowerCase().equals(t.getName().toLowerCase())) {
                                Gadget gadget = GadgetManager.instance.getGadgetByName(t, ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()));
                                if (gadget == null) return;
                                if (GadgetUpdater.getPlayerGadgets(p).contains(gadget.getName())) {
                                    if (GadgetManager.instance.getActiveGadget(p, t) == gadget) gadget.disable(p);
                                    else gadget.enable(p);
                                }
                                openMenu(p, t);
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void inventoryClose(InventoryCloseEvent e) {
        new BukkitRunnable() {
            public void run() {
                if (e.getPlayer().getOpenInventory() != null) {
                    if (e.getInventory().getTitle().equals("Gadget Menu")) {
                        GadgetUpdater.saveGadgets((Player) e.getPlayer());
                    } else {
                        for (GadgetType type : GadgetType.values()) {
                            if (e.getInventory().getTitle().toLowerCase().equals(type.getName().toLowerCase())) {
                                GadgetUpdater.saveGadgets((Player) e.getPlayer());
                            }
                        }
                    }
                }
            }
        }.runTaskLater(Hub.getInstance(), 5L);
    }

    private void openMenu(Player p, GadgetType type) {
        Inventory inv;
        if (type == null) {
            inv = Bukkit.createInventory(null, 27, "Gadget Menu");
            for (int i = 0; i < inv.getSize(); i++) {
                ItemStack glass = new ItemFactory(Material.STAINED_GLASS_PANE).data(random.nextInt(15)).displayName("").buildItem();
                inv.setItem(i, glass);
            }
            for (GadgetType types : GadgetType.values()) {
                ItemStack item = new ItemFactory(types.getItem()).displayName(Chat.cGreen + types.getName()).buildItem();
                inv.setItem(types.getSlot(), item);
            }
            inv.setItem(18, new ItemFactory(Material.BARRIER).displayName(Chat.cGreen + "Disable all Gadgets").buildItem());
        } else {
            List<Gadget> gadgets = GadgetManager.instance.getGadgetList(type);
            inv = Bukkit.createInventory(null, 54, type.getName());
            for (int i = 0; i < 54; i++) {
                if (i < 9 || i % 9 == 0 || (i % 9) - 8 == 0 || i >= 45) {
                    ItemStack glass = new ItemFactory(Material.STAINED_GLASS_PANE).data(1).displayName("").buildItem();
                    inv.setItem(i, glass);
                }
            }
            int slot = 9;
            for (Gadget gadget : gadgets) {
                slot++;
                if (slot % 9 == 0 || (slot % 9) - 8 == 0) slot += 2;
                if (slot >= 45 || slot < 9) continue;
                List<String> lore = new ArrayList<>();
                lore.addAll(Arrays.asList(gadget.getLore()));
                if (GadgetUpdater.getPlayerGadgets(p).contains(gadget.getName()))
                    lore.add((GadgetManager.instance.getActiveGadget(p, type) == gadget ? Chat.cGreen + "Enabled" : Chat.cRed + "Disabled"));
                else
                    lore.add(Chat.cRed + "Not unlocked");
                ItemStack gadgetitem = new ItemFactory(gadget.getMaterial()).displayName(Chat.cGreen + gadget.getName()).lore(lore).buildItem();
                if (!GadgetUpdater.getPlayerGadgets(p).contains(gadget.getName()))
                    gadgetitem = new ItemFactory(Material.INK_SACK).data(8).displayName(Chat.cGreen + gadget.getName()).lore(lore).buildItem();
                inv.setItem(slot, gadgetitem);
            }
            inv.setItem(45, new ItemFactory(Material.ARROW).displayName(Chat.cGreen + "Back to main menu").buildItem());
        }
        p.openInventory(inv);
    }
}
