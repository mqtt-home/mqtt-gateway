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

        final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Duration.class, new DurationDeserializer())
            .create();

        return gson.fromJson(json, type);
    }
}
