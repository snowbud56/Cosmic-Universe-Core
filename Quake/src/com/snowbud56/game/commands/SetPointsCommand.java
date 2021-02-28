package com.snowbud56.game.commands;

/*
 * Created by snowbud56 on April 14, 2019
 * Do not change or use this code without permission
 */

import com.snowbud56.command.CommandBase;
import com.snowbud56.game.GameManager;
import com.snowbud56.game.GameState;
import com.snowbud56.game.games.QuakeMinigame;
import com.snowbud56.player.PlayerManager;
import com.snowbud56.player.Rank;
import com.snowbud56.util.Chat;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class SetPointsCommand extends CommandBase {
    public SetPointsCommand() {
        super(Rank.ADMIN, "setpoints");
    }

    @Override
    public void execute(Player p, String[] args) {
        if (!(GameManager.getActiveGame() instanceof QuakeMinigame)) {
            p.sendMessage(Chat.prefix + "The active game is not a compatible game for this command.");
            return;
        }
        QuakeMinigame game = (QuakeMinigame) GameManager.getActiveGame();
        if (PlayerManager.getPlayer(p).getRank().Has(p, Rank.OWNER, true)) {
            if (!game.isState(GameState.ACTIVE)) {
                p.sendMessage(Chat.prefix + "An error occured: (Game isn't active!)");
            } else {
                try {
                    game.setPoints(Bukkit.getPlayer(args[0]), Integer.parseInt(args[1]));
                    p.sendMessage(Chat.AdminPrefix + "Set " + Chat.adminElement(Bukkit.getPlayer(args[0]).getName()) + "'s points to " + Chat.adminElement(args[1]));
                } catch (Exception e) {
                    p.sendMessage(Chat.AdminPrefix + "An error occured. Exception: " + Chat.adminElement(e.toString()));
                }
            }
        }
    }
}
