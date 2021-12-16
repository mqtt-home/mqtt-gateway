package de.rnd7.mqttgateway.config;

import com.google.gson.annotations.Expose;
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

    @SerializedName("qos")
    private int qos = 1;

    @SerializedName("bridge-info")
    private boolean bridgeInfo = true;

    @Expose(serialize = false)
    private boolean autoPublish = true;

    @SerializedName("bridge-info-topic")
    private String bridgeInfoTopic;

    @SerializedName("deduplicate")
    private boolean deduplicate = false;

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

    public boolean isAutoPublish() {
        return autoPublish;
    }

    public ConfigMqtt setAutoPublish(final boolean autoPublish) {
        this.autoPublish = autoPublish;
        return this;
    }

    public boolean isDeduplicate() {
        return this.deduplicate;
    }

    public ConfigMqtt setDeduplicate(final boolean deduplicate) {
        this.deduplicate = deduplicate;
        return this;
    }

    public ConfigMqtt setBridgeInfoTopic(final String bridgeInfoTopic) {
        this.bridgeInfoTopic = bridgeInfoTopic;
        return this;
    }

    public Optional<String> getBridgeInfoTopic() {
        return Optional.ofNullable(this.bridgeInfoTopic);
    }
}
