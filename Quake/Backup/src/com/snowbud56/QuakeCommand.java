package com.snowbud56;

import com.snowbud56.command.CommandBase;
import com.snowbud56.game.constructors.Game;
import com.snowbud56.player.PlayerManager;
import com.snowbud56.player.Rank;
import com.snowbud56.util.Chat;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class QuakeCommand extends CommandBase {

    QuakeCommand() {
        super(Rank.ADMIN, "game", "g", "quake", "quakecraft");
    }

    @Override
    public void execute(Player p, String[] args) {
        if (args.length == 0) {
            p.sendMessage(Chat.prefix + "Quake Commands:");
            p.sendMessage(Chat.help(aliasUsed, "setpoints <player> <points>", "Sets the points for a specified player.", Rank.OWNER));
            p.sendMessage(Chat.help(aliasUsed, "start", "Starts the game.", Rank.ADMIN));
            p.sendMessage(Chat.help(aliasUsed, "stop", "Stops the game.", Rank.ADMIN));
            p.sendMessage(Chat.help(aliasUsed, "explosive", "Toggles explosive mode for the game.", Rank.ADMIN));
            return;
        }
        String arg1 = args[0].toLowerCase();
        Game game = QuakeMinigame.getGame();
        switch (arg1) {
            case ("start"):
                if (game.isState(Game.GameState.LOBBY)) {
                    game.sendMessage(Chat.element(p.getName()) + " has started the game.");
                    game.startCountDown(true);
                } else {
                    p.sendMessage(Chat.prefix + "Unable to start the game (The game has already been started)");
                }
                break;
            case ("setpoints"):
                if (PlayerManager.getPlayer(p).getRank().Has(p, Rank.OWNER, true)) {
                    if (!game.isState(Game.GameState.ACTIVE)) {
                        p.sendMessage(Chat.prefix + "An error occured: (GameBase isn't active!)");
                    } else {
                        try {
                            game.setPoints(Bukkit.getPlayer(args[1]), Integer.parseInt(args[2]));
                            p.sendMessage(Chat.AdminPrefix + "Set " + Chat.adminElement(Bukkit.getPlayer(args[1]).getName()) + "'s points to " + Chat.adminElement(args[2]));
                        } catch (Exception e) {
                            p.sendMessage(Chat.AdminPrefix + "An error occured. Exception: " + Chat.adminElement(e.toString()));
                        }
                    }
                }
                break;
            case ("stop"):
                if (game.isState(Game.GameState.STARTING)) {
                    game.setState(Game.GameState.LOBBY);
                    game.sendMessage(Chat.element(p.getName()) + " has stopped the game.");
                } else if (game.isState(Game.GameState.ACTIVE)) {
                    game.endGame(true, p);
                    game.sendMessage(Chat.element(p.getName()) + " has stopped the game.");
                }
                else
                    p.sendMessage(Chat.prefix + "Unable to stop the game (The game hasn't been started yet)");
                break;
            case ("explosive"):
                if (game.isExplosiveMode()) {
                    game.sendMessage(p.getName() + " has disabled explosive mode!");
                    p.sendMessage(Chat.prefix + "Successfully disabled explosive mode");
                    game.setExplosiveMode(false);
                } else {
                    game.sendMessage(p.getName() + " has enabled explosive mode!");
                    p.sendMessage(Chat.prefix + "Successfully enabled explosive mode.");
                    game.setExplosiveMode(true);
                }
                break;
        }
    }
}
