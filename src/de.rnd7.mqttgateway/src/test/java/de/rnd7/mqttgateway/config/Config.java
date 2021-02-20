package de.rnd7.mqttgateway.config;

public class Config {
    private final ConfigMqtt mqtt = new ConfigMqtt();
    private final ConfigApp app = new ConfigApp();

    public ConfigMqtt getMqtt() {
        return this.mqtt;
    }

    public ConfigApp getApp() {
        return this.app;
    }
}
