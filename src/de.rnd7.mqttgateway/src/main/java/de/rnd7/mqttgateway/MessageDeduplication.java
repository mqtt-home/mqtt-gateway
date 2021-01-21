package de.rnd7.mqttgateway;

import java.util.HashMap;
import java.util.Map;

class MessageDeduplication {
    private final Map<String, String> messageCache = new HashMap<>();

    private final boolean deduplicate;
    private final String baseTopic;

    MessageDeduplication(final boolean deduplicate, final String baseTopic) {
        this.deduplicate = deduplicate;
        this.baseTopic = baseTopic;
    }

    boolean apply(final PublishMessage message) {
        final String topic = message.getTopic(this.baseTopic);
        final String valueString = message.getMessage();

        if (this.deduplicate) {
            if (valueString.equals(this.messageCache.get(topic))) {
                return false;
            } else {
                this.messageCache.put(topic, valueString);
            }
        }

        return true;
    }
}
