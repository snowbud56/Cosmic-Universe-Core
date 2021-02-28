package com.snowbud56.warden.util;

/*
* Created by snowbud56 on March 17, 2018
* Do not change or use this code without permission
*/

import com.snowbud56.player.PlayerManager;
import com.snowbud56.player.Rank;
import com.snowbud56.util.Chat;
import com.snowbud56.warden.checks.CheckType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WACPlayer {

    private Player player;
    private HashMap<CheckType, List<Long>> violations = new HashMap<>();
    private Boolean usingBow = false;
    private Location lastBlockLocation;
    private Long lastTimeOnBlock = 0L;
    private Long bowStart = 0L;
    private Long foodStart = 0L;
    private Long lastMove = 0L;
    private Long lastVelocity = 0L;
    private Vector lastVelocitySpeed;
    private Long lastRegen = 0L;

    public WACPlayer(Player p) {
        this.player = p;
        for (CheckType type : CheckType.values()) {
            violations.put(type, new ArrayList<>());
        }
    }

    public Player getPlayer() {
        return player;
    }

    public void addLog(CheckType type) {
        this.violations.computeIfAbsent(type, k -> new ArrayList<>());
        if (!this.violations.get(type).isEmpty()) {
            List<Long> toRemove = new ArrayList<>();
            for (Long time : violations.get(type))
                if (time < (System.currentTimeMillis() - type.getTimePeriod() * 60000))
                    toRemove.add(time);
            for (Long time : toRemove) violations.get(type).remove(time);
            toRemove.clear();
        }
        this.violations.get(type).add(System.currentTimeMillis());
        if (this.violations.get(type).size() >= type.getTimesBeforeBan()) {
            if (WACSettings.WACBanning) {
                for (Player player : Bukkit.getOnlinePlayers())
                    if (PlayerManager.getPlayer(player).getRank().Has(player, Rank.HELPER, false))
                        player.sendMessage(Chat.Wprefix + Chat.element(this.player.getName()) + " would've been banned for " + Chat.element(type.getName()) + ", but banning isn't set up because snowbud56 is bad.");
            } else {
                player.kickPlayer(Chat.cRed + Chat.Bold + "You have been kicked.\n\n" + Chat.cGray + "Reason: " + Chat.cRed + "[WAC] Blacklisted modifications detected.");
                for (Player player : Bukkit.getOnlinePlayers())
                    if (PlayerManager.getPlayer(player).getRank().Has(player, Rank.HELPER, false))
                        player.sendMessage(Chat.Wprefix + Chat.element(this.player.getName()) + " has been kicked for " + Chat.element(type.getName()) + ".");
                System.out.println(Chat.Wprefix + Chat.element(player.getName()) + " has been kicked for " + Chat.element(type.getName()) + ".");
            }
        }
    }

    public Integer getViolations(CheckType type) {
        return this.violations.getOrDefault(type, new ArrayList<>()).size();
    }

    public void setLastMove() {
        this.lastMove = System.currentTimeMillis();
    }

    public Long getLastMove() {
        return lastMove;
    }

    public Long getBowStart() {
        return bowStart;
    }

    public void setBowStart() {
        this.bowStart = System.currentTimeMillis();
    }

    public void setFoodStarting() {
        this.foodStart = System.currentTimeMillis();
    }

    public Long getFoodStarting() {
        return foodStart;
    }

    public void clearFoodStarting() {
        foodStart = null;
    }

    public void clearBow() {
        bowStart = null;
        usingBow = false;
    }

    public Boolean getBow() {
        return usingBow;
    }

    public void setBow(Boolean usingBow) {
        this.usingBow = usingBow;
    }

    public Long getLastRegen() {
        return lastRegen;
    }

    public void setLastRegen() {
        this.lastRegen = System.currentTimeMillis();
    }

    public Long getLastVelocity() {
        return lastVelocity;
    }

    public void setLastVelocity() {
        this.lastVelocity = System.currentTimeMillis();
    }

    public Long getLastTimeOnBlock() {
        return lastTimeOnBlock;
    }

    public void setLastTimeOnBlock() {
        this.lastTimeOnBlock = System.currentTimeMillis();
    }

    public Location getLastBlockLocation() {
        return lastBlockLocation;
    }

    public void setLastBlockLocation(Location lastBlockLocation) {
        if (lastBlockLocation.add(0, -1, 0).getBlock().getType() == Material.AIR)
            while (lastBlockLocation.add(0, -1, 0).getBlock().getType() == Material.AIR)
                lastBlockLocation = lastBlockLocation.add(0, -0.1, 0);
        this.lastBlockLocation = lastBlockLocation;
    }

    public void setLastVelocitySpeed(Vector lastVelocitySpeed) {
        this.lastVelocitySpeed = lastVelocitySpeed;
    }
}
