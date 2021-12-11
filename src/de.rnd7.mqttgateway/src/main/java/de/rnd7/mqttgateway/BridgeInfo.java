package de.rnd7.mqttgateway;

import java.util.Locale;

public enum BridgeInfo {
    UNKNOWN,
    ONLINE,
    OFFLINE;

    String toMessage() {
        return name().toLowerCase(Locale.ROOT);
    }
}
