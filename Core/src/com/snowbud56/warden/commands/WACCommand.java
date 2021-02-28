package com.snowbud56.warden.commands;

/*
* Created by snowbud56 on March 17, 2018
* Do not change or use this code without permission
*/

import com.snowbud56.command.CommandBase;
import com.snowbud56.player.PlayerManager;
import com.snowbud56.player.Rank;
import com.snowbud56.util.Chat;
import com.snowbud56.util.PlayersUtil;
import com.snowbud56.warden.WAC;
import com.snowbud56.warden.checks.CheckType;
import com.snowbud56.warden.util.WACPlayer;
import com.snowbud56.warden.util.WACSettings;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;

public class WACCommand extends CommandBase {

    public WACCommand() {
        super (Rank.HELPER, "WAC");
    }

    @Override
    public void execute(Player p, String[] args) {
        if (args.length == 0) {
            p.sendMessage(Chat.prefix + "/wac player <player>: view a players' WAC violations.");
            if (PlayerManager.getPlayer(p).getRank().Has(p, Rank.ADMIN, false)) p.sendMessage(Chat.prefix + "/wac ban: Toggle whether WAC will automatically ban hackers.");
            if (PlayerManager.getPlayer(p).getRank().Has(p, Rank.ADMIN, false)) p.sendMessage(Chat.prefix + "/wac toggle: Toggle WAC on and off.");
            if (PlayerManager.getPlayer(p).getRank().Has(p, Rank.ADMIN, false)) p.sendMessage(Chat.prefix + "/wac testban: Test the ban effect.");
        } else if (args[0].toLowerCase().equals("player")) {
            if (args[1] != null) {
                List<String> matches = PlayersUtil.findPlayer(p, args[1], false);
                if (matches.size() == 1) {
                    Player target = Bukkit.getPlayer(matches.get(0));
                    if (target == null) {
                        p.sendMessage(Chat.prefix + "Something went wrong! Please try again or contact an Administrator.");
                        return;
                    }
                    WACPlayer player = WAC.getPlayer(target);
                    HashMap<CheckType, Integer> v = new HashMap<>();
                    int total = 0;
                    for (CheckType type : CheckType.values()) {
                        int amount = player.getViolations(type);
                        v.put(type, amount);
                        total = total + amount;
                    }
                    p.sendMessage(Chat.prefix + matches.get(0) + "'s WAC stats:");
                    for (CheckType type : CheckType.values()) {
                        p.sendMessage(Chat.prefix + type.getName() + ": " + v.get(type));
                    }
                    p.sendMessage(Chat.prefix + "Total Violations: " + total);
                } else p.sendMessage(Chat.prefix + Chat.mBody + (matches.size() == 0 ? Chat.cRed + args[1] + Chat.mBody + " isn't online or doesn't exist!" : "Too many matches! Did you mean: " + Chat.cRed + matches.toString().replace("[", "").replace("]", "")));
            }
        } else if (args[0].toLowerCase().equals("ban")) {
            if (PlayerManager.getPlayer(p).getRank().Has(p, Rank.ADMIN, true)) {
                WACSettings.WACBanning = !WACSettings.WACBanning;
                p.sendMessage(Chat.prefix + Chat.element((WACSettings.WACBanning ? "Enabled" : "Disabled")) + " WAC auto-ban.");
            }
        } else if (args[0].toLowerCase().equals("toggle")) {
            if (PlayerManager.getPlayer(p).getRank().Has(p, Rank.ADMIN, true)) {
                WACSettings.WACEnabled = !WACSettings.WACEnabled;
                p.sendMessage(Chat.prefix + Chat.element((WACSettings.WACEnabled ? "Enabled" : "Disabled")) + " WAC.");
            }
        } else if (args[0].toLowerCase().equals("testban")) {
            if (PlayerManager.getPlayer(p).getRank().Has(p, Rank.ADMIN, true)) {
                WAC.banPlayer(WAC.getPlayer(p), false);
                p.sendMessage(Chat.Wprefix + "Will do! Activating a test ban!");
            }
        }
    }
}
