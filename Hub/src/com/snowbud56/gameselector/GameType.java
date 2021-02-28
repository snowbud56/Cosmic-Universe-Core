package com.snowbud56.gameselector;

/*
* Created by snowbud56 on March 21, 2018
* Do not change or use this code without permission
*/

import com.snowbud56.util.Chat;
import org.bukkit.Material;

public enum GameType {

    ABYSS("The Abyss", new String[] {Chat.cGray + "Fight others in the Abyss!"}, Material.OBSIDIAN, 3, "Pit"),
    ARCADE("Arcade", new String[] {Chat.cGray + "Play many games with friends", Chat.cGray + "in the arcade!"}, Material.NETHER_STAR, 5, "Arcade");

    private final String name, server;
    private final Integer slot;
    private final Material material;
    private final String[] lore;

    GameType(String name, String[] lore, Material material, Integer slot, String server) {
        this.name = name;
        this.slot = slot;
        this.lore = lore;
        this.material = material;
        this.server = server;
    }

    public String getName() {
        return name;
    }

    public Integer getSlot() {
        return slot;
    }

    public Material getMaterial() {
        return material;
    }

    public String[] getLore() {
        return lore;
    }

    public String getServer() {
        return server;
    }

    public static GameType getType(String server) {
        for (GameType type : values()) {
            if (type.getServer().equals(server)) return type;
        }
        return null;
    }
    public static GameType getType(Integer slot) {
        for (GameType type : values()) {
            if (type.getSlot() == slot) return type;
        }
        return null;
    }
}
