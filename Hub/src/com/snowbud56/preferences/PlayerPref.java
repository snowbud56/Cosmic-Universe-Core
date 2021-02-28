package com.snowbud56.preferences;

/*
* Created by snowbud56 on January 09, 2018
* Do not change or use this code without permission
*/

public class PlayerPref {

    private Boolean playerVis, chat, pms, novelocity, gamemode, join, fly, forcefield, vanish;

    PlayerPref(boolean playerVis, boolean chat, boolean pms, boolean novelocity, boolean gamemode, boolean join, boolean fly, boolean forcefield) {
        this.chat = chat;
        this.fly = fly;
        this.playerVis = playerVis;
        this.pms = pms;
        this.novelocity = novelocity;
        this.gamemode = gamemode;
        this.join = join;
        this.forcefield = forcefield;
    }

    public Boolean getPms() {
        return pms;
    }

    public void setPms(Boolean pms) {
        this.pms = pms;
    }

    public Boolean getNovelocity() {
        return novelocity;
    }

    public void setNovelocity(Boolean novelocity) {
        this.novelocity = novelocity;
    }

    public Boolean getGamemode() {
        return gamemode;
    }

    public void setGamemode(Boolean gamemode) {
        this.gamemode = gamemode;
    }

    public Boolean getJoin() {
        return join;
    }

    public void setJoin(Boolean join) {
        this.join = join;
    }

    public Boolean getFly() {
        return fly;
    }

    public void setFly(Boolean fly) {
        this.fly = fly;
    }

    public Boolean getChat() {
        return chat;
    }

    public void setChat(Boolean chat) {
        this.chat = chat;
    }

    public Boolean getPlayerVis() {
        return playerVis;
    }

    public void setPlayerVis(Boolean playerVis) {
        this.playerVis = playerVis;
    }

    public Boolean getForcefield() {
        return forcefield;
    }

    public void setForcefield(Boolean forcefield) {
        this.forcefield = forcefield;
    }

    public Boolean getVanish() {
        return vanish;
    }

    public void setVanish(Boolean vanish) {
        this.vanish = vanish;
    }
}
