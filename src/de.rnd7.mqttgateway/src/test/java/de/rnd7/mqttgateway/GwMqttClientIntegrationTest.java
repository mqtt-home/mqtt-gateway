package de.rnd7.mqttgateway;

import com.hivemq.testcontainer.junit5.HiveMQTestContainerExtension;
import de.rnd7.mqttgateway.config.ConfigMqtt;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.testcontainers.utility.DockerImageName;

import java.net.URISyntaxException;
import java.time.Duration;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GwMqttClientIntegrationTest {
    @RegisterExtension
    final public HiveMQTestContainerExtension extension
        = new HiveMQTestContainerExtension(DockerImageName.parse("hivemq/hivemq-ce:2021.3"));

    @Test
    void test_connect() throws URISyntaxException {
        final GwMqttClient client = GwMqttClient.start(new ConfigMqtt()
            .setUrl(String.format("tcp://%s:%s",
                this.extension.getHost(),
                this.extension.getMqttPort())));

        awaitConnected(client);
        client.shutdown();
        awaitDisconnected(client);
    }

    private void awaitConnected(final GwMqttClient client) {
        await().atMost(Duration.ofSeconds(2)).until(client::isConnected);
        assertTrue(client.isConnected());
    }

    private void awaitDisconnected(final GwMqttClient client) {
        await().atMost(Duration.ofSeconds(2)).until(() -> !client.isConnected());
        assertFalse(client.isConnected());
    }

    @Test
    void test_gateway_info() throws URISyntaxException {
        final String host = this.extension.getHost();
        final int port = this.extension.getMqttPort();

        final GwMqttClient client = GwMqttClient.start(new ConfigMqtt()
            .setUrl(String.format("tcp://%s:%s",
                host,
                port))
            .setBridgeInfoTopic("bridge"));

        final GwMqttClient otherClient = GwMqttClient.start(new ConfigMqtt()
            .setUrl(String.format("tcp://%s:%s",
                host,
                port))
            .setBridgeInfoTopic("bridge"));
        otherClient.subscribe("#");

        final MessageConsumer consumer = new MessageConsumer();
        Events.register(consumer);

        awaitConnected(client);
        // client.subscribe("#");
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

    @Test
    void test_subscribe_event() throws URISyntaxException {
        final String host = this.extension.getHost();
        final int port = this.extension.getMqttPort();

        final GwMqttClient client = GwMqttClient.start(new ConfigMqtt()
            .setUrl(String.format("tcp://%s:%s",
                host,
                port)));

        awaitConnected(client);

        Events.post(new SubscribeTopic("test/#"));
        final MessageConsumer consumer = new MessageConsumer();
        Events.register(consumer);
        Events.post(PublishMessage.absolute("test", "value"));

        final Message msg = consumer.awaitCount(1).get(0);
        assertEquals("test", msg.getTopic());
        assertEquals("value", msg.getRaw());

        consumer.clear();
        client.shutdown();
    }

}
