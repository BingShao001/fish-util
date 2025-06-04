package com.yb.fish.leaf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;

public class PropertyFactory {
    private static final Logger logger = LoggerFactory.getLogger(PropertyFactory.class);
    private static final Properties prop = new Properties();
    static {
        try {
            InputStream resourceAsStream = PropertyFactory.class.getClassLoader().getResourceAsStream("leaf.properties");
            Objects.requireNonNull(resourceAsStream, "can't find leaf.properties file");
            prop.load(resourceAsStream);
        } catch (IOException e) {
            logger.warn("Load Properties Ex", e);
        }
    }
    public static Properties getProperties() {
        return prop;
    }
}
