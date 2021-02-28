package com.snowbud56.warden.checks.player;

/*
* Created by snowbud56 on March 18, 2018
* Do not change or use this code without permission
*/

import com.snowbud56.warden.checks.CheckResult;
import com.snowbud56.warden.checks.CheckType;
import com.snowbud56.warden.checks.Level;
import com.snowbud56.warden.util.WACPlayer;
import com.snowbud56.warden.util.WACSettings;

public class FastUse {

    private static CheckResult result;

    public static CheckResult runBow(WACPlayer p) {
        if (p.getBowStart() != null && System.currentTimeMillis() - p.getBowStart() < WACSettings.BOW_MIN) {
            result = new CheckResult(Level.DEFINITELY, "tried to shoot too fast. Took " + (System.currentTimeMillis() - p.getBowStart()) + " ms to shoot, min: " + WACSettings.BOW_MIN, CheckType.FASTUSE);
            return result;
        }
        result = new CheckResult(Level.PASSED, null, CheckType.FASTUSE);
        return result;
    }

    public static CheckResult runFood(WACPlayer p) {
        if (p.getFoodStarting() != null && System.currentTimeMillis() - p.getFoodStarting() < WACSettings.FOOD_MIN) {
            result = new CheckResult(Level.DEFINITELY, "tried to eat too fast. Took " + (System.currentTimeMillis() - p.getFoodStarting()) + " ms to eat, min: " + WACSettings.FOOD_MIN, CheckType.FASTUSE);
            return result;
        }
        result = new CheckResult(Level.PASSED, null, CheckType.FASTUSE);
        return result;
    }

}
