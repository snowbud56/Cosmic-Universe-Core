package com.snowbud56.warden.checks.player;

/*
* Created by snowbud56 on March 19, 2018
* Do not change or use this code without permission
*/

import com.snowbud56.warden.WAC;
import com.snowbud56.warden.checks.CheckResult;
import com.snowbud56.warden.checks.CheckType;
import com.snowbud56.warden.checks.Level;
import com.snowbud56.warden.util.WACPlayer;
import com.snowbud56.warden.util.WACSettings;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class FastHeal {

    private static CheckResult result;

    public static CheckResult runCheck(Player entity) {
        WACPlayer player = WAC.getPlayer(entity);
        player.setLastRegen();
        if (player.getLastRegen() != null)
            for (PotionEffect effect : entity.getActivePotionEffects()) {
                if (effect.getType() == PotionEffectType.REGENERATION) {
                    if (effect.getAmplifier() == 1)
                        if (System.currentTimeMillis() - player.getLastRegen() < WACSettings.MIN_HEAL_DELAY_REGEN_1)
                            result = new CheckResult(Level.DEFINITELY, "tried to heal too fast. " + (System.currentTimeMillis() - player.getLastRegen()) + "ms delay, min: " + WACSettings.MIN_HEAL_DELAY_REGEN_1, CheckType.FASTHEAL);
                        else if (effect.getAmplifier() == 2)
                        if (System.currentTimeMillis() - player.getLastRegen() < WACSettings.MIN_HEAL_DELAY_REGEN_2)
                            result = new CheckResult(Level.DEFINITELY, "tried to heal too fast. " + (System.currentTimeMillis() - player.getLastRegen()) + "ms delay, min: " + WACSettings.MIN_HEAL_DELAY_REGEN_2, CheckType.FASTHEAL);
                        else if (effect.getAmplifier() == 3)
                        if (System.currentTimeMillis() - player.getLastRegen() < WACSettings.MIN_HEAL_DELAY_REGEN_3)
                            result = new CheckResult(Level.DEFINITELY, "tried to heal too fast. " + (System.currentTimeMillis() - player.getLastRegen()) + "ms delay, min: " + WACSettings.MIN_HEAL_DELAY_REGEN_3, CheckType.FASTHEAL);
                    else if (effect.getAmplifier() == 4)
                        if (System.currentTimeMillis() - player.getLastRegen() < WACSettings.MIN_HEAL_DELAY_REGEN_4)
                            result = new CheckResult(Level.DEFINITELY, "tried to heal too fast. " + (System.currentTimeMillis() - player.getLastRegen()) + "ms delay, min: " + WACSettings.MIN_HEAL_DELAY_REGEN_4, CheckType.FASTHEAL);
                    else if (effect.getAmplifier() == 5)
                        if (System.currentTimeMillis() - player.getLastRegen() < WACSettings.MIN_HEAL_DELAY_REGEN_5)
                            result = new CheckResult(Level.DEFINITELY, "tried to heal too fast. " + (System.currentTimeMillis() - player.getLastRegen()) + "ms delay, min: " + WACSettings.MIN_HEAL_DELAY_REGEN_5, CheckType.FASTHEAL);
                    else
                        if (System.currentTimeMillis() - player.getLastRegen() < WACSettings.MIN_HEAL_DELAY_REGEN_MAX)
                            result = new CheckResult(Level.DEFINITELY, "tried to heal too fast. " + (System.currentTimeMillis() - player.getLastRegen()) + "ms delay, min: " + WACSettings.MIN_HEAL_DELAY_REGEN_MAX, CheckType.FASTHEAL);
                }
            }
            if (System.currentTimeMillis() - player.getLastRegen() < WACSettings.MIN_HEAL_DELAY) {
                result = new CheckResult(Level.DEFINITELY, "tried to heal too fast. " + (System.currentTimeMillis() - player.getLastRegen()) + "ms delay, min: " + WACSettings.MIN_HEAL_DELAY, CheckType.FASTHEAL);
            }
        result = new CheckResult(Level.PASSED, null, CheckType.FASTHEAL);
        return result;
    }
}
