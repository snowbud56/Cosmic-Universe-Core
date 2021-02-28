package com.snowbud56.warden.checks.movement;

/*
* Created by snowbud56 on March 17, 2018
* Do not change or use this code without permission
*/

import com.snowbud56.warden.checks.CheckResult;
import com.snowbud56.warden.checks.CheckType;
import com.snowbud56.warden.checks.Level;
import com.snowbud56.warden.util.Distance;
import com.snowbud56.warden.util.WACPlayer;
import com.snowbud56.warden.util.WACSettings;
import org.bukkit.Material;
import org.bukkit.potion.PotionEffect;

public class SpeedCheck {

    private static CheckResult result;

    public static CheckResult runCheck(Distance d, WACPlayer p) {
        if (p.getPlayer().isFlying() || p.getPlayer().getLocation().add(0, -1, 0).getBlock().getType() == Material.AIR || System.currentTimeMillis() - p.getLastVelocity() < WACSettings.SPEED_IGNORE_SINCE_VELOCITY * 1000) {
            result = new CheckResult(Level.PASSED, null, CheckType.SPEED);
            return result;
        }
        Double xz_speed = (d.getxDiff() > d.getzDiff() ? d.getxDiff() : d.getzDiff());
        Double speed_min = WACSettings.MAX_XZ_SPEED;
        for (PotionEffect effect : p.getPlayer().getActivePotionEffects()) {
            if (effect.getType().getName().equals("SPEED")) {
                int amp = effect.getAmplifier();
                while (amp > 0) {
                    speed_min += WACSettings.MAX_XZ_SPEED_MULTIPLIER;
                    amp--;
                }
            }
        }
        if (xz_speed > speed_min)
            result = new CheckResult(Level.DEFINITELY, "tried to move faster than normal. Player speed: " + xz_speed.toString() + ", max: " + speed_min, CheckType.SPEED);
        else
            result = new CheckResult(Level.PASSED, null, CheckType.SPEED);
        return result;
    }
}