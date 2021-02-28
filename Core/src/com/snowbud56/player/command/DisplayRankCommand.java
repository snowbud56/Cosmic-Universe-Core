package com.snowbud56.player.command;

/*
 * Created by snowbud56 on March 28, 2019
 * Do not change or use this code without permission
 */

import com.snowbud56.command.CommandBase;
import com.snowbud56.player.CorePlayer;
import com.snowbud56.player.PlayerManager;
import com.snowbud56.player.Rank;
import com.snowbud56.util.Chat;
import com.snowbud56.util.TabUtils;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DisplayRankCommand extends CommandBase {

    private List<Rank> bannedRanks = new ArrayList<>();

    public DisplayRankCommand() {
        super(Rank.SNOW, "displayrank");
        bannedRanks.addAll(Arrays.asList(Rank.SNOW, Rank.TESTER, Rank.BUILD, Rank.YOUTUBE));
    }

    @Override
    public void execute(Player p, String[] args) {
        CorePlayer cp = PlayerManager.getPlayer(p);
        if (args.length == 0) {
            List<String> ranks = new ArrayList<>();
            for (Rank r : Rank.values())
                if (cp.getRank().Has(p, r, false) && !bannedRanks.contains(r))
                    ranks.add(r.name());
            p.sendMessage(Chat.prefix + Chat.mBody + "Set rank usages:");
            p.sendMessage(Chat.help(aliasUsed, "<rank>", "Set's a player's rank", Rank.ADMIN));
            p.sendMessage(Chat.mBody + "Avaliable ranks: " + Chat.cRed + ranks.toString().replace("[", "").replace("]", ""));
            return;
        }
        Rank rank;
        try {
            rank = Rank.valueOf(args[0]);
        } catch (Exception ex) {
            List<String> ranks = new ArrayList<>();
            for (Rank r : Rank.values())
                if (cp.getRank().Has(p, r, false) && !bannedRanks.contains(r))
                    ranks.add(r.name());
            p.sendMessage(Chat.mHead + "Error> " + Chat.mBody + "That is an invalid rank!\n" +
                    Chat.mHead + "Error> " + Chat.mBody + "Avaliable ranks (case-sensitive): " + Chat.cRed + ranks.toString().replace("[", "").replace("]", ""));
            return;
        }
        if (!cp.getRank().Has(p, rank, false)) {
            p.sendMessage(Chat.prefix + "You don't have permission to access this rank!");
        } else if (bannedRanks.contains(rank)) {
            p.sendMessage(Chat.prefix + "That rank has been display banned by the owner!");
        } else {
            cp.setDisplayRank(rank);
            p.sendMessage(Chat.prefix + "Your display rank is now " + Chat.element(rank.name) + "!");
            TabUtils.updatePlayerPrefixRank(p);
        }
    }
}
