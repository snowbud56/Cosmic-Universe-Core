package com.snowbud56.preferences.command;

/*
* Created by snowbud56 on February 05, 2018
* Do not change or use this code without permission
*/

import com.snowbud56.command.CommandBase;
import com.snowbud56.player.PlayerManager;
import com.snowbud56.player.Rank;
import com.snowbud56.preferences.PlayerPref;
import com.snowbud56.preferences.PrefManager;
import com.snowbud56.util.Chat;
import com.snowbud56.util.PlayersUtil;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.List;

@SuppressWarnings("deprecated")
public class FlyCommand extends CommandBase {
    public FlyCommand() {
        super(Rank.HELPER, new Rank[] {Rank.SNOW, Rank.YOUTUBE}, "fly", "flight");
    }

    @Override
    public void execute(Player p, String[] args) {
        if (args.length > 0 && PlayerManager.getPlayer(p).getRank().Has(p, Rank.ADMIN, false)) {
            List<String> matches = PlayersUtil.findPlayer(p, args[0], false);
            if (matches.size() == 1) {
                Player target = Bukkit.getPlayer(matches.get(0));
                if (target == null) {
                    OfflinePlayer t = Bukkit.getOfflinePlayer(matches.get(0));
                    PrefManager.updatePlayerbyUUID(t.getUniqueId());
                    PlayerPref targetpref = PrefManager.getPlayerPrefs(t.getUniqueId());
                    targetpref.setFly(!targetpref.getFly());
                    PrefManager.setPlayerPrefs(t.getUniqueId(), targetpref);
                    p.sendMessage(Chat.prefix + "Flight for " + t.getName() + " " + (targetpref.getFly() ? "enabled" : "disabled") + ".");
                    PrefManager.savePlayerPrefs(t.getUniqueId());
                } else {
                    PlayerPref targetpref = PrefManager.getPlayerPrefs(target.getUniqueId());
                    targetpref.setFly(!targetpref.getFly());
                    target.setAllowFlight(targetpref.getFly());
                    PrefManager.setPlayerPrefs(target.getUniqueId(), targetpref);
                    p.sendMessage(Chat.prefix + "Flight for " + target.getDisplayName() + " " + (targetpref.getFly() ? "enabled" : "disabled") + ".");
                    target.sendMessage(Chat.prefix + "Flight " + (targetpref.getFly() ? "enabled" : "disabled") + ".");
                }
            } else p.sendMessage(Chat.prefix + (matches.size() == 0 ? Chat.cRed + args[0] + Chat.cGray + " isn't online or doesn't exist!" : "Too many matches! Did you mean: " + Chat.cRed + matches.toString().replace("[", "").replace("]", "")));
            return;
        }
        PlayerPref pref = PrefManager.getPlayerPrefs(p.getUniqueId());
        pref.setFly(!pref.getFly());
        p.setAllowFlight(pref.getFly());
        p.sendMessage(Chat.prefix + "Flight " + (pref.getFly() ? "enabled" : "disabled") + ".");
        PrefManager.setPlayerPrefs(p.getUniqueId(), pref);
    }
}
