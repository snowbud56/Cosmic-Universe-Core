package com.snowbud56.util;

import com.snowbud56.player.PlayerManager;
import com.snowbud56.player.Rank;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;

public class Chat {

    public static String Wprefix = "§c§lWarden §8// §7";
    public static String prefix = "§9§lCosmic §8// §7";
    public static String AdminPrefix = "§c§lCosmic §4// §f";
    public static String Scramble = "§k";
    public static String Bold = "§l";
    public static String Strike = "§m";
    public static String BoldStrike = "§l§m";
    public static String Line = "§n";
    public static String Italics = "§o";
    public static String Reset = "§r";

    public static String cAqua = "" + ChatColor.AQUA;
    public static String cBlack = "" + ChatColor.BLACK;
    public static String cBlue = "" + ChatColor.BLUE;
    public static String cDAqua = "" + ChatColor.DARK_AQUA;
    public static String cDBlue = "" + ChatColor.DARK_BLUE;
    public static String cDGray = "" + ChatColor.DARK_GRAY;
    public static String cDGreen = "" + ChatColor.DARK_GREEN;
    public static String cDPurple = "" + ChatColor.DARK_PURPLE;
    public static String cDRed = "" + ChatColor.DARK_RED;
    public static String cGold = "" + ChatColor.GOLD;
    public static String cGray = "" + ChatColor.GRAY;
    public static String cGreen = "" + ChatColor.GREEN;
    public static String cPurple = "" + ChatColor.LIGHT_PURPLE;
    public static String cRed = "" + ChatColor.RED;
    public static String cWhite = "" + ChatColor.WHITE;
    public static String cYellow = "" + ChatColor.YELLOW;
    public static String mHead = "" + ChatColor.BLUE;
    public static String mBody = "" + ChatColor.GRAY;
    public static String mChat = "" + ChatColor.WHITE;
    public static String mElem = "" + ChatColor.YELLOW;

    public static String element(String text) {
        return cRed + text + cGray;
    }
    public static String adminElement(String text) {
        return cRed + text + cWhite;
    }
    public static void messageRank(String message, Rank rank) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (PlayerManager.getPlayer(player).getRank().Has(player, rank, false)) {
                player.sendMessage(message);
            }
        }
    }
    public static void messageRank(String message, Rank rank, Rank[] specificRanks) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (PlayerManager.getPlayer(player).getRank().Has(player, rank, specificRanks,false)) {
                player.sendMessage(message);
            }
        }
    }
    public static String help(String aliasUsed, String arguments, String usage, Rank rank) {
        return prefix + "/" + aliasUsed + " " + arguments + ": " + usage + " " + rank.getTag(false, false, false);
    }

    public static String playerNotFound(String playerName, List<String> matches) {
        return Chat.prefix + (matches.size() == 0 ? Chat.cRed + playerName + Chat.mBody + " isn't online or doesn't exist!" : "Too many matches! Did you mean: " + Chat.cRed + matches.toString().replace("[", "").replace("]", ""));
    }
}
