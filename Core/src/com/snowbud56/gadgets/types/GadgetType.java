package com.snowbud56.gadgets.types;

/*
* Created by snowbud56 on January 28, 2018
* Do not change or use this code without permission
*/

import org.bukkit.Material;

public enum GadgetType {

    PARTICLE("Particles", Material.NETHER_STAR, 11),
    ARROW_TRAIL("Arrow Trails", Material.ARROW, 13),
    KILL_EFFECT("Kill Effects", Material.REDSTONE_BLOCK, 15);
    //TODO

    private String name;
    private Material item;
    private Integer slot;

    GadgetType(String name, Material item, Integer slot) {
        this.name = name;
        this.item = item;
        this.slot = slot;
    }

    public String getName() {
        return name;
    }

    public Material getItem() {
        return item;
    }

    public Integer getSlot() {
        return slot;
    }

    public static GadgetType getBySlot(Integer slot) {
        for (GadgetType type : GadgetType.values()) {
            if (type.slot.equals(slot)) return type;
        }
        return null;
    }
}
