package de.rnd7.mqttgateway;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MessageDeduplicationTest {
    private final PublishMessage message = PublishMessage.absolute("topic", "message");
    @Test
    void test_no_deduplication() {
        final MessageDeduplication deduplication = new MessageDeduplication(false, "topic");
        assertTrue(deduplication.apply(this.message));
        assertTrue(deduplication.apply(this.message));
    }

    @Test
    void test_deduplication() {
        final MessageDeduplication deduplication = new MessageDeduplication(true, "topic");
        assertTrue(deduplication.apply(this.message));
        assertFalse(deduplication.apply(this.message));
    }
}
