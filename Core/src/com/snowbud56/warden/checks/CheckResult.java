package com.snowbud56.warden.checks;

/*
* Created by snowbud56 on March 17, 2018
* Do not change or use this code without permission
*/

public class CheckResult {

    private Level level;
    private String message;
    private CheckType type;

    public CheckResult(Level level, String message, CheckType type) {
        this.level = level;
        this.message = message;
        this.type = type;
    }

    public Level getLevel() {
        return level;
    }

    public String getMessage() {
        return message;
    }

    public CheckType getType() {
        return type;
    }

    public boolean failed() {
        return level != Level.PASSED;
    }
}
