package com.snowbud56.game.commands;

/*
 * Created by snowbud56 on April 14, 2019
 * Do not change or use this code without permission
 */

import com.snowbud56.Core;
import com.snowbud56.command.CommandBase;
import com.snowbud56.game.*;
import com.snowbud56.player.Rank;
import com.snowbud56.util.Chat;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameCommand extends CommandBase {
    public GameCommand() {
        super(Rank.ADMIN, "game", "g");
    }

    @Override
    public void execute(Player p, String[] args) {
        if (!Core.isGameServer()) {
            p.sendMessage(Chat.prefix + "This is not a game server, so you cannot use this command!");
            return;
        }
        if (args.length == 0) {
            p.sendMessage(Chat.prefix + "Game Commands:");
            p.sendMessage(Chat.help(aliasUsed, "start [seconds]", "Starts the game with optional countdown time.", Rank.ADMIN));
            p.sendMessage(Chat.help(aliasUsed, "stop", "Stops the game.", Rank.ADMIN));
            p.sendMessage(Chat.help(aliasUsed, "set <game> [map]", "Sets the current game or next game.", Rank.ADMIN));
            p.sendMessage(Chat.help(aliasUsed, "info", "Returns some useful game information.", Rank.OWNER));
            return;
        }
        String arg1 = args[0].toLowerCase();
        Game game = GameManager.getActiveGame();
        switch (arg1) {
            case ("start"):
                if (game.isState(GameState.LOBBY)) {
                    game.sendMessage(Chat.element(p.getDisplayName()) + " has started the game.");
                    if (args.length >= 2) {
                        try {
                            game.setCountdown(Integer.parseInt(args[1]));
                        } catch (Exception e) {
                            p.sendMessage(Chat.prefix + "That's not an integer! Cannot set countdown timer.");
                        }
                    }
                    game.startCountDown(true);
                } else {
                    p.sendMessage(Chat.prefix + "Unable to start the game (The game has already been started)");
                }
                break;
            case ("stop"):
                if (game.isState(GameState.STARTING)) {
                    game.setState(GameState.LOBBY);
                    game.sendMessage(Chat.element(p.getDisplayName()) + " has stopped the game.");
                } else if (game.isState(GameState.ACTIVE)) {
                    game.endGame(true, p);
                    game.sendMessage(Chat.element(p.getDisplayName()) + " has stopped the game.");
                }
                else
                    p.sendMessage(Chat.prefix + "Unable to stop the game (The game hasn't been started yet)");
                break;
            case "set":
                try {
                    GameType gameType = GameType.valueOf(args[1].toUpperCase());
                    String mapName = (args.length >= 3 ? args[2] : null);
                    boolean randomMap = false;
                    if (game.isState(GameState.ACTIVE) || game.isState(GameState.ENDING)) {
                        if (mapName != null) {
                            if (GameMaps.gameHasMap(gameType, mapName))
                                GameManager.setNextMap(mapName.toLowerCase());
                        } else {
                            Random r = new Random();
                            String[] mapList = GameMaps.maps.get(gameType);
                            mapName = mapList[r.nextInt(mapList.length)];
                            randomMap = true;
                            GameManager.setNextMap(mapName);
                        }
                        GameManager.setNextGame(gameType);
                        Bukkit.broadcastMessage(Chat.prefix + p.getDisplayName() + " has set the next game to " + args[1].toLowerCase());
                        p.sendMessage(Chat.AdminPrefix + "Successfully set the next game to " + Chat.adminElement(args[1].toLowerCase()) + " with " + (randomMap ? "random " : "") + "map " + Chat.adminElement(mapName) + ".");
                    } else {
                        if (mapName != null) {
                            if (GameMaps.gameHasMap(gameType, mapName))
                                GameManager.setNextMap(mapName.toLowerCase());
                        } else {
                            Random r = new Random();
                            String[] mapList = GameMaps.maps.get(gameType);
                            mapName = mapList[r.nextInt(mapList.length)];
                            randomMap = true;
                            GameManager.setNextMap(mapName);
                        }
                        GameManager.setActiveGame(gameType);
                        Bukkit.broadcastMessage(Chat.prefix + p.getDisplayName() + " has set the game to " + args[1].toLowerCase());
                        p.sendMessage(Chat.AdminPrefix + "Successfully set the next game to " + Chat.adminElement(args[1].toLowerCase()) + " with " + (randomMap ? "random " : "") + "map " + Chat.adminElement(mapName) + ".");
                    }
                } catch (Exception e) {
                    List<String> games = new ArrayList<>();
                    for (GameType r : GameType.values()) games.add(r.name());
                    p.sendMessage(Chat.prefix + "That's an invalid game!");
                    p.sendMessage(Chat.prefix + "List of Games: " + games.toString().replace("[", "").replace("]", ""));
                }
                break;
            case "info":
                p.sendMessage(Chat.AdminPrefix + "More info coming soon.");
                p.sendMessage(Chat.AdminPrefix + "GameType: " + Chat.adminElement(game.getType().name()));
                p.sendMessage(Chat.AdminPrefix + "Current Map: " + Chat.adminElement(game.getMapName()));
                p.sendMessage(Chat.AdminPrefix + "Spawn Points: ");
                for (Location point : game.getSpawnPoints()) {
                    p.sendMessage(Chat.AdminPrefix + Chat.adminElement("  - " + point.getX() + ", " + point.getY() + ", " + point.getZ()));
                }
                p.sendMessage(Chat.AdminPrefix + "Next Game Type: " + Chat.adminElement(GameManager.getNextGame().toString()));
                p.sendMessage(Chat.AdminPrefix + "Next Game Map: " + Chat.adminElement(GameManager.getNextMap()));
                break;
            default:
                p.sendMessage(Chat.prefix + "Invalid usage! Please type '/" + aliasUsed + "' to see a list of subcommands.");
        }
    }
}
