package de.rnd7.mqttgateway.config;

import java.time.Duration;
import java.time.ZonedDateTime;

public class ConfigApp {
    private Duration duration;

    private ZonedDateTime date;

    public Duration getDuration() {
        return this.duration;
    }

    public ConfigApp setDuration(final Duration duration) {
        this.duration = duration;
        return this;
    }

    public ZonedDateTime getDate() {
        return this.date;
    }

    public ConfigApp setDate(final ZonedDateTime date) {
        this.date = date;
        return this;
    }
}
