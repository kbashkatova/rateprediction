package ru.liga.rateforecaster.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * A singleton class for managing application configuration properties from a properties file.
 */
public class AppConfig {

    private static final AppConfig instance = new AppConfig();
    private final String PROPERTIES_PATH = "src/main/resources/application.properties";
    private final Logger logger = LoggerFactory.getLogger(AppConfig.class);


    private final Properties properties = new Properties();

    private AppConfig() {
        try (FileInputStream input = new FileInputStream(PROPERTIES_PATH)) {
            properties.load(input);
        } catch (IOException e) {
            logger.error("Failed to load application properties from {}", PROPERTIES_PATH, e);
            throw new RuntimeException("Failed to load application properties from " + PROPERTIES_PATH, e);
        }
    }

    public static AppConfig getInstance() {
        return instance;
    }

    public String getTryFilePath() {
        return properties.getProperty("tryFilePath");
    }

    public String getEurFilePath() {
        return properties.getProperty("eurFilePath");
    }

    public String getUsdFilePath() {
        return properties.getProperty("usdFilePath");
    }
}