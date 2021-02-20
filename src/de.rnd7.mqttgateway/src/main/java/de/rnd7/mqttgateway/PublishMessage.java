package de.rnd7.mqttgateway;

import com.google.common.base.Objects;

import java.util.Optional;

public final class PublishMessage {
    private final String topic;
    private final String message;
    private final boolean relativeTopic;
    private Boolean retain = null;

    private PublishMessage(final String topic, final String message, final boolean relativeTopic) {
        this.topic = topic;
        this.message = message;
        this.relativeTopic = relativeTopic;
    }

    public PublishMessage setRetain(final boolean retain) {
        this.retain = retain;
        return this;
    }

    public Optional<Boolean> getRetain() {
        return Optional.ofNullable(this.retain);
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
        final PublishMessage message1 = (PublishMessage) o;
        return this.relativeTopic == message1.relativeTopic && Objects.equal(this.topic, message1.topic) && Objects.equal(this.message, message1.message) && Objects.equal(this.retain, message1.retain);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.topic, this.message, this.relativeTopic, this.retain);
    }
}
