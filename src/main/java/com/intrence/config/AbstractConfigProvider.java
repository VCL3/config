package com.intrence.config;

import com.intrence.config.collection.ConfigMap;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

public abstract class AbstractConfigProvider {
    public static final String ENV_PROPERTY = "intrence.environment";
    protected static final String ALL_ENVIRONMENT = "all";
    protected static final String ENV_VARIABLE = "INTRENCE_ENV";

    protected static final String CONFIG_PACKAGE = ConfigProvider.class.getPackage().getName();
    protected static final Pattern CONFIG_PATH_REGEX = Pattern.compile("^.*\\.config\\.yml$");

    protected String environment;
    protected ConfigMap configMap;

    protected AbstractConfigProvider() {
        this.environment = setupEnvironment();

        Set<String> configPaths = getConfigPaths();
        List<ConfigMap> configMaps = readConfigMaps(environment, configPaths);
        this.configMap = mergeConfigMaps(configMaps);
    }

    // Override this.
    protected String setupEnvironment() {
        String environment = System.getProperty(ENV_PROPERTY);
        if (environment == null) {
            environment = System.getenv(ENV_VARIABLE);
            if (environment != null) {
                System.setProperty(ENV_PROPERTY, environment);
            }
        }
        if (environment == null) {
            throw new RuntimeException("System property " + ENV_PROPERTY + " or environment variable " +
                    ENV_VARIABLE + " must be set.");
        }
        return environment;
    }

    // Override this.
    protected Set<String> getConfigPaths() {
        Set<String> configPaths = new Reflections(CONFIG_PACKAGE,
                new ResourcesScanner()).getResources(CONFIG_PATH_REGEX);

        if (configPaths.isEmpty()) {
            throw new RuntimeException("No config files found in package " + CONFIG_PACKAGE + " matching regex " + CONFIG_PATH_REGEX);
        }

        return configPaths;
    }

    protected InputStream openPathAsInputStream(String path) {
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
    }

    protected List<ConfigMap> readConfigMaps(String environment, Set<String> configPaths) {
        List<ConfigMap> allConfigMaps = new ArrayList<ConfigMap>();
        List<ConfigMap> environmentConfigMaps = new ArrayList<ConfigMap>();

        for (String path : configPaths) {
            InputStream inputStream = openPathAsInputStream(path);
            ConfigMap fullConfig = new ConfigMap((Map) new Yaml().load(inputStream));
            try {
                inputStream.close();
            } catch (IOException e) {
                throw new RuntimeException("Exception closing InputStream to " + path, e);
            }

            ConfigMap allConfig = fullConfig.getMap(ALL_ENVIRONMENT, null);
            if (allConfig != null) {
                allConfigMaps.add(allConfig);
            }

            ConfigMap environmentConfig = fullConfig.getMap(environment, null);
            if (environmentConfig != null) {
                environmentConfigMaps.add(environmentConfig);
            }
        }

        if (environmentConfigMaps.isEmpty()) {
            throw new RuntimeException("Environment \"" + environment + "\" is not configured by any of " +
                    "the scanned config files: " + configPaths);
        }

        allConfigMaps.addAll(environmentConfigMaps);
        return allConfigMaps;
    }

    protected ConfigMap mergeConfigMaps(List<ConfigMap> configMaps) {
        ConfigMap result = configMaps.get(0);
        for (int i = 1; i < configMaps.size(); i++) {
            result = result.merge(configMaps.get(i));
        }
        return result;
    }
}
