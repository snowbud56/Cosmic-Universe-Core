package com.snowbud56.util;

/*
* Created by snowbud56 on January 08, 2018
* Do not change or use this code without permission
*/

import org.bukkit.scoreboard.Objective;

public class ScoreboardUtil {

    public static void setupScores(Objective obj, String[] lines) {
        int linenum = 15;
        for (int i = 0; i < 15 && i < lines.length; i++) {
            obj.getScore(lines[i]).setScore(linenum);
            linenum--;
        }
    }
}
