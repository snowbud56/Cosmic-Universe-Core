package com.snowbud56.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtil {
    public static String convertmstoTime(long ms) {
        if (ms > 86400000) {
            return Math.round((((double) ms / (double) 86400000) * 100.0) / 10.0) / 10.0 + " days";
        } else if (ms > 3600000) {
            return Math.round((((double) ms / (double) 3600000) * 100.0) / 10.0) / 10.0 + " hours";
        } else if (ms > 60000) {
            return Math.round((((double) ms / (double) 60000) * 100.0) / 10.0) / 10.0 + " minutes";
        } else {
            return Math.round((((double) ms / (double) 1000) * 100.0) / 10.0) / 10.0 + " seconds";
        }
    }

    public static String getDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }
}
