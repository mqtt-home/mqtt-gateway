package de.rnd7.mqttgateway;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import com.hivemq.client.mqtt.datatypes.MqttQos;
import com.hivemq.client.mqtt.lifecycle.MqttClientAutoReconnect;
import com.hivemq.client.mqtt.lifecycle.MqttClientConnectedContext;
import com.hivemq.client.mqtt.lifecycle.MqttClientDisconnectedContext;
import com.hivemq.client.mqtt.mqtt3.Mqtt3AsyncClient;
import com.hivemq.client.mqtt.mqtt3.Mqtt3Client;
import com.hivemq.client.mqtt.mqtt3.Mqtt3ClientBuilder;
import com.hivemq.client.mqtt.mqtt3.message.auth.Mqtt3SimpleAuth;
import com.hivemq.client.mqtt.mqtt3.message.connect.Mqtt3Connect;
import com.hivemq.client.mqtt.mqtt3.message.connect.Mqtt3ConnectBuilder;
import com.hivemq.client.mqtt.mqtt3.message.publish.Mqtt3Publish;
import com.hivemq.client.mqtt.mqtt3.message.subscribe.Mqtt3Subscribe;
import com.hivemq.client.mqtt.mqtt3.message.subscribe.Mqtt3Subscription;
import com.hivemq.client.mqtt.mqtt3.message.subscribe.suback.Mqtt3SubAck;
import de.rnd7.mqttgateway.config.ConfigMqtt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.eventbus.Subscribe;

public class GwMqttClient {
    private static final Logger logger = LoggerFactory.getLogger(GwMqttClient.class);

    public static final String STATE = "bridge/state";

    private final Object mutex = new Object();
    private final ConfigMqtt config;

    private final Mqtt3AsyncClient client;

    private final MessageDeduplication deduplication;
    private final AtomicBoolean connected = new AtomicBoolean(false);
    private final AtomicBoolean expectDisconnect = new AtomicBoolean(false);

    private GwMqttClient(final ConfigMqtt config) throws URISyntaxException {
        this.config = config;
        this.client = this.connect();
        this.deduplication = new MessageDeduplication(config.isDeduplicate(), config.getTopic());
    }

    public boolean isConnected() {
        return this.connected.get();
    }

    public static GwMqttClient start(final ConfigMqtt config) throws URISyntaxException {
        if (config.getUrl() == null) {
            throw new IllegalArgumentException("MQTT broker URL must be set in the configuration.");
        }

        final GwMqttClient client = new GwMqttClient(config);
        Events.register(client);

        return client;
    }

    @Subscribe
    public void subscribe(final SubscribeTopic topic) {
        subscribe(topic.getTopic());
    }

    public GwMqttClient subscribe(final String topic) {
        final CompletableFuture<Mqtt3SubAck> subscribe = this.client.subscribe(Mqtt3Subscribe.builder().addSubscription(this.topicFilter(topic)).build(),
            this::onMessage);

        try {
            subscribe.get(5, TimeUnit.SECONDS);
        } catch (final InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (final Exception e) {
            logger.error("Subscription not successful within the given time.", e);
        }

        return this;
    }

    private void onConnected(final MqttClientConnectedContext context) {
        logger.info("MQTT client connected");
        this.connected.set(true);

        if (config.isPublishBridgeInfo() && config.isAutoPublish()) {
            online();
        }
    }

    private void onDisconnected(final MqttClientDisconnectedContext context) {
        if (!expectDisconnect.get()) {
            logger.error("MQTT client disconnected: {}", context.getCause().getMessage(), context.getCause());
        }
        this.connected.set(false);
    }

    private void onMessage(final Mqtt3Publish message) {
        Events.post(new Message(message.getTopic().toString(),
            new String(message.getPayloadAsBytes())));
    }

    private Mqtt3AsyncClient connect() throws URISyntaxException {
        logger.info("Connecting MQTT client");
        expectDisconnect.set(false);

        final URI uri = new URI(this.config.getUrl());

        final Mqtt3ClientBuilder mqtt3ClientBuilder = Mqtt3Client.builder()
            .serverHost(uri.getHost())
            .serverPort(uri.getPort())
            .addConnectedListener(this::onConnected)
            .addDisconnectedListener(this::onDisconnected)
            .automaticReconnect(MqttClientAutoReconnect.builder().build());

        final Mqtt3AsyncClient result = registerWill(mqtt3ClientBuilder);

        final Mqtt3Connect mqtt3Connect = initAuthentication()
            .cleanSession(true)
            .build();

        result.connect(mqtt3Connect);

        return result;
    }

    private Mqtt3AsyncClient registerWill(final Mqtt3ClientBuilder mqtt3ClientBuilder) {
        if (this.config.isPublishBridgeInfo()) {
            final String info = BridgeInfo.OFFLINE.toMessage();
            final Optional<String> topic = this.config.getBridgeInfoTopic();
            final PublishMessage message = topic.map(s -> PublishMessage.absolute(s, info))
                .orElseGet(() -> PublishMessage.relative(STATE, info));

            final String valueString = message.getMessage();

            return mqtt3ClientBuilder
                .willPublish()
                .topic(message.getTopic(this.config.getTopic()))
                .payload(valueString.getBytes(StandardCharsets.UTF_8)).applyWillPublish()
                .buildAsync();

        }
        return mqtt3ClientBuilder.buildAsync();
    }

    private Mqtt3Subscription topicFilter(final String filter) {
        return Mqtt3Subscription.builder().topicFilter(filter).build();
    }

    private Mqtt3ConnectBuilder initAuthentication() {
        final Mqtt3ConnectBuilder result = Mqtt3Connect.builder();
        final Optional<String> user = this.config.getUsername();
        final Optional<String> pass = this.config.getPassword();
        if (user.isPresent() && pass.isPresent()) {
            final Mqtt3SimpleAuth auth = Mqtt3SimpleAuth.builder()
                .username(user.get())
                .password(pass.get().getBytes(StandardCharsets.UTF_8))
                .build();

            return result.simpleAuth(auth);
        }
        return result;
    }

    private void publish(final String topic, final String value, final boolean retain) {
        synchronized (this.mutex) {
            if (this.connected.get()) {
                logger.debug("publishing {} = {}", topic, value);
            }
            else {
                logger.error("cannot publish, not connected. {} = {}", topic, value);
            }

            final Mqtt3Publish publish = Mqtt3Publish.builder()
                .topic(topic)
                .payload(value.getBytes(StandardCharsets.UTF_8))
                .qos(qos())
                .retain(retain)
                .build();

            this.client.publish(publish);
        }
    }

    private MqttQos qos() {
        switch (this.config.getQos()) {
            case 0:
                return MqttQos.AT_MOST_ONCE;
            case 1:
                return MqttQos.AT_LEAST_ONCE;
            default:
                return MqttQos.EXACTLY_ONCE;
        }
    }

    @Subscribe
    public void publish(final PublishMessage message) {
        if (!this.deduplication.apply(message)) {
            return;
        }

        final String topic = message.getTopic(this.config.getTopic());
        final String valueString = message.getMessage();
        this.publish(topic, valueString, message.getRetain().orElse(this.config.isRetain()));
    }

    @Subscribe
    public void onBridgInfo(final BridgeInfo info) {
        publishBridgeInfo(info.toMessage());
    }

    private void publishBridgeInfo(final String info) {
        if (this.config.isPublishBridgeInfo()) {
            final Optional<String> topic = this.config.getBridgeInfoTopic();

            if (topic.isPresent()) {
                publish(PublishMessage.absolute(topic.get(), info));
            }
            else {
                publish(PublishMessage.relative(STATE, info));
            }
        }
    }

    public GwMqttClient online() {
        onBridgInfo(BridgeInfo.ONLINE);
        return this;
    }

    public void shutdown() {
        expectDisconnect.set(true);
        onBridgInfo(BridgeInfo.OFFLINE);
        this.client.disconnect();
    }

}
