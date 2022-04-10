package futures;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Config {
    public static Properties config;

    static {
        config = new Properties();
        try {
            config.load(new FileInputStream("futures-api" + File.separator + "config.properties"));
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public static String getProperty(String key) {
        return config.getProperty(key);
    }
}
