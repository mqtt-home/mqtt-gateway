package de.rnd7.mqttgateway;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Events {
    private final static Events events = new Events();
    private final Executor executor = Executors.newFixedThreadPool(4);
    private AsyncEventBus eventBus = new AsyncEventBus("mqtt", this.executor);

    private Events() {

    }

    public static Events getInstance() {
        return events;
    }

    /**
     * Call this method for test purpose only.
     * This will create a new event bus and can be used to isolate test cases.
     */
    public void resetBus() {
        this.eventBus = new AsyncEventBus("mqtt", this.executor);
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
