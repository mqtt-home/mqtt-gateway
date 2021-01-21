package de.rnd7.mqttgateway;

public class TopicCleaner {
    private TopicCleaner() {

    }

    public static String clean(final String topic) {
        return topic
            .replace(" ", "-").toLowerCase()
            .replace("ä", "ae")
            .replace("ö", "oe")
            .replace("ü", "ue");
    }
}
