package com.snowbud56.warden.checks.movement;

/*
* Created by snowbud56 on March 17, 2018
* Do not change or use this code without permission
*/

import com.snowbud56.warden.checks.CheckResult;

public class NoSlowDown {

    private static CheckResult result;

//    public static void registerMove(Distance d, WACPlayer p) {
//        if (p.getPlayer().isFlying() || p.getPlayer().getLocation().add(0, -1, 0).getBlock().getType() == Material.AIR) {
//            return;
//        }
//        Double xzDist = (d.getxDiff() > d.getzDiff() ? d.getxDiff() : d.getzDiff());
//        if (xzDist > WACSettings.MAX_XZ_EATING_SPEED && p.getFoodStarting() != null && System.currentTimeMillis() - p.getFoodStarting() > 1200) {
//            p.addFoodInvalidAmount();
//        }
//    }
//
//    public static CheckResult runCheck(Distance d, WACPlayer u) {
//        Double xzDist = (d.getxDiff() > d.getzDiff() ? d.getxDiff() : d.getzDiff());
//        if (u.getPlayer().isBlocking() && xzDist > WACSettings.MAX_XZ_BLOCKING_SPEED) {
//            result = new CheckResult(Level.DEFINITELY, "tried to most too fast whilst blocking. ", CheckType.NOSLOWBLOCK);
//            return result;
//        }
//        result = new CheckResult(Level.PASSED, null, CheckType.NOSLOWEATING);
//        return result;
//    }
}
