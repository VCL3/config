package com.intrence.config.configloader;


import com.intrence.config.collection.ConfigMap;

/**
 * A generic interface to allow ConfigMap loading, and to provide information to callers about whether the ConfigMap
 * data returned can change at runtime.
 */
public interface ConfigMapLoader {
    public ConfigMap getConfigMap();
    public boolean isConfigMapDynamic();
    public void stopUpdating();
}
