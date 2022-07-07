package app;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
    public static final Properties config;

    static {
        config = new Properties();
        try {
            InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("config.properties");
            if (is != null) {
                config.load(is);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getProperty(String key) {
        return config.getProperty(key);
    }
}
