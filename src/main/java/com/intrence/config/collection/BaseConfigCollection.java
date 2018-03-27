package com.intrence.config.collection;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class BaseConfigCollection {
    protected static Object validateAndConvert(Object value, String path) {
        if (value == null ||
                value instanceof Double ||
                value instanceof Integer ||
                value instanceof Long ||
                value instanceof Boolean ||
                value instanceof ConfigMap ||
                value instanceof ConfigList) {
            return value;
        } else if (value instanceof String) {
            return ((String) value).isEmpty() ? null : value;
        } else if (value instanceof Map) {
            ConfigMap dynamicMap = new ConfigMap((Map) value, path);
            return dynamicMap.isEmpty() ? null : dynamicMap;
        } else if (value instanceof List) {
            ConfigList dynamicList = new ConfigList((List) value, path);
            return dynamicList.isEmpty() ? null : dynamicList;
        } else if (value instanceof Set) {
            ConfigList dynamicList = new ConfigList(new ArrayList((Set) value), path);
            return dynamicList.isEmpty() ? null : dynamicList;
        } else {
            throw new IllegalArgumentException("Invalid value at " + path + ", " + value.getClass().getName());
        }
    }

    protected abstract Object get(String path);

    public String getString(String path) {
        return getWithPath(path, String.class);
    }

    public String getString(String path, String def) {
        return getWithPath(path, String.class, def);
    }

    public Integer getInteger(String path) {
        return getWithPath(path, Integer.class);
    }

    public Integer getInteger(String path, Integer def) {
        return getWithPath(path, Integer.class, def);
    }

    public Long getLong(String path) {
        return getWithPath(path, Long.class);
    }

    public Long getLong(String path, Long def) {
        return getWithPath(path, Long.class, def);
    }

    public Double getDouble(String path) {
        return getWithPath(path, Double.class);
    }

    public Double getDouble(String path, Double def) {
        return getWithPath(path, Double.class, def);
    }

    public Boolean getBoolean(String path) {
        return getWithPath(path, Boolean.class);
    }

    public Boolean getBoolean(String path, Boolean def) {
        return getWithPath(path, Boolean.class, def);
    }

    public ConfigMap getMap(String path) {
        return getWithPath(path, ConfigMap.class);
    }

    public ConfigMap getMap(String path, ConfigMap def) {
        return getWithPath(path, ConfigMap.class, def);
    }

    public ConfigList getList(String path) {
        return getWithPath(path, ConfigList.class);
    }

    public ConfigList getList(String path, ConfigList def) {
        return getWithPath(path, ConfigList.class, def);
    }

    public Object getObject(String path) {
        return getWithPath(path, Object.class);
    }

    public Object getObject(String path, Object def) {
        return getWithPath(path, Object.class, def);
    }

    private <T> T getWithPath(String path, Class<T> klass, T def) {
        Object o = getRecursive(this, path.split("\\."), 0);

        if (o == null) return def;

        if (klass.isAssignableFrom(o.getClass())) {
            @SuppressWarnings("unchecked")
            T t = (T) o;
            return t;
        } else if (klass.equals(Long.class) && Integer.class.isAssignableFrom(o.getClass())) {
            @SuppressWarnings("unchecked")
            T t = (T) ((Long) ((Integer) o).longValue());
            return t;
        } else {
            throw new IllegalArgumentException("Value found at " + path + " is not a " + klass.getName());
        }
    }

    private <T> T getWithPath(String path, Class<T> klass) {
        T value = getWithPath(path, klass, null);

        if (value == null)
            throw new IllegalArgumentException("No value found at " + path);
        else
            return value;
    }

    private Object getRecursive(Object node, String[] path, int level) {
        Object next = null;

        if (node instanceof BaseConfigCollection) {
            next = ((BaseConfigCollection) node).get(path[level]);
        }

        if (next == null) return null;

        if (level >= path.length - 1) {
            return next;
        } else {
            return getRecursive(next, path, level + 1);
        }
    }
}
