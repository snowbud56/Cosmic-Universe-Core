package com.snowbud56.warden.checks.movement;

/*
* Created by snowbud56 on March 21, 2018
* Do not change or use this code without permission
*/

import com.snowbud56.warden.checks.CheckResult;
import com.snowbud56.warden.checks.CheckType;
import com.snowbud56.warden.checks.Level;
import com.snowbud56.warden.util.Distance;
import com.snowbud56.warden.util.WACPlayer;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class FlyCheck {

    private static CheckResult result;

    public static CheckResult runCheck(Distance d, WACPlayer player) {
        Player p = player.getPlayer();
//        p.sendMessage(Chat.prefix + "yDiff = " + (System.currentTimeMillis() - player.getLastTimeOnBlock() > WACSettings.FLY_IGNORE_AFTER_JUMP ? Chat.cRed : Chat.cGray) + d.getyDiff());
//        p.sendMessage(Chat.prefix + "time = " + (System.currentTimeMillis() - player.getLastTimeOnBlock()));
        if (p.getAllowFlight() || p.getVehicle() != null || p.getEntityId() == 100) {
            if (d.getTo().clone().add(0, -1, 0).getBlock().getType() != Material.AIR)
                player.setLastBlockLocation(d.getFrom());
            player.setLastTimeOnBlock();
            result = new CheckResult(Level.PASSED, null, CheckType.FLIGHT);
            return result;
        }
        if (isOnGround(p)) {
            player.setLastTimeOnBlock();
            player.setLastBlockLocation(d.getFrom());
            result = new CheckResult(Level.PASSED, null, CheckType.FLIGHT);
            return result;
        }
//        if (System.currentTimeMillis() - player.getLastTimeOnBlock() > WACSettings.FLY_IGNORE_AFTER_JUMP) {
//            if (d.getyDiff() <= WACSettings.FLY_MIN_Y_SPEED_FALL){
//                result = new CheckResult(Level.DEFINITELY, "tried to jump too long or use fly. yDiff = " + d.getyDiff(), CheckType.FLIGHT);
//                return result;
//            }
//        }
        result = new CheckResult(Level.PASSED, null, CheckType.FLIGHT);
        return result;
    }

    private static Boolean isOnGround(Player p) {
        Location l = p.getLocation().clone().add(0, -1, 0);
        return l.add(1, 0, 1).getBlock().getType() != Material.AIR
                || l.add(0, 0, 1).getBlock().getType() != Material.AIR
                || l.add(-1, 0, 1).getBlock().getType() != Material.AIR
                || l.add(1, 0, 0).getBlock().getType() != Material.AIR
                || l.add(0, 0, 0).getBlock().getType() != Material.AIR
                || l.add(-1, 0, 0).getBlock().getType() != Material.AIR
                || l.add(1, 0, -1).getBlock().getType() != Material.AIR
                || l.add(0, 0, -1).getBlock().getType() != Material.AIR
                || l.add(-1, 0, -1).getBlock().getType() != Material.AIR;
    }
}