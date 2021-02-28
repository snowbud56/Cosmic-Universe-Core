package com.snowbud56.npc;

/*
* Created by snowbud56 on February 07, 2018
* Do not change or use this code without permission
*/

import java.util.ArrayList;
import java.util.List;

public class NPCManager {

    private List<NPC> npcs = new ArrayList<>();

    public void addNPC(NPC npc) {
        npcs.add(npc);
    }

    public List<NPC> getNPCs() {
        return npcs;
    }
}
