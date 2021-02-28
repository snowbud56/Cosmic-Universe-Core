package com.snowbud56.warden.events;

/*
* Created by snowbud56 on March 17, 2018
* Do not change or use this code without permission
*/

import com.snowbud56.warden.WAC;
import com.snowbud56.warden.checks.CheckResult;
import com.snowbud56.warden.checks.movement.FlyCheck;
import com.snowbud56.warden.checks.movement.SpeedCheck;
import com.snowbud56.warden.util.Distance;
import com.snowbud56.warden.util.WACPlayer;
import com.snowbud56.warden.util.WACSettings;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerVelocityEvent;

public class MoveListener implements Listener {


    @EventHandler
    public void playerMove(PlayerMoveEvent e) {
        if (!WACSettings.WACEnabled) return;
        Distance d = new Distance(e);
        WACPlayer u = WAC.getPlayer(e.getPlayer());
        CheckResult speed = SpeedCheck.runCheck(d, u);
        if (speed.failed()) {
            e.setTo(e.getFrom());
            WAC.log(speed, u);
        }
//        Long time = System.currentTimeMillis() - u.getLastMove();
//        if (!(time <= 3) && time < WACSettings.TICKS_BEFORE_NEXT_MOVE) {
//            e.setTo(e.getFrom());
//            WAC.log(new CheckResult(Level.DEFINITELY, "tried to send packets too quick. Last move: " + time + "ms ago, max: " + WACSettings.TICKS_BEFORE_NEXT_MOVE + "ms", CheckType.TIMER), u);
//        }
        CheckResult flight = FlyCheck.runCheck(d, u);
        if (flight.failed()) {
            e.setTo(u.getLastBlockLocation());
            WAC.log(flight, u);
        }
        u.setLastMove();
    }

    @EventHandler
    public void onVelocity(PlayerVelocityEvent e) {
        WACPlayer p = WAC.getPlayer(e.getPlayer());
        p.setLastVelocity();
        p.setLastVelocitySpeed(e.getVelocity());
    }
}
