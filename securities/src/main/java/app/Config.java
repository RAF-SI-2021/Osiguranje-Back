package app;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Config {
    public static final Properties config;

    static {
        config = new Properties();
        try {
            ClassLoader classLoader = Config.class.getClassLoader();
            File f = new File(classLoader.getResource("config.properties").getFile());
            //File f = new File("src/main/resources/config.properties");
            config.load(new FileInputStream(f));
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    public static String getProperty(String key) {
        return config.getProperty(key);
    }
}
