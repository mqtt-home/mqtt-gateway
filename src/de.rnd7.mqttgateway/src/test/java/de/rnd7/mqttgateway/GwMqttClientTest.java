package de.rnd7.mqttgateway;

import com.hivemq.testcontainer.junit5.HiveMQTestContainerExtension;
import de.rnd7.mqttgateway.config.ConfigMqtt;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.time.Duration;

import static org.awaitility.Awaitility.await;
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

        await().atMost(Duration.ofSeconds(2)).until(client::isConnected);
        assertTrue(client.isConnected());
        client.shutdown();
        await().atMost(Duration.ofSeconds(2)).until(() -> !client.isConnected());
        assertFalse(client.isConnected());
    }
}
