package de.rnd7.mqttgateway;

public class TopicCleaner {
    private TopicCleaner() {

    }

    public static String clean(final String topic) {
        return topic
            .toLowerCase()
            .replace(" ", "-")
            .replace("ä", "ae")
            .replace("ö", "oe")
            .replace("ü", "ue");
    }
}
