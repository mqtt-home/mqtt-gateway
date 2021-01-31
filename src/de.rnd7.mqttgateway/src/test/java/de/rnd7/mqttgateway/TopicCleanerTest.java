package de.rnd7.mqttgateway;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class TopicCleanerTest {

    @ParameterizedTest
    @CsvSource(value = {"Küche/licht,kueche/licht", "Ä,ae", "ö,oe"})
    void test(final String topic, final String expected) {
    	assertEquals(expected, TopicCleaner.clean(topic));
    }
}
