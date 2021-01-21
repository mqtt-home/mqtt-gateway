# mqtt-gateway

This is a internal component to build a MQTT gateway.

## Maven

### Repository

Add the following repository to your project:

```xml
<repository>
  <id>github</id>
  <name>GitHub philipparndt Apache Maven Packages</name>
  <url>https://maven.pkg.github.com/philipparndt/mqtt-gateway</url>
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
  <version>1.0.3</version>
</dependency>
```
