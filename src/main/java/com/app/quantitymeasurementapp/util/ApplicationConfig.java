package com.app.quantitymeasurementapp.util;

import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;

public class ApplicationConfig {

    private static final Logger logger = Logger.getLogger(ApplicationConfig.class.getName());
    private static ApplicationConfig instance;
    private final Properties properties = new Properties();

    // Streamlined Enum: Only contains the keys we actually use in ConnectionPool!
    public enum ConfigKey {
        DB_DRIVER_CLASS("db.driver"),
        DB_URL("db.url"),
        DB_USERNAME("db.username"),
        DB_PASSWORD("db.password"),
        DB_POOL_SIZE("db.pool-size"),
        HIKARI_CONNECTION_TEST_QUERY("db.hikari.connection-test-query");

        private final String key;

        ConfigKey(String key) { 
            this.key = key; 
        }
        
        public String getKey() { 
            return key; 
        }
    }

    private ApplicationConfig() {
        loadConfiguration();
    }

    public static synchronized ApplicationConfig getInstance() {
        if (instance == null) {
            instance = new ApplicationConfig();
        }
        return instance;
    }

    /**
     * Clean, simple resource loading. 
     */
    private void loadConfiguration() {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("application.properties")) {
            if (input != null) {
                properties.load(input);
                logger.info("Successfully loaded application.properties!");
            } else {
                logger.warning("application.properties not found. Using H2 defaults.");
                loadDefaults();
            }
        } catch (Exception e) {
            logger.severe("Error loading config: " + e.getMessage());
            loadDefaults();
        }
    }

    /**
     * Safe fallback values if the properties file is missing.
     */
    private void loadDefaults() {
        properties.setProperty(ConfigKey.DB_DRIVER_CLASS.getKey(), "org.h2.Driver");
        properties.setProperty(ConfigKey.DB_URL.getKey(), "jdbc:h2:./quantitymeasurementdb;AUTO_SERVER=TRUE");
        properties.setProperty(ConfigKey.DB_USERNAME.getKey(), "sa");
        properties.setProperty(ConfigKey.DB_PASSWORD.getKey(), "");
        properties.setProperty(ConfigKey.DB_POOL_SIZE.getKey(), "10");
    }

    // --- Simple Getters ---

    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    public String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    public int getIntProperty(String key, int defaultValue) {
        try {
            return Integer.parseInt(properties.getProperty(key, String.valueOf(defaultValue)));
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}