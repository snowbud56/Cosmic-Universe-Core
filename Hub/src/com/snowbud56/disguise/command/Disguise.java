package com.snowbud56.disguise.command;

/*
* Created by snowbud56 on January 08, 2018
* Do not change or use this code without permission
*/

import com.snowbud56.Core;
import com.snowbud56.command.CommandBase;
import com.snowbud56.disguise.DisguiseManager;
import com.snowbud56.disguise.PlayerDisguise;
import com.snowbud56.disguise.events.PlayerNicknameEvent;
import com.snowbud56.player.PlayerManager;
import com.snowbud56.player.Rank;
import com.snowbud56.util.BookUtil;
import com.snowbud56.util.Chat;
import com.snowbud56.util.JSONManager;
import com.snowbud56.util.managers.LogManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Disguise extends CommandBase implements Listener {
    private static Map<Player, Boolean> settingName = new HashMap<>();
    private static Map<Player, Boolean> settingSkin = new HashMap<>();
    private List<String> bannedNicks;
    private static String BETA_JSON = "{text:\"\",extra:[{text:\"NICKNAME IN BETA\n\",color:red,bold:true},{text:\"\nNicknaming is currently in \",bold:false,color:black},{text:\"BETA\",bold:true,color:black},{text:\". This means that this feature may change at any point, and it may break whenever.\n\nIf at any point you encounter a bug, please PM snowbud56.\n\n\",color:black,bold:false},{text:\"Click to agree\",underlined:true,bold:true,hoverEvent:{action:show_text,value:[{text:\"Click to agree to the conditions\",bold:false,color:gray}]},clickEvent:{action:run_command,value:\"/dsetup agree\"}}]}";
    private static String RANK_JSON = "{text:\"\",extra:[{text:\"Let's get you set up with your nickname!\nFirst you'll need to choose which \",bold:false,color:black},{text:\"RANK\",color:black,bold:true},{text:\" you would like to be shown as when nicked.\",bold:false,color:black},{text:\"\n\n\"},{text:\"\n➤ \",color:black,bold:false},{text:\"ICE\",color:blue,bold:false,hoverEvent:{action:show_text,value:[{text:\"Click to set your nickname rank to \",color:gray,bold:false},{text:\"ICE\",color:blue,bold:false}]},clickEvent:{action:run_command,value:\"/dsetup rank ice\"}},{text:\"\n➤ \",color:black,bold:false},{text:\"FROST\",color:aqua,bold:false,hoverEvent:{action:show_text,value:[{text:\"Click to set your nickname rank to \",color:gray,bold:false},{text:\"FROST\",color:aqua,bold:false}]},clickEvent:{action:run_command,value:\"/dsetup rank frost\"}},{text:\"\n➤ \",color:black,bold:false},{text:\"Default\",color:gray,bold:false,hoverEvent:{action:show_text,value:[{text:\"Click to set your nickname rank to Default\",color:gray,bold:false}]},clickEvent:{action:run_command,value:\"/dsetup rank all\"}}]}";
    private static String SKIN_JSON = "{text:\"\",extra:[{text:\"Awesome! Now, which\",color:black,bold:false},{text:\"\nSKIN \",color:black,bold:true},{text:\"would you like to have while nicked?\",color:black,bold:false},{text:\"\n\n\"},{text:\"➤ \",color:black,bold:false},{text:\"My normal skin\",hoverEvent:{action:show_text,value:[{text:\"Click to have your normal skin while nicked.\",color:gray,bold:false}]},clickEvent:{action:run_command,value:\"/dsetup skin normal\"}},{text:\"\n\"},{text:\"➤ \",color:black,bold:false},{text:\"Steve/Alex skin\",color:black,bold:false,hoverEvent:{action:show_text,value:[{text:\"Click to have a Steve/Alex skin\",bold:false,color:gray}]},clickEvent:{action:run_command,value:\"/dsetup skin alex\"}},{text:\"\n\"},{text:\"➤ \",color:black,bold:false},{text:\"Custom skin\",color:black,bold:false,hoverEvent:{action:show_text,value:[{text:\"Click to set a custom skin for your nickname\",color:gray,bold:false}]},clickEvent:{action:run_command,value:\"/dsetup skin\"}}]}";
    private static String NAME_JSON = "{text:\"\",extra:[{text:\"Alright, now you'll need to choose the \",color:black,bold:false},{text:\"NAME \",color:black,bold:true},{text:\"to use!\",color:black,bold:false},{text:\"\n\n\"},{text:\"➤ \",color:black,bold:false},{text:\"Choose a name\",hoverEvent:{action:show_text,value:[{text:\"Click to set your own nickname.\",color:gray,bold:false}]},clickEvent:{action:run_command,value:\"/dsetup name\"}}]}";

    public Disguise() {
        super(Rank.MODERATOR, new Rank[] {Rank.SNOW, Rank.YOUTUBE}, "dsetup", "nick");
        bannedNicks = Core.getPlugin().getConfig().getStringList("banned-nicknames");
    }

    @Override
    public void execute(Player p, String[] args) {
        if (aliasUsed.equals("dsetup")) {
            switch (args[0].toLowerCase()) {
                case "agree":
                    BookUtil.openBook(p, new String[] {RANK_JSON});
                    break;
                case "rank":
                    try {
                        Rank trank = Rank.valueOf(args[1].toUpperCase());
                        DisguiseManager.getPlayerDisguise(p).setRank(trank);
                        p.sendMessage(Chat.prefix + Chat.mBody + "Your nick rank will be " + trank.color + trank.name);
                        BookUtil.openBook(p, new String[] {SKIN_JSON});
                    } catch (Exception ex) {
                        System.out.println("There was an error with " + p.getName() + "'s disguise. Exception: " + ex);
                        p.sendMessage(Chat.prefix + Chat.mBody + "There was an error with setting up your disguise, please contact an Administrator.");
                    }
                    break;
                case "skin":
                    if (args.length == 1) {
                        settingSkin.put(p, true);
                        p.sendMessage(Chat.prefix + Chat.mBody + "Please say the player skin you want to have.");
                    } else if (args[1].toLowerCase().equals("normal")) {
                        p.sendMessage(Chat.prefix + Chat.mBody + "You will have your normal skin.");
                        DisguiseManager.getPlayerDisguise(p).setSkin(p.getName());
                        BookUtil.openBook(p, new String[] {NAME_JSON});
                    } else if (args[1].toLowerCase().equals("alex")) {
                        p.sendMessage(Chat.prefix + Chat.mBody + "You will have a Steve/Alex skin.");
                        DisguiseManager.getPlayerDisguise(p).setSkin("Steve");
                        BookUtil.openBook(p, new String[] {NAME_JSON});
                    }
                    break;
                case "name":
                    settingName.put(p, true);
                    p.sendMessage(Chat.prefix + Chat.mBody + "Please say the nickname you want to have.");
                    break;
            }
        } else if (aliasUsed.equals("nick")) {
            if (args.length >= 1 && args[0].toLowerCase().equals("reset") && DisguiseManager.disguises.containsKey(p)) {
                if (DisguiseManager.disguises.get(p).isActivated())
                    DisguiseManager.disguises.get(p).deactivate();
                DisguiseManager.disguises.put(p, null);
                p.sendMessage(Chat.prefix + "Your nickname has been reset.");
                LogManager.messageAdmins(Chat.adminElement(p.getName()) + " has reset their nickname.");
                return;
            }
            if (!DisguiseManager.disguises.containsKey(p) || DisguiseManager.disguises.get(p) == null) {
                PlayerNicknameEvent event = new PlayerNicknameEvent(p);
                Bukkit.getServer().getPluginManager().callEvent(event);
                if (event.isCancelled()) {
                    p.sendMessage(Chat.prefix + "Nicknames have been disabled on this server.");
                    return;
                }
                PlayerDisguise disguise = new PlayerDisguise(p);
                BookUtil.openBook(p, new String[] {BETA_JSON});
                DisguiseManager.disguises.put(p, disguise);
            } else
                p.sendMessage(Chat.prefix + "Please reset your nickname before trying to make a new one using " + Chat.element("/nick reset") + ".");
        }
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        if (settingName.containsKey(p) && settingName.get(p)) {
            String name = e.getMessage().split(" ")[0];
            e.setCancelled(true);
            p.sendMessage(Chat.prefix + Chat.mBody + "Please wait while I see if " + Chat.cRed + name + Chat.mBody + " is avaliable.");
            if (Bukkit.getOfflinePlayer(name) != null && Bukkit.getOfflinePlayer(name).hasPlayedBefore()) {
                p.sendMessage(Chat.prefix + Chat.mBody + "Unable to use that nickname: Someone has joined with that name in the past!\n" + Chat.mBody + "Please choose another.");
                System.out.println("[Core] " + p.getName() + " tried to nick as " + name);
                System.out.println("[Core] Reject reason: User joined in the past");
                for (Player player : Bukkit.getOnlinePlayers()) if (PlayerManager.getPlayer(player).getRank().Has(p, Rank.ADMIN, false)) player.sendMessage(Chat.prefix + Chat.cRed + p.getName() + Chat.mBody + " tried to nick as " + Chat.cRed + name + "\n" + Chat.prefix + Chat.mBody + "Reject reason: User joined in the past");
                return;
            }
            for (String bannednick : bannedNicks) {
                if (bannednick.toLowerCase().equals(name.toLowerCase())) {
                    p.sendMessage(Chat.prefix + Chat.mBody + "Unable to use that nickname: That nickname has been banned by the owner!\n" + Chat.mBody + "Please choose another.");
                    System.out.println("[Core] " + p.getName() + " tried to nick as " + name);
                    System.out.println("[Core] Reject reason: Nickname banned by owner");
                    for (Player player : Bukkit.getOnlinePlayers()) if (PlayerManager.getPlayer(player).getRank().Has(p, Rank.ADMIN, false)) player.sendMessage(Chat.prefix + Chat.cRed + p.getName() + Chat.mBody + " tried to nick as " + Chat.cRed + name + "\n" + Chat.prefix + Chat.mBody + "Reject reason: Nickname banned by owner");
                    return;
                }
            }
            if (name.length() > 16) {
                p.sendMessage(Chat.prefix + "Unable to use that nickname: That nickname is longer than 16 characters\n" + Chat.mBody + "Please choose another.");
                System.out.println("[Core] " + p.getName() + " tried to nick as " + name);
                System.out.println("[Core] Reject reason: Nickname too long");
                for (Player player : Bukkit.getOnlinePlayers()) if (PlayerManager.getPlayer(player).getRank().Has(p, Rank.ADMIN, false)) player.sendMessage(Chat.prefix + Chat.cRed + p.getName() + Chat.mBody + " tried to nick as " + Chat.cRed + name + "\n" + Chat.prefix + Chat.mBody + "Reject reason: Nickname too long");
                return;
            }
            new BukkitRunnable() {
                public void run() {
                    try {
//                        PreparedStatement s = Core.getConnection().prepareStatement("SELECT * FROM disguises");
//                        ResultSet set = s.executeQuery();
//                        while (set.next()) {
//                            if (set.getBoolean("activated") && set.getString("name").toLowerCase().equals(name.toLowerCase())) {
//                                p.sendMessage(Chat.prefix + "Unable to use that nickname: Someone has taken that nickname!\n" + Chat.mBody + "Please choose another.");
//                                System.out.println("[Core] " + p.getName() + " tried to nick as " + name);
//                                System.out.println("[Core] Reject reason: Nickname taken");
//                                for (Player player : Bukkit.getOnlinePlayers()) if (PlayerManager.getPlayer(player).getRank().Has(p, Rank.ADMIN, false)) player.sendMessage(Chat.prefix + Chat.cRed + p.getName() + Chat.mBody + " tried to nick as " + Chat.cRed + name + "\n" + Chat.prefix + Chat.mBody + "Reject reason: Nickname taken");
//                                set.close();
//                                s.close();
//                                return;
//                            }
//                        }
                        settingName.put(p, false);
                        PlayerDisguise disguise = DisguiseManager.getPlayerDisguise(p);
                        disguise.setName(name);
                        p.sendMessage(Chat.prefix + "Success! Your nickname will be " + Chat.cRed + name);
                        LogManager.messageAdmins(Chat.adminElement(p.getName()) + " is now " + Chat.adminElement(name) + ".");
                        DisguiseManager.getPlayerDisguise(e.getPlayer()).activate();
                        Bukkit.getScheduler().runTaskLater(Core.getPlugin(), () -> BookUtil.openBook(p, new String[] {JSONManager.getJSON(ChatColor.translateAlternateColorCodes('&', "&0You have finished setting up your nickname!\n\nYou are now nicked as " + disguise.getRank().getTag(false, true, true) + "&7" + disguise.getName() + "&0. You will be nicked in lobbies and in game.\n\nTo go back to being your normal self, type &0&l/nick reset"))}), 6L);
//                        set.close();
//                        s.close();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }.runTaskAsynchronously(Core.getPlugin());
        }
        if (settingSkin.containsKey(p) && settingSkin.get(p)) {
            e.setCancelled(true);
            settingSkin.put(p, false);
            String skin = e.getMessage().split(" ")[0];
            DisguiseManager.getPlayerDisguise(p).setSkin(skin);
            p.sendMessage(Chat.prefix + "Your nickname skin will be " + Chat.cRed + skin);
            BookUtil.openBook(p, new String[] {NAME_JSON});
        }
    }
}
