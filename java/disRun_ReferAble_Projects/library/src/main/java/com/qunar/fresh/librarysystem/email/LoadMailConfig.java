package com.qunar.fresh.librarysystem.email;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA. User: libin.chen Date: 14-4-10 Time: 下午7:08 To change this template use File | Settings |
 * File Templates.
 */
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
            logger.error("载入邮件配置失败");
            throw new RuntimeException(e);
        }
        return loadMailConfig;
    }

    private static void loadProperty() throws IOException {
        Properties properties = new Properties();
        InputStream stream = LoadMailConfig.class.getResourceAsStream("/mail-config.properties");
        try {
            properties.load(stream);
            mailConfig = properties;
        } catch (IOException e) {
            logger.error("载入邮件配置失败");
            throw new RuntimeException(e);
        } finally {
            stream.close();
        }
    }

    String getMailConfigBykey(String key) {
        return mailConfig.getProperty(key);
    }
}
