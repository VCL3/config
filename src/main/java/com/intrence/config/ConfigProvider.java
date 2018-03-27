package com.intrence.config;

import com.intrence.config.collection.ConfigMap;

public class ConfigProvider extends AbstractConfigProvider {
    private static final ConfigProvider INSTANCE = new ConfigProvider();

    public static String getEnvironment() {
        return INSTANCE.environment;
    }

    public static ConfigMap getConfig() {
        return INSTANCE.configMap;
    }

    public static void ensureConfig() {
        getConfig();
    }

    protected ConfigProvider() {
        super();
    }
}
