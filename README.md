# mqtt-gateway

[![mqtt-smarthome](https://img.shields.io/badge/mqtt-smarthome-blue.svg)](https://github.com/mqtt-smarthome/mqtt-smarthome)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=de.rnd7.mqttgateway%3Amqttgateway-parent&metric=alert_status)](https://sonarcloud.io/dashboard?id=de.rnd7.mqttgateway%3Amqttgateway-parent)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=de.rnd7.mqttgateway%3Amqttgateway-parent&metric=coverage)](https://sonarcloud.io/dashboard?id=de.rnd7.mqttgateway%3Amqttgateway-parent)
[![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=de.rnd7.mqttgateway%3Amqttgateway-parent&metric=reliability_rating)](https://sonarcloud.io/dashboard?id=de.rnd7.mqttgateway%3Amqttgateway-parent)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=de.rnd7.mqttgateway%3Amqttgateway-parent&metric=security_rating)](https://sonarcloud.io/dashboard?id=de.rnd7.mqttgateway%3Amqttgateway-parent)
[![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=de.rnd7.mqttgateway%3Amqttgateway-parent&metric=vulnerabilities)](https://sonarcloud.io/dashboard?id=de.rnd7.mqttgateway%3Amqttgateway-parent)

This is an internal component to build a MQTT gateway.

## Maven

### Repository

Add the following repository to your project:

```xml
<repository>
  <id>github</id>
  <name>GitHub mqtt-home Apache Maven Packages</name>
  <url>https://maven.pkg.github.com/mqtt-home/mqtt-gateway</url>
</repository>
```

Make sure you have a GitHub access token in your `~/.m2/settings.xml`
```xml
<servers>
  <server>
    <id>github</id>
    <username>your username</username>
    <password>your access token</password>
  </server>
</servers>
```

See https://docs.github.com/en/packages/guides/configuring-apache-maven-for-use-with-github-packages

### Dependency

Use the following dependency:

```xml
<dependency>
  <groupId>de.rnd7.mqttgateway</groupId>
  <artifactId>mqttgateway</artifactId>
  <version>1.1.0-b7</version>
</dependency>
```
