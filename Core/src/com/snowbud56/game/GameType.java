package com.snowbud56.game;
/*
 * Created by snowbud56 on April 08, 2019
 * Do not change or use this code without permission
 */

import com.snowbud56.game.games.QuakeMinigame;
import com.snowbud56.game.games.SpleggMinigame;

public enum GameType {
    QUAKE(1, "quake"),
    SPLEGG(2, "splegg"),
    CTF(3, "ctf");

    private int ID;
    private String name;

    GameType(int id, String name) {
        this.ID = id;
        this.name = name;
    }

    public String getGameName() {
        return name;
    }

    public Game getNewInstance(String mapName) {
        switch (ID) {
            case (1):
                return new QuakeMinigame(mapName);
            case (2):
                return new SpleggMinigame(mapName);
            default:
                return new QuakeMinigame(mapName);
        }
    }

    public GameType nextGame() {
        return this.ordinal() < GameType.values().length - 1 ? GameType.values()[this.ordinal() + 1] : QUAKE;
    }
}
