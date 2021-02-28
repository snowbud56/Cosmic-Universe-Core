package com.snowbud56.warden.util;

/*
* Created by snowbud56 on March 17, 2018
* Do not change or use this code without permission
*/

public class WACSettings {

    public static Boolean WACEnabled = true;
    public static Boolean WACBanning = false;

    public static final Integer SPEED_IGNORE_SINCE_VELOCITY = 1;
    public static final Double MAX_XZ_SPEED = 0.66D;
    public static final Double MAX_XZ_SPEED_MULTIPLIER = 0.055D;

    public static final Long BOW_MIN = 100L;
    public static final Long FOOD_MIN = 1000L;

    public static final Long MIN_HEAL_DELAY = 325L;
    public static final Long MIN_HEAL_DELAY_REGEN_1 = 2400L;
    public static final Long MIN_HEAL_DELAY_REGEN_2 = 1150L;
    public static final Long MIN_HEAL_DELAY_REGEN_3 = 550L;
    public static final Long MIN_HEAL_DELAY_REGEN_4 = 250L;
    public static final Long MIN_HEAL_DELAY_REGEN_5 = 145L;
    public static final Long MIN_HEAL_DELAY_REGEN_MAX = 45L;

    public static final Integer TICKS_BEFORE_NEXT_MOVE = 40;

    public static final Long FLY_IGNORE_AFTER_JUMP = 750L;
    public static final Double FLY_MIN_Y_SPEED_FALL = 0.49D;

}