package de.rnd7.mqttgateway;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EventsTest {
    @Test
    void test_sync_bus() {
    	Events.getInstance().syncBus();
        final MessageConsumer consumer = new MessageConsumer();
        Events.register(consumer);
        Events.post(new Message("topic", "msg"));
        final List<Message> list = consumer.getMessages();
        assertEquals(1, list.size());
        assertEquals("topic", list.get(0).getTopic());
        assertEquals("msg", list.get(0).getRaw());
        Events.getInstance().resetBus();
    }

    @Test
    void test_unregister() {
        Events.getInstance().syncBus();
        final MessageConsumer consumer = new MessageConsumer();
        Events.register(consumer);
        Events.unregister(consumer);
        Events.post(new Message("topic", "msg"));
        final List<Message> list = consumer.getMessages();
        assertTrue(list.isEmpty());
        Events.getInstance().resetBus();
    }
}
