package de.fyreum.customitemsxl.logger;

import de.erethon.commons.misc.NumberUtil;

public enum DebugMode {
    OFF(0), // no debug info
    LOW(1), // some debug info
    MEDIUM(2), // normal debug info
    HIGH(3), // lots of debug info
    EXTENDED(4), // extended and longer debug info
    TOO_MUCH(5), // too much info xD
    ;

    private final int value;

    DebugMode(int value) {
        this.value = value;
    }

    public boolean atLeast(DebugMode mode) {
        return value >= mode.getValue();
    }

    public boolean atLeast(int v) {
        return value >= v;
    }

    public int getValue() {
        return value;
    }

    public static DebugMode getByString(String s) {
        DebugMode byName = getByName(s);
        return byName != null ? byName : getByValue(NumberUtil.parseInt(s, -1));
    }

    public static DebugMode getByName(String s) {
        for (DebugMode mode : values()) {
            if (mode.name().equalsIgnoreCase(s)) {
                return mode;
            }
        }
        return null;
    }

    public static DebugMode getByValue(int v) {
        for (DebugMode mode : values()) {
            if (mode.getValue() == v) {
                return mode;
            }
        }
        return null;
    }
}
