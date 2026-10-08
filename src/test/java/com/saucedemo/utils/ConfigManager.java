package com.saucedemo.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class ConfigManager {
  private static final Properties PROPERTIES = loadProperties();

  private ConfigManager() {}

  public static String get(String key) {
    String systemValue = System.getProperty(key);
    if (systemValue != null && !systemValue.isBlank()) {
      return systemValue;
    }

    String environmentValue = System.getenv(environmentVariable(key));
    if (environmentValue != null && !environmentValue.isBlank()) {
      return environmentValue;
    }

    String value = PROPERTIES.getProperty(key);
    if (value == null || value.isBlank()) {
      throw new IllegalArgumentException("Missing configuration value: " + key);
    }
    return value;
  }

  public static int getInt(String key) {
    return Integer.parseInt(get(key));
  }

  public static boolean getBoolean(String key) {
    return Boolean.parseBoolean(get(key));
  }

  public static String standardUsername() {
    return get("standard.username");
  }

  public static String standardPassword() {
    return get("standard.password");
  }

  private static String environmentVariable(String key) {
    return switch (key) {
      case "baseUrl" -> "SAUCEDEMO_BASE_URL";
      case "browser" -> "PLAYWRIGHT_BROWSER";
      case "headless" -> "PLAYWRIGHT_HEADLESS";
      case "timeoutMs" -> "PLAYWRIGHT_TIMEOUT_MS";
      case "apiBaseUrl" -> "REQRES_BASE_URL";
      case "apiKey" -> "REQRES_API_KEY";
      default -> key.replaceAll("([a-z])([A-Z])", "$1_$2").toUpperCase();
    };
  }

  private static Properties loadProperties() {
    Properties properties = new Properties();
    try (InputStream input = ConfigManager.class.getClassLoader()
        .getResourceAsStream("config.properties")) {
      if (input == null) {
        throw new IllegalStateException("Missing test resource: config.properties");
      }
      properties.load(input);
      return properties;
    } catch (IOException e) {
      throw new IllegalStateException("Unable to load config.properties", e);
    }
  }
}
