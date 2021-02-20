package de.rnd7.mqttgateway.config;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDate;
import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ConfigParserTest {
    @Test
    void test_parse() throws Exception {
        final URL url = ConfigParserTest.class.getResource("example.json");

        final Config config = ConfigParser.parse(Paths.get(url.toURI()).toFile(), Config.class);
        assertEquals("tcp://192.168.2.98:1883", config.getMqtt().getUrl());
        assertEquals("user", config.getMqtt().getUsername().orElseThrow(IllegalStateException::new));
        assertEquals("password", config.getMqtt().getPassword().orElseThrow(IllegalStateException::new));
        assertFalse(config.getMqtt().isDeduplicate());

        Assertions.assertEquals(1234000, config.getApp().getDuration().toMillis());
        assertEquals(ZonedDateTime.parse("2021-02-20T07:48:39.389776+01:00[Europe/Berlin]"), config.getApp().getDate());
    }

    @Test
    void test_serialize() throws IOException {
    	final Config config = new Config();
    	config.getApp()
            .setDate(ZonedDateTime.now())
            .setDuration(Duration.ofSeconds(60));

        final String json = ConfigParser.buildGson().toJson(config);
        try (final InputStream in = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8))) {
            final Config parsed = ConfigParser.parse(in, Config.class);
            assertEquals(config.getApp().getDate(), parsed.getApp().getDate());
            assertEquals(config.getApp().getDuration(), parsed.getApp().getDuration());
        }

    }
}
