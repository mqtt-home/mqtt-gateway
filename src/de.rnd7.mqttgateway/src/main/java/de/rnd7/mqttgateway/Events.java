package de.rnd7.mqttgateway;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Events {
    private final static Events events = new Events();
    private final Executor executor = Executors.newFixedThreadPool(4);
    private final AsyncEventBus eventBus = new AsyncEventBus("mqtt", this.executor);

    private Events() {

    }

    public static Events getInstance() {
        return events;
    }

    public static void register(final Object object) {
        events.eventBus.register(object);
    }

    public static void post(final Object object) {
        events.eventBus.post(object);
    }

    public static EventBus getBus() {
        return events.eventBus;
    }

    public static void unregister(final Object object) {
        events.eventBus.unregister(object);
    }
}
