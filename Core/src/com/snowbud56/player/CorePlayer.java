package com.snowbud56.player;

/*
* Created by snowbud56 on January 08, 2018
* Do not change or use this code without permission
*/

import org.bukkit.entity.Player;

public class CorePlayer {

    private Rank rank;
    private Rank displayRank;
    private Player player;
    private Integer emeralds;
    private Boolean isTestingRank = false;
    private Boolean canMove = true;

    public CorePlayer(Player player, Rank rank, Rank displayRank, Integer emeralds) {
        this.player = player;
        this.rank = rank;
        this.displayRank = displayRank;
        this.emeralds = emeralds;
    }

    public Player getPlayer() {
        return player;
    }

    public Rank getRank() {
        return rank;
    }

    public void setRank(Rank rank) {
        this.rank = rank;
    }

    public Integer getEmeralds() {
        return emeralds;
    }

    public void setTestingRank(Boolean testing) {
        this.isTestingRank = testing;
    }

    public boolean isTestingRank() {
        return this.isTestingRank;
    }

    public Rank getDisplayRank() {
        return displayRank;
    }

    public void setDisplayRank(Rank displayRank) {
        this.displayRank = displayRank;
    }

    public void setCanMove(Boolean canMove) {
        this.canMove = canMove;
    }

    public Boolean canMove() {
        return canMove;
    }
}
