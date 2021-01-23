package de.rnd7.mqttgateway.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.ZonedDateTime;

public class ConfigParser {

    private ConfigParser() {
    }

    public static <T> T parse(final File file, final Class<T> type) throws IOException {
        try (final InputStream in = new FileInputStream(file)) {
            return parse(in, type);
        }
    }

    public static <T> T parse(final InputStream in, final Class<T> type) throws IOException {
        final String json = IOUtils.toString(in, StandardCharsets.UTF_8);

        return buildGson().fromJson(json, type);
    }

    public static Gson buildGson() {
        return new GsonBuilder()
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .registerTypeAdapter(ZonedDateTime.class, new ZonedDateTimeAdapter())
            .setPrettyPrinting()
            .create();
    }
}
