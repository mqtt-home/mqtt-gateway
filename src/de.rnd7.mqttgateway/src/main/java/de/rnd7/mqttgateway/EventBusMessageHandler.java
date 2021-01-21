package de.rnd7.mqttgateway;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class EventBusMessageHandler implements MqttCallback {
    private static final Logger LOGGER = LoggerFactory.getLogger(EventBusMessageHandler.class);

    @Override
    public void connectionLost(final Throwable cause) {
        LOGGER.error(cause.getMessage(), cause);
    }

    @Override
    public void messageArrived(final String topic, final MqttMessage message) throws Exception {
        try {
            Events.post(new Message(topic, new String(message.getPayload())));
        } catch (final Exception e) {
            LOGGER.trace(e.getMessage(), e);
        }
    }

    @Override
    public void deliveryComplete(final IMqttDeliveryToken token) {
        // do nothing
    }
}
