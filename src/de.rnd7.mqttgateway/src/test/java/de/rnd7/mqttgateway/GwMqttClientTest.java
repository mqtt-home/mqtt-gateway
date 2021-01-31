package de.rnd7.mqttgateway;

import com.hivemq.testcontainer.junit5.HiveMQTestContainerExtension;
import de.rnd7.mqttgateway.config.ConfigMqtt;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.time.Duration;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GwMqttClientTest {
    @RegisterExtension
    final public HiveMQTestContainerExtension extension
        = new HiveMQTestContainerExtension("hivemq/hivemq-ce", "2020.6");

    @Test
    public void test_connect() throws URISyntaxException {
        final GwMqttClient client = GwMqttClient.start(new ConfigMqtt()
            .setUrl(String.format("tcp://%s:%s",
                this.extension.getHost(),
                this.extension.getMqttPort())));

        awaitConnected(client);
        client.shutdown();
        await().atMost(Duration.ofSeconds(2)).until(() -> !client.isConnected());
        assertFalse(client.isConnected());
    }

    private void awaitConnected(final GwMqttClient client) {
        await().atMost(Duration.ofSeconds(2)).until(client::isConnected);
        assertTrue(client.isConnected());
    }

    @Test
    void test_gateway_info() throws URISyntaxException {
        final GwMqttClient client = GwMqttClient.start(new ConfigMqtt()
            .setUrl(String.format("tcp://%s:%s",
                this.extension.getHost(),
                this.extension.getMqttPort()))
            .setBridgeInfoTopic("bridge"));

        final MessageConsumer consumer = new MessageConsumer();
        Events.register(consumer);

        awaitConnected(client);
        client.subscribe("#");
        client.online();

        final Message online = consumer.awaitCount(1).get(0);
        assertEquals("bridge", online.getTopic());
        assertEquals("online", online.getRaw());

        consumer.clear();
        client.shutdown();

        final Message offline = consumer.awaitCount(1).get(0);
        assertEquals("bridge", offline.getTopic());
        assertEquals("offline", offline.getRaw());
    }
}
