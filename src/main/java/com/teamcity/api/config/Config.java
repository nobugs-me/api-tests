package com.teamcity.api.config;

import java.io.IOException;
import java.util.Properties;

public final class Config {

    private static final String CONFIG_PROPERTIES = "config.properties";
    private static Config config;
    private final Properties properties;

    private Config() {
        properties = new Properties();
        loadProperties();
    }

    private static Config getConfig() {
        if (config == null) {
            config = new Config();
        }
        return config;
    }

    public static String getProperty(String key) {
        return getConfig().properties.getProperty(key);
    }

    private void loadProperties() {
        try (var inputStream = Config.class.getClassLoader().getResourceAsStream(CONFIG_PROPERTIES)) {
            properties.load(inputStream);
            // Убираем проверку на null (если файла не существует), это будет отлавливаться в блоке catch
        } catch (IOException | NullPointerException e) {
            throw new IllegalStateException("Cannot load properties file", e);
        }
    }

}
