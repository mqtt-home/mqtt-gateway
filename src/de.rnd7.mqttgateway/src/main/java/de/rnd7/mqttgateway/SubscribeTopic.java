package de.rnd7.mqttgateway;

public class SubscribeTopic {
    private final String topic;

    public SubscribeTopic(final String topic) {
        this.topic = topic;
    }

    public String getTopic() {
        return topic;
    }
}
