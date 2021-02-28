package com.snowbud56.game.games;

/*
 * Created by snowbud56 on May 28, 2019
 * Do not change or use this code without permission
 */

import com.snowbud56.game.GameBase;
import com.snowbud56.game.GameManager;
import com.snowbud56.game.GameType;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.HashMap;

public class CTFMinigame extends GameBase implements Listener {
    private HashMap<Player, Integer> points;
    private boolean explosiveMode = false;
    private GameType gameType = GameType.QUAKE;
    private Scoreboard gameBoard;
    private Objective pointsObj;

    public CTFMinigame(String mapName) {
        super("ctf", mapName);
        points = new HashMap<>();
        gameBoard = GameManager.gameScoreboard;
        pointsObj = gameBoard.getObjective("points");
        if (pointsObj == null) pointsObj = gameBoard.registerNewObjective("points", "dummy");
    }

    @Override
    public GameType getType() {
        return GameType.CTF;
    }

    @Override
    public void postLobbyCountdown() {

    }

    @Override
    public void gameStart() {

    }

    @Override
    public void preEndGame() {

    }

    @Override
    public void postEndGame() {

    }
}
