package de.rnd7.mqttgateway.config;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConfigMqttTest {

    @Test
    void test_default_values() {
        final ConfigMqtt config = new ConfigMqtt()
            .setDefaultTopic("default-topic");

        assertNull(config.getUrl());
        assertFalse(config.getUsername().isPresent());
        assertFalse(config.getPassword().isPresent());
        assertEquals(1, config.getQos());
        assertTrue(config.isRetain());
        assertEquals("default-topic", config.getTopic());
        assertTrue(config.isPublishBridgeInfo());
        assertFalse(config.isDeduplicate());
        assertFalse(config.getBridgeInfoTopic().isPresent());
    }

    @Test
    void test_set_get() {
        final ConfigMqtt config = new ConfigMqtt()
            .setUrl("tcp://localhost:1883")
            .setUsername("user")
            .setPassword("pass")
            .setQos(2)
            .setRetain(false)
            .setTopic("topic")
            .setBridgeInfo(false)
            .setDeduplicate(true)
            .setBridgeInfoTopic("bridge");

        assertEquals("tcp://localhost:1883", config.getUrl());
        assertEquals("user", config.getUsername().orElseThrow(IllegalStateException::new));
        assertEquals("pass", config.getPassword().orElseThrow(IllegalStateException::new));
        assertEquals(2, config.getQos());
        assertFalse(config.isRetain());
        assertEquals("topic", config.getTopic());
        assertFalse(config.isPublishBridgeInfo());
        assertTrue(config.isDeduplicate());
        assertEquals("bridge", config.getBridgeInfoTopic().orElseThrow(IllegalStateException::new));
    }
    @Test
    void test_set_default_topic() {
        final ConfigMqtt config = new ConfigMqtt();

        config.setDefaultTopic("default-topic");
        assertEquals("default-topic", config.getTopic());

        config.setTopic("topic");
        assertEquals("topic", config.getTopic());

        config.setDefaultTopic("default-topic");
        assertEquals("topic", config.getTopic());
    }
}
