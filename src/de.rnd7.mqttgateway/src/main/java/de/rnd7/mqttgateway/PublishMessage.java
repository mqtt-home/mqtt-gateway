package de.rnd7.mqttgateway;

import java.util.Objects;

public class PublishMessage {
    private final String topic;
    private final String message;
    private final boolean relativeTopic;

    private PublishMessage(final String topic, final String message, final boolean relativeTopic) {
        this.topic = topic;
        this.message = message;
        this.relativeTopic = relativeTopic;
    }

    public static PublishMessage relative(final String topic, final String message) {
        return new PublishMessage(topic, message, true);
    }

    public static PublishMessage absolute(final String topic, final String message) {
        return new PublishMessage(topic, message, false);
    }

    public String getTopic() {
        return this.topic;
    }

    public String getTopic(final String baseTopic) {
        if (isRelativeTopic()) {
            return baseTopic + "/" + getTopic();
        }
        else {
            return getTopic();
        }
    }

    public String getMessage() {
        return this.message;
    }

    private boolean isRelativeTopic() {
        return this.relativeTopic;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final PublishMessage message = (PublishMessage) o;
        return Objects.equals(this.topic, message.topic) &&
            Objects.equals(this.message, message.message) &&
            Objects.equals(this.relativeTopic, message.relativeTopic);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.topic, this.message, this.relativeTopic);
    }
}
