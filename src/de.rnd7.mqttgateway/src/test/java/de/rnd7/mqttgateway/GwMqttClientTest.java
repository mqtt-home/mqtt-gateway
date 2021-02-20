package de.rnd7.mqttgateway;

import de.rnd7.mqttgateway.config.ConfigMqtt;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class GwMqttClientTest {
    @Test
    void test_url_not_set() {
        final ConfigMqtt config = new ConfigMqtt();
        assertThrows(IllegalArgumentException.class, () -> GwMqttClient.start(config));
    }
}
