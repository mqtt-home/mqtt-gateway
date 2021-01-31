package de.rnd7.mqttgateway;

import com.google.common.eventbus.Subscribe;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static org.awaitility.Awaitility.await;

public class MessageConsumer {

    private final List<Message> messages = new ArrayList<>();

    @Subscribe
    public void onMessage(final Message message) {
        this.messages.add(message);
    }

    public void clear() {
        this.messages.clear();
    }

    public List<Message> getMessages() {
        return this.messages;
    }

    public List<Message> awaitCount(final int count) {
        await().atMost(Duration.ofSeconds(10))
            .until(() -> this.messages.size() == count);

        return this.messages.subList(0, count);
    }
}
