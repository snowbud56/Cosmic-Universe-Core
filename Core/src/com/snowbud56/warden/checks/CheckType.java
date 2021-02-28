package com.snowbud56.warden.checks;

/*
* Created by snowbud56 on March 17, 2018
* Do not change or use this code without permission
*/

public enum CheckType {

    SPEED("Speed", 15, 1, 5),
    FASTUSE("FastUse", 10, 2, 2),
    FASTHEAL("Regen", 25, 1, 5),
    FLIGHT("Fly", 15, 2, 2),
    TIMER("Timer", 50, 1, 5);

    CheckType(String name, Integer timesBeforeBan, Integer timePeriod, Integer notifyInterval) {
        this.name = name;
        this.timePeriod = timePeriod;
        this.timesBeforeBan = timesBeforeBan;
        this.notifyInterval = notifyInterval;
    }

    private String name;
    private Integer timesBeforeBan, timePeriod, notifyInterval;

    public String getName() {
        return name;
    }

    public Integer getTimesBeforeBan() {
        return timesBeforeBan;
    }

    public Integer getTimePeriod() {
        return timePeriod;
    }

    public Integer getNotifyInterval() {
        return notifyInterval;
    }
}
