package com.snowbud56.chat;

/*
* Created by snowbud56 on January 08, 2018
* Do not change or use this code without permission
*/

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class ChatManager {

    public static Map<Player, Long> lastChat = new HashMap<>();

    public static int delay = -1;
    public static boolean silenced = false;

}
