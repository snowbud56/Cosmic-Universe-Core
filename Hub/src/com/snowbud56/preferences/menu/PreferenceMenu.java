package com.snowbud56.preferences.menu;

/*
* Created by snowbud56 on January 09, 2018
* Do not change or use this code without permission
*/

import com.snowbud56.player.PlayerManager;
import com.snowbud56.player.Rank;
import com.snowbud56.preferences.PlayerPref;
import com.snowbud56.preferences.PrefManager;
import com.snowbud56.preferences.events.ForcefieldEnableEvent;
import com.snowbud56.preferences.prefs.PlayerVisibility;
import com.snowbud56.util.Chat;
import com.snowbud56.util.ItemFactory;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class PreferenceMenu implements Listener {

//    private static Random random = new Random();

    private static String[] mainGUIDesign = new String[] {
            "o", "o", "o", "o", "o", "o", "o", "o", "o",
            "o", "o", "p", "o", "c", "o", "v", "o", "o",
            "o", "o", "o", "o", "o", "o", "o", "o", "e",
//            "o", "o", "o", "o", "o", "o", "o", "o", "o",
//            "o", "o", "o", "o", "o", "o", "o", "o", "o",
//            "o", "o", "o", "o", "o", "o", "o", "o", "o",
    };
    private static String[] exclusiveGUIDesign = new String[] {
            "o", "o", "o", "o", "o", "o", "o", "o", "o",
            "o", "o", "f", "o", "l", "o", "i", "o", "o",
            "b", "o", "o", "o", "o", "o", "o", "o", "o",
//            "o", "o", "o", "o", "o", "o", "o", "o", "o",
//            "o", "o", "o", "o", "o", "o", "o", "o", "o",
//            "o", "o", "o", "o", "o", "o", "o", "o", "o"
    };

    public static void openMainGUI(Player p) {
        Inventory inv = Bukkit.createInventory(null, mainGUIDesign.length, "Preferences");
        PlayerPref preferences = PrefManager.getPlayerPrefs(p.getUniqueId());
//        Enchantment enabled = Enchantment.DIG_SPEED;
        for (int i = 0; i < mainGUIDesign.length; i++) {
            switch (mainGUIDesign[i]) {
                case "o":
                    ItemStack glass = new ItemFactory(Material.STAINED_GLASS_PANE).data(11).displayName("").buildItem();
                    inv.setItem(i, glass);
                    continue;
                case "p":
                    ItemStack pms = new ItemFactory(Material.PAPER).displayName(Chat.mHead + "Private Messages").lore(Arrays.asList(
                            (preferences.getPms() ? Chat.cGreen + "Enabled" : Chat.cRed + "Disabled"),
                            "",
                            Chat.mBody + "Enable/disable private messages",
                            Chat.mBody + "PLEASE NOTE: Staff members bypass this!",
                            "",
                            Chat.mBody + (preferences.getPms() ? Chat.cRed + "Click to disable." : Chat.cGreen + "Click to enable."))).buildItem();
//                    if (preferences.getPms()) pms.addUnsafeEnchantment(enabled, 1);
                    inv.setItem(i, pms);
                    continue;
                case "c":
                    ItemStack chat = new ItemFactory(Material.BOOK_AND_QUILL).displayName(Chat.mHead + "Chat Visibility").lore(Arrays.asList(
                            (preferences.getChat() ? Chat.cGreen + "Enabled" : Chat.cRed + "Disabled"),
                            "",
                            Chat.mBody + "Enable/disable public chat visibility",
                            Chat.mBody + "PLEASE NOTE: Staff members bypass this!",
                            "",
                            Chat.mBody + (preferences.getChat() ? Chat.cRed + "Click to disable." : Chat.cGreen + "Click to enable."))).buildItem();
//                    if (preferences.getChat()) chat.addUnsafeEnchantment(enabled, 1);
                    inv.setItem(i, chat);
                    continue;
                case "j":
                    ItemStack join = new ItemFactory(Material.FIREWORK).displayName(Chat.mHead + "Join Message").lore(Arrays.asList(
                            (preferences.getJoin() ? Chat.cGreen + "Enabled" : Chat.cRed + "Disabled"),
                            "",
                            Chat.mBody + "Announce a message whenever you join",
                            Chat.mBody + "and leave the server",
                            "",
                            Chat.mBody + (preferences.getJoin() ? Chat.cRed + "Click to disable." : Chat.cGreen + "Click to enable."))).buildItem();
//                    if (preferences.getJoin()) join.addUnsafeEnchantment(enabled, 1);
                    inv.setItem(i, join);
                    continue;
                case "v":
                    ItemStack playerVisibility = new ItemFactory(Material.BARRIER).displayName(Chat.mHead + "Player Visibility").lore(Arrays.asList(
                            (preferences.getPlayerVis() ? Chat.cGreen + "Enabled" : Chat.cRed + "Disabled"),
                            "",
                            Chat.mBody + "Enable/disable hub player visibility",
                            Chat.mBody + "PLEASE NOTE: Staff members bypass this!",
                            "",
                            Chat.mBody + (preferences.getPlayerVis() ? Chat.cRed + "Click to disable." : Chat.cGreen + "Click to enable."))).buildItem();
//                    if (preferences.getPlayerVis()) playerVisibility.addUnsafeEnchantment(enabled, 1);
                    inv.setItem(i, playerVisibility);
                    continue;
                case "e":
                    if (PlayerManager.getPlayer(p).getRank().Has(p, Rank.ADMIN, new Rank[] {Rank.SNOW, Rank.YOUTUBE}, false)) {
                        ItemStack exclusive = new ItemFactory(Material.NETHER_STAR).displayName(Chat.mHead + "Exclusive Menu").buildItem();
                        inv.setItem(i, exclusive);
                    } else {
                        ItemStack glass1 = new ItemFactory(Material.STAINED_GLASS_PANE).data(11).displayName("").buildItem();
                        inv.setItem(i, glass1);
                    }
            }
        }
        p.openInventory(inv);
    }

    private void openExclusiveGUI(Player p) {
        Inventory inv = Bukkit.createInventory(null, exclusiveGUIDesign.length, "Exclusive Preferences");
        PlayerPref preferences = PrefManager.getPlayerPrefs(p.getUniqueId());
//        Enchantment enabled = Enchantment.DIG_SPEED;
        for (int i = 0; i < exclusiveGUIDesign.length; i++) {
            switch (exclusiveGUIDesign[i]) {
                case "o":
                    ItemStack glass = new ItemFactory(Material.STAINED_GLASS_PANE).data(14).displayName("").buildItem();
                    inv.setItem(i, glass);
                    continue;
                case "b":
                    ItemStack back = new ItemFactory(Material.ARROW).displayName(Chat.mHead + "Back to Preferences").buildItem();
                    inv.setItem(i, back);
                    continue;
                case "f":
                    ItemStack ff = new ItemFactory(Material.SLIME_BALL).displayName(Chat.mHead + "Hub Forcefield").lore(Arrays.asList(
                            (preferences.getForcefield() ? Chat.cGreen + "Enabled" : Chat.cRed + "Disabled"),
                            "",
                            Chat.mBody + "Want some alone time in your own",
                            Chat.mBody + "personal bubble? Here you go!",
                            "",
                            Chat.mBody + (preferences.getForcefield() ? Chat.cRed + "Click to disable." : Chat.cGreen + "Click to enable."))).buildItem();
//                    if (preferences.getForcefield()) ff.addUnsafeEnchantment(enabled, 1);
                    inv.setItem(i, ff);
                    continue;
                case "i":
                    ItemStack noVelocity = new ItemFactory(Material.BEDROCK).displayName(Chat.mHead + "Ignore Velocity").lore(Arrays.asList(
                            (preferences.getNovelocity() ? Chat.cGreen + "Enabled" : Chat.cRed + "Disabled"),
                            "",
                            Chat.mBody + "Tired of those pesky gadgets?",
                            Chat.mBody + "Click here to not be bothered by them!",
                            "",
                            Chat.mBody + (preferences.getNovelocity() ? Chat.cRed + "Click to disable." : Chat.cGreen + "Click to enable."))).buildItem();
//                    if (preferences.getNovelocity()) noVelocity.addUnsafeEnchantment(enabled, 1);
                    inv.setItem(i, noVelocity);
                    continue;
                case "l":
                    ItemStack fly = new ItemFactory(Material.EYE_OF_ENDER).displayName(Chat.mHead + "Hub Fly").lore(Arrays.asList(
                            (preferences.getFly() ? Chat.cGreen + "Enabled" : Chat.cRed + "Disabled"),
                            "",
                            Chat.mBody + "What's this? Floating in mid-air?",
                            Chat.mBody + "Try it yourself by clicking me!",
                            "",
                            Chat.mBody + (preferences.getFly() ? Chat.cRed + "Click to disable." : Chat.cGreen + "Click to enable."))).buildItem();
//                    if (preferences.getFly()) fly.addUnsafeEnchantment(enabled, 1);
                    inv.setItem(i, fly);
                    continue;
                case "v":
                    ItemStack vanish = new ItemFactory(Material.BARRIER).displayName(Chat.mHead + "Hub Vanish").lore(Arrays.asList(
                            (preferences.getVanish() ? Chat.cGreen + "Enabled" : Chat.cRed + "Disabled"),
                            "",
                            Chat.mBody + "Don't want to be seen?",
                            Chat.mBody + "Try it yourself by clicking me!",
                            "",
                            Chat.mBody + (preferences.getVanish() ? Chat.cRed + "Click to disable." : Chat.cGreen + "Click to enable."))).buildItem();
//                    if (preferences.getVanish()) vanish.addUnsafeEnchantment(enabled, 1);
                    inv.setItem(i, vanish);
                    continue;
                default:
                    break;
            }
        }
        p.openInventory(inv);
    }

    @EventHandler
    public void onClickEvent(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if (e.getInventory().getName().equals("Preferences") || e.getInventory().getName().equals("Exclusive Preferences")) {
            e.setCancelled(true);
            ItemStack clicked = e.getCurrentItem();
            if (clicked == null || clicked.getType() == Material.AIR || clicked.getType() == Material.STAINED_GLASS_PANE) return;
            String preference = ChatColor.stripColor(clicked.getItemMeta().getDisplayName());
            PlayerPref prefs = PrefManager.getPlayerPrefs(p.getUniqueId());
            switch (preference) {
                case "Join Message":
                    prefs.setJoin(!prefs.getJoin());
                    openMainGUI(p);
                    break;
                case "Chat Visibility":
                    prefs.setChat(!prefs.getChat());
                    openMainGUI(p);
                    break;
                case "Private Messages":
                    prefs.setPms(!prefs.getPms());
                    openMainGUI(p);
                    break;
                case "Player Visibility":
                    prefs.setPlayerVis(!prefs.getPlayerVis());
                    if (prefs.getPlayerVis()) PlayerVisibility.enablePlayerVis(p);
                    else PlayerVisibility.disablePlayerVis(p);
                    openMainGUI(p);
                    break;
                case "Exclusive Menu":
                    openExclusiveGUI(p);
                    break;
                case "Back to Preferences":
                    openMainGUI(p);
                    break;
                case "Hub Forcefield":
                    prefs.setForcefield(!prefs.getForcefield());
                    if (prefs.getForcefield()) {
                        ForcefieldEnableEvent forcefieldEnableEvent = new ForcefieldEnableEvent(p);
                        Bukkit.getPluginManager().callEvent(forcefieldEnableEvent);
                    }
                    openExclusiveGUI(p);
                    break;
                case "Ignore Velocity":
                    prefs.setNovelocity(!prefs.getNovelocity());
                    openExclusiveGUI(p);
                    break;
                case "Hub Fly":
                    prefs.setFly(!prefs.getFly());
                    openExclusiveGUI(p);
                    p.setAllowFlight(prefs.getFly());
                    break;
                case "Hub Vanish":
                    prefs.setVanish(!prefs.getVanish());
                    openExclusiveGUI(p);
                    break;
            }
            PrefManager.setPlayerPrefs(p.getUniqueId(), prefs);
        }
    }
}
