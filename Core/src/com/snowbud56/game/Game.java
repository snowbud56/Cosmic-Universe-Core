package com.snowbud56.game;
/*
 * Created by snowbud56 on April 08, 2019
 * Do not change or use this code without permission
 */

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;

public interface Game {
    GameType getType();
    void onDisable();

    void quitGame(Player player);
    void joinGame(Player player, boolean newGame, boolean initialSetup);

    void respawn(Player player);
    void setSpectator(Player player);

    void startCountDown(Boolean forceStart);
    void setCountdown(int countdown);
    void postLobbyCountdown();
    void gameStart();
    void preEndGame();
    void postEndGame();
    void startGame(Boolean forceStart);
    void endGame(Boolean forced, Player winner);

    boolean isState(GameState state);
    void setState(GameState state);

    void sendMessage(String message);

    boolean canMove(Player p);

    String getMapName();
    List<Location> getSpawnPoints();
}
