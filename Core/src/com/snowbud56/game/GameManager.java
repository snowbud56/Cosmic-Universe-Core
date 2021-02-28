package com.snowbud56.game;

/*
 * Created by snowbud56 on April 08, 2019
 * Do not change or use this code without permission
 */

import com.snowbud56.util.Chat;
import com.snowbud56.util.ScoreboardUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class GameManager {

    public static Location lobbyPoint = new Location(Bukkit.getWorld("world"), -2562.5, 55, 744.5, -90, 0);
    private static Game activeGame;
    private static GameType nextGame;
    private static String nextMap;
    public static Scoreboard gameScoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
    public static Scoreboard lobbyScoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
    public static Objective lobbyObj;

    public static Game getActiveGame() {
        return activeGame;
    }

    public static void setActiveGame(GameType gameType) {
        if (lobbyObj == null) {
            lobbyObj = lobbyScoreboard.registerNewObjective("main", "dummy");
            lobbyObj.setDisplaySlot(DisplaySlot.SIDEBAR);
            lobbyObj.setDisplayName(Chat.cBlue + Chat.Bold + "Cosmic" + Chat.cGold + Chat.Bold + " Universe");

            ScoreboardUtil.setupScores(lobbyObj, new String[] {
                    Chat.cYellow + Chat.Bold + "Players",
                    " / ",
                    Chat.cDBlue,
                    Chat.cGreen + Chat.Bold + "Game",
                    Chat.cGreen,
                    Chat.cDGreen,
                    Chat.cRed + Chat.Bold + "Map",
                    Chat.cWhite,
                    "----------------"
            });
            Team playercount = lobbyScoreboard.getTeam("playercount");
            if (playercount == null) playercount = lobbyScoreboard.registerNewTeam("playercount");
            playercount.addEntry(" / ");
            Team gameN = lobbyScoreboard.getTeam("gameName");
            if (gameN == null) gameN = lobbyScoreboard.registerNewTeam("gameName");
            gameN.addEntry(Chat.cGreen);
            Team mapName = lobbyScoreboard.getTeam("mapName");
            if (mapName == null) mapName = lobbyScoreboard.registerNewTeam("mapName");
            mapName.addEntry(Chat.cWhite);
        }
        if (activeGame != null) {
            activeGame.setState(GameState.LOBBY);
            activeGame.onDisable();
        }
        activeGame = gameType.getNewInstance(nextMap);
        nextGame = gameType.nextGame();
        nextMap = GameMaps.getRandomMap(nextGame);
    }

    public static void goToNextGame() {
        GameManager.setActiveGame(nextGame);
    }

    public static void setNextGame(GameType nextGame) {
        GameManager.nextGame = nextGame;
    }

    public static void setNextMap(String nextMap) {
        GameManager.nextMap = nextMap;
    }

    public static GameType getNextGame() {
        if (nextGame == null) nextGame = GameType.QUAKE;
        return nextGame;
    }

    public static String getNextMap() {
        return nextMap;
    }
}
