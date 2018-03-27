package com.intrence.config.configloader;

import com.intrence.config.collection.ConfigMap;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * A ConfigMap loader which loads static data from a yaml file.
 *
 */
public class YamlFileConfigMapLoader implements ConfigMapLoader {
    private final String sourceFile;
    
    public YamlFileConfigMapLoader(String configMapFile) {
        this.sourceFile = configMapFile;
    }
    
    public ConfigMap getConfigMap() {
        // read data from yaml file
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(sourceFile);
        ConfigMap allSources = new ConfigMap((Map) new Yaml().load(inputStream));
        try {
            inputStream.close();
        } catch (IOException e) {
            throw new RuntimeException("Exception closing InputStream to " + sourceFile, e);
        }
        return allSources;
    }

    // only static data is returned - it can't change at runtime
    public boolean isConfigMapDynamic() {
        return false;
    }

    public void stopUpdating() {
        // NO-OP
    }
}
