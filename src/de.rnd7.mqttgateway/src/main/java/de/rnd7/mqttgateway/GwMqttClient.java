package de.rnd7.mqttgateway;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.eventbus.Subscribe;

public class GwMqttClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(GwMqttClient.class);

    public static final String STATE = "bridge/state";
    private final String defaultClientId;

    private final MemoryPersistence persistence = new MemoryPersistence();
    private final Object mutex = new Object();
    private final ConfigMqtt config;

    private Optional<MqttClient> client;

    private final MessageDeduplication deduplication;
    private final List<String> subscriptions = new ArrayList<>();

    private GwMqttClient(final ConfigMqtt config) {
        this.config = config;
        this.defaultClientId = UUID.randomUUID().toString();
        this.client = this.connect();
        this.deduplication = new MessageDeduplication(config.isDeduplicate(), config.getTopic());
    }

    public static GwMqttClient start(final ConfigMqtt config) {
        final GwMqttClient client = new GwMqttClient(config);
        Events.register(client);

        registerOfflineHook(client);

        return client;
    }

    private static void registerOfflineHook(final GwMqttClient mqttClient) {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                mqttClient.shutdown();
            }
        });
    }

    public void subscribe(final String topic) {
        this.subscriptions.add(topic);
        this.client.ifPresent(client -> {
            try {
                client.subscribe(topic);
            } catch (final MqttException e) {
                LOGGER.error("Error subscribing to topic {}", topic, e);
            }
        });
    }

    private Optional<MqttClient> connect() {
        try {
            LOGGER.info("Connecting MQTT client");
            final MqttClient result = new MqttClient(this.config.getUrl(),
                this.config.getClientId().orElse(this.defaultClientId),
                this.persistence);

            result.setCallback(new EventBusMessageHandler());

            final MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);

            initAuthentication(connOpts);

            result.connect(connOpts);
            LOGGER.info("MQTT client connected");

            if (!this.subscriptions.isEmpty()) {
                result.subscribe(this.subscriptions.toArray(new String[0]));
                LOGGER.info("MQTT subscriptions: {}", this.subscriptions);
            }

            return Optional.of(result);
        } catch (final MqttException e) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug(e.getMessage(), e);
            } else {
                LOGGER.error("{} {}",
                    e.getMessage(),
                    Optional.ofNullable(e.getCause()).map(Throwable::getMessage).orElse("No cause."));
            }

            return Optional.empty();
        }
    }

    private void initAuthentication(final MqttConnectOptions connOpts) {
        this.config.getUsername().ifPresent(connOpts::setUserName);
        this.config.getPassword().map(String::toCharArray).ifPresent(connOpts::setPassword);
    }

    private void publish(final String topic, final String value) {
        synchronized (this.mutex) {
            LOGGER.debug("publishing {} = {}", topic, value);

            if (!this.client.filter(MqttClient::isConnected).isPresent()) {
                this.client = this.connect();
            }

            this.client.ifPresent(mqttClient -> {
                try {
                    final MqttMessage message = new MqttMessage(value.getBytes());
                    message.setQos(this.config.getQos());
                    message.setRetained(this.config.isRetain());
                    mqttClient.publish(topic, message);
                } catch (final MqttException e) {
                    LOGGER.error(e.getMessage(), e);
                }
            });
        }
    }

    @Subscribe
    public void publish(final PublishMessage message) {
        if (!this.deduplication.apply(message)) {
            return;
        }

        final String topic = message.getTopic(this.config.getTopic());
        final String valueString = message.getMessage();
        this.publish(topic, valueString);
    }

    @Subscribe
    public void onBridgInfo(final BridgInfo info) {
        publishBridgeInfo(info.name());
    }

    private void publishBridgeInfo(final String info) {
        if (this.config.isPublishBridgeInfo()) {
            publish(PublishMessage.relative(STATE, info));
        }
    }

    public void online() {
        onBridgInfo(BridgInfo.online);
    }

    public void shutdown() {
        this.client.ifPresent(c -> {
            try {
                onBridgInfo(BridgInfo.offline);

                c.disconnect();
                c.close();
            } catch (final MqttException e) {
                LOGGER.debug(e.getMessage(), e);
            }
        });
    }

}
