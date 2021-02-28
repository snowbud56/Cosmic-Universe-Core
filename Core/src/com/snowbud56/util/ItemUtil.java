package com.snowbud56.util;

/*
* Created by snowbud56 on February 16, 2018
* Do not change or use this code without permission
*/

import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.AbstractMap;
import java.util.LinkedList;
import java.util.Map;

public class ItemUtil {
    public static LinkedList<Map.Entry<Material, Byte>> matchItem(Player caller, String items, boolean inform) {
        LinkedList<Map.Entry<Material, Byte>> matchList = new LinkedList<Map.Entry<Material, Byte>>();
        String failList = "";
        for (String cur : items.split(",")) {
            Map.Entry<Material, Byte> match = searchItem(caller, cur, inform);
            if (match != null)
                matchList.add(match);
            else
                failList += cur + " " ;
        }
        if (inform && failList.length() > 0) {
            failList = failList.substring(0, failList.length() - 1);
            caller.sendMessage(Chat.prefix + "Invalid item! Did you mean: " + Chat.cRed + failList);
        }
        return matchList;
    }

    public static Map.Entry<Material, Byte> searchItem(Player caller, String args, boolean inform) {
        LinkedList<Map.Entry<Material, Byte>> matchList = new LinkedList<>();
        for (Material cur : Material.values()) {
            //By Name
            if (cur.toString().equalsIgnoreCase(args))
                return new AbstractMap.SimpleEntry<>(cur, (byte)0);
            if (cur.toString().toLowerCase().contains(args.toLowerCase()))
                matchList.add(new AbstractMap.SimpleEntry<>(cur, (byte)0));
            String[] arg = args.split(":");
            int id = 0;
            try {
                if (arg.length > 0)
                    id = Integer.parseInt(arg[0]);
            }
            catch (Exception e) {
                continue;
            }
            if (id != cur.getId())
                continue;
            byte data = 0;
            try {
                if (arg.length > 1)
                    data = Byte.parseByte(arg[1]);
            }
            catch (Exception e) {
                continue;
            }
            return new AbstractMap.SimpleEntry<>(cur, data);
        }
        if (matchList.size() != 1) {
            if (!inform)
                return null;
            caller.sendMessage(Chat.prefix + "Invalid item! Did you mean: " + Chat.cRed + args);
            if (matchList.size() > 0) {
                String matchString = "";
                for (Map.Entry<Material, Byte> cur : matchList)
                    matchString += cur.getKey().toString() + ", ";
                if (matchString.length() > 1)
                    matchString = matchString.substring(0 , matchString.length() - 2);
                caller.sendMessage(Chat.prefix + "Invalid item! Did you mean: " + Chat.cRed + matchString);
            }
            return null;
        }
        return matchList.get(0);
    }
}
