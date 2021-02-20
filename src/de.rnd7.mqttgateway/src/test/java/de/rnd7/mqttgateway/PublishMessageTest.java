package de.rnd7.mqttgateway;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PublishMessageTest {

    @Test
    void test_equals_contract() {
        EqualsVerifier.forClass(PublishMessage.class)
            .suppress(Warning.NONFINAL_FIELDS)
            .verify();
    }

    @Test
    void test_retain() {
        final PublishMessage message = PublishMessage.absolute("topic", "msg");

        assertFalse(message.getRetain().orElse(false));
        assertFalse(message.setRetain(false).getRetain().orElse(false));
        assertTrue(message.setRetain(true).getRetain().orElse(false));
        assertEquals("topic", message.getTopic("base"));
    }

    @Test
    void test_relative_message() {
        final PublishMessage message = PublishMessage.relative("some", "msg");
        assertEquals("base/some", message.getTopic("base"));
        assertEquals("msg", message.getMessage());
    }
}
