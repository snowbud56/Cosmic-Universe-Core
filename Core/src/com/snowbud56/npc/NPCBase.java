package com.snowbud56.npc;

/*
* Created by snowbud56 on February 07, 2018
* Do not change or use this code without permission
*/

import org.bukkit.Location;
import org.bukkit.entity.EntityType;

public abstract class NPCBase implements NPC {

    protected String displayname;
    protected EntityType type;
    protected Boolean isBaby = false;
    protected Boolean noAI = false;

    public NPCBase(EntityType type, Location loc) {
        this.type = type;
    }

    public EntityType getType() {
        return type;
    }
}
