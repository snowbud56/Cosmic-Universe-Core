package com.snowbud56.game.commands;

/*
 * Created by snowbud56 on April 14, 2019
 * Do not change or use this code without permission
 */

import com.snowbud56.command.CommandBase;
import com.snowbud56.game.GameManager;
import com.snowbud56.game.games.QuakeMinigame;
import com.snowbud56.player.Rank;
import com.snowbud56.util.Chat;
import org.bukkit.entity.Player;

public class ExplosiveCommand extends CommandBase {
    public ExplosiveCommand() {
        super(Rank.ADMIN, "explosive", "explosivemode");
    }

    @Override
    public void execute(Player player, String[] strings) {
        if (!(GameManager.getActiveGame() instanceof QuakeMinigame)) {
            player.sendMessage(Chat.prefix + "The active game is not a compatible game for this command.");
            return;
        }
        QuakeMinigame game = (QuakeMinigame) GameManager.getActiveGame();
        if (game.isExplosiveMode()) {
            game.setExplosiveMode(false);
            game.sendMessage(player.getDisplayName() + " has disabled explosive mode!");
        } else {
            game.setExplosiveMode(true);
            game.sendMessage(player.getDisplayName() + " has enabled explosive mode!");
        }
    }
}
