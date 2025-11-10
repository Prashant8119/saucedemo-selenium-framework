package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * ConfigReader class to read configuration from config.properties file
 * This class uses Singleton pattern to ensure only one instance exists
 */
public class ConfigReader {
    private static ConfigReader instance;
    private Properties properties;

    // Private constructor to prevent instantiation
    private ConfigReader() {
        properties = new Properties();
        loadProperties();
    }

    // Get singleton instance
    public static ConfigReader getInstance() {
        if (instance == null) {
            synchronized (ConfigReader.class) {
                if (instance == null) {
                    instance = new ConfigReader();
                }
            }
        }
        return instance;
    }

    // Load properties from file
    private void loadProperties() {
        try {
            FileInputStream fis = new FileInputStream("src/test/resources/config.properties");
            properties.load(fis);
            fis.close();
            System.out.println("✓ Configuration loaded successfully");
        } catch (IOException e) {
            System.err.println("✗ Failed to load config.properties file");
            e.printStackTrace();
            throw new RuntimeException("Configuration file not found!");
        }
    }

    // Get property value by key
    public String getProperty(String key) {
        String value = properties.getProperty(key);
        if (value == null) {
            System.err.println("✗ Property not found: " + key);
        }
        return value;
    }

    // Get property with default value
    public String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    // Get URL
    public String getUrl() {
        return getProperty("url");
    }

    // Get Browser
    public String getBrowser() {
        return getProperty("browser");
    }

    // Get Username
    public String getUsername() {
        return getProperty("username");
    }

    // Get Password
    public String getPassword() {
        return getProperty("password");
    }
}