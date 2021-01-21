package de.rnd7.mqttgateway;

public class Message {
    private final String raw;
    private final String topic;

    public Message(final String topic, final String raw) {
        this.topic = topic;
        this.raw = raw;
    }

    public String getRaw() {
        return this.raw;
    }

    public String getTopic() {
        return this.topic;
    }
}
