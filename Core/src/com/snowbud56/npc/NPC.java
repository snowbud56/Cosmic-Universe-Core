package com.snowbud56.npc;

/*
* Created by snowbud56 on February 07, 2018
* Do not change or use this code without permission
*/

public interface NPC {

    void spawn();

    void setNoAI(Boolean noAI);
    void setBaby(Boolean baby);
    boolean getNoAI();
    boolean getBaby();

}
