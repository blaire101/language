package com.x.dmt.email;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class LoadMailConfig {

    private static Logger logger = LoggerFactory.getLogger(LoadMailConfig.class);

    private static Properties mailConfig;

    private static LoadMailConfig loadMailConfig = new LoadMailConfig();

    private LoadMailConfig() {
    }

    public static LoadMailConfig getInstance() {
        try {
            loadProperty();
        } catch (Exception e) {
            logger.error("load mail config failure !");
            throw new RuntimeException(e);
        }
        return loadMailConfig;
    }

    private static void loadProperty() throws IOException {
        Properties properties = new Properties();
        InputStream stream = LoadMailConfig.class.getResourceAsStream("/props/mail-config.properties");
        try {
            properties.load(stream);
            mailConfig = properties;
        } catch (IOException e) {
            logger.error("load mail config failure !");
            throw new RuntimeException(e);
        } finally {
            stream.close();
        }
    }

    public String getMailConfigBykey(String key) {
        return mailConfig.getProperty(key);
    }
}
