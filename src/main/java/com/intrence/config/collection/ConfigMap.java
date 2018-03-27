package com.intrence.config.collection;

import java.util.*;

public final class ConfigMap extends BaseConfigCollection implements Map<String, Object> {
    private final Date dateRefreshed;
    
    private final Map<String, Object> map;

    public ConfigMap() {
        this(Collections.emptyMap());
    }

    public ConfigMap(Map map) {
        this(map, "");
    }

    protected ConfigMap(Map inputMap, String path) {
        if (inputMap == null) throw new IllegalArgumentException("Map map must be non-null.");
        
        dateRefreshed = new Date();
        
        HashMap<String, Object> map = new HashMap<String, Object>();

        Entry entry;
        Object oKey;
        String key;
        String newPath;
        Object value;
        for (Object oEntry : inputMap.entrySet()) {
            entry = (Entry) oEntry;

            oKey = entry.getKey();
            if (oKey == null || !(oKey instanceof String)) {
                throw new IllegalArgumentException("Non-string key at " + (path.equals("") ? "top-level" : path));
            }
            key = (String) oKey;
            newPath = path + (path.equals("") ? "" : ".") + key;

            value = validateAndConvert(entry.getValue(), newPath);
            if (value != null) {
                map.put(key, value);
            }
        }

        this.map = Collections.unmodifiableMap(map);
    }

    public ConfigMap merge(ConfigMap other) {
        Map<String, Object> map = new HashMap<String, Object>(this);

        for (Entry<String, Object> entry : other.entrySet()) {
            String key = entry.getKey();
            Object newValue = entry.getValue();
            Object putValue = newValue;

            if (map.containsKey(key)) {
                Object oldValue = map.get(entry.getKey());
                if (newValue instanceof ConfigMap && oldValue instanceof ConfigMap) {
                    putValue = ((ConfigMap) oldValue).merge((ConfigMap) newValue);
                }
            }

            map.put(key, putValue);
        }

        return new ConfigMap(map);
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public boolean containsKey(Object o) {
        return map.containsKey(o);
    }

    @Override
    public boolean containsValue(Object o) {
        return map.containsValue(o);
    }

    @Override
    public Object get(Object o) {
        return map.get(o);
    }

    @Override
    public Object put(String s, Object o) {
        return map.put(s, o);
    }

    @Override
    public Object remove(Object o) {
        return map.remove(o);
    }

    @Override
    public void putAll(Map<? extends String, ? extends Object> map) {
        this.map.putAll(map);
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public Set<String> keySet() {
        return map.keySet();
    }

    @Override
    public Collection<Object> values() {
        return map.values();
    }

    @Override
    public Set<Entry<String, Object>> entrySet() {
        return map.entrySet();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ConfigMap that = (ConfigMap) o;

        return map.equals(that.map);
    }

    @Override
    public int hashCode() {
        return map.hashCode();
    }

    @Override
    protected Object get(String path) {
        return get((Object) path);
    }

    @Override
    public String toString() {
        return map.toString();
    }
    
    public Date getDateRefreshed() {
        return dateRefreshed;
    }
}
