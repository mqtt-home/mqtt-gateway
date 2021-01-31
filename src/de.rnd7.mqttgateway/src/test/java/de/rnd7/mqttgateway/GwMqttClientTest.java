package de.rnd7.mqttgateway;

import com.hivemq.client.mqtt.mqtt5.Mqtt5BlockingClient;
import com.hivemq.client.mqtt.mqtt5.Mqtt5Client;
import com.hivemq.testcontainer.junit5.HiveMQTestContainerExtension;
import de.rnd7.mqttgateway.config.ConfigMqtt;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GwMqttClientTest {
    @RegisterExtension
    final public HiveMQTestContainerExtension extension
        = new HiveMQTestContainerExtension("hivemq/hivemq-ce", "2020.6");

    @Test
    public void test_connect() {
        final GwMqttClient client = GwMqttClient.start(new ConfigMqtt()
            .setUrl(String.format("tcp://%s:%s", this.extension.getHost(), this.extension.getMqttPort())));

        assertTrue(client.isConnected());
        client.shutdown();
        assertFalse(client.isConnected());

//        final Mqtt5BlockingClient client = Mqtt5Client.builder()
//            .serverHost()
//            .serverPort(this.extension.getMqttPort())
//            .buildBlocking();

//        client.connect();
//        client.disconnect();
    }
}
