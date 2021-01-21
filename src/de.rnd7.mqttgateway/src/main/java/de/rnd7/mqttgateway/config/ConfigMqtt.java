package de.rnd7.mqttgateway.config;

import com.google.gson.annotations.SerializedName;

import java.util.Optional;

public class ConfigMqtt {
    @SerializedName("url")
    private String url;

    @SerializedName("username")
    private String username;

    @SerializedName("password")
    private String password;

    @SerializedName("retain")
    private boolean retain = true;

    @SerializedName("topic")
    private String topic;

    @SerializedName("client-id")
    private String clientId;

    @SerializedName("qos")
    private int qos = 2;

    @SerializedName("bridge-info")
    private boolean bridgeInfo = true;

    @SerializedName("deduplicate")
    private boolean deduplicate = true;

    public String getUrl() {
        return this.url;
    }

    public ConfigMqtt setUrl(final String url) {
        this.url = url;
        return this;
    }

    public Optional<String> getUsername() {
        return Optional.ofNullable(this.username);
    }

    public ConfigMqtt setUsername(final String username) {
        this.username = username;
        return this;
    }

    public Optional<String> getPassword() {
        return Optional.ofNullable(this.password);
    }

    public ConfigMqtt setPassword(final String password) {
        this.password = password;
        return this;
    }

    public ConfigMqtt setClientId(final String clientId) {
        this.clientId = clientId;
        return this;
    }

    public Optional<String> getClientId() {
        return Optional.ofNullable(this.clientId);
    }

    public ConfigMqtt setDefaultClientId(final String clientId) {
        if (this.clientId == null) {
            this.clientId = clientId;
        }

        return this;
    }

    public int getQos() {
        return this.qos;
    }

    public ConfigMqtt setQos(final int qos) {
        this.qos = qos;
        return this;
    }

    public boolean isRetain() {
        return this.retain;
    }

    public ConfigMqtt setRetain(final boolean retain) {
        this.retain = retain;
        return this;
    }

    public String getTopic() {
        return this.topic;
    }

    public ConfigMqtt setDefaultTopic(final String topic) {
        if (this.topic == null) {
            this.topic = topic;
        }

        return this;
    }

    public ConfigMqtt setTopic(final String topic) {
        this.topic = topic;
        return this;
    }

    public boolean isPublishBridgeInfo() {
        return this.bridgeInfo;
    }

    public ConfigMqtt setBridgeInfo(final boolean bridgeInfo) {
        this.bridgeInfo = bridgeInfo;
        return this;
    }

    public boolean isDeduplicate() {
        return this.deduplicate;
    }

    public ConfigMqtt setDeduplicate(final boolean deduplicate) {
        this.deduplicate = deduplicate;
        return this;
    }
}
