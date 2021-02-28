package com.snowbud56.warden.util;

/*
* Created by snowbud56 on March 17, 2018
* Do not change or use this code without permission
*/

import org.bukkit.Location;
import org.bukkit.event.player.PlayerMoveEvent;

public class Distance {

    private Location from, to;

    private Double xDiff, yDiff, zDiff;

    public Distance(PlayerMoveEvent e) {
        this.from = e.getFrom();
        this.to = e.getTo();
        xDiff = Math.abs(to.getX() - from.getX());
        yDiff = Math.abs(to.getY() - from.getY());
        zDiff = Math.abs(to.getZ() - from.getZ());
    }

    public Double getzDiff() {
        return zDiff;
    }

    public Double getyDiff() {
        return yDiff;
    }

    public Double getxDiff() {
        return xDiff;
    }

    public Location getFrom() {
        return from;
    }

    public Location getTo() {
        return to;
    }
}
