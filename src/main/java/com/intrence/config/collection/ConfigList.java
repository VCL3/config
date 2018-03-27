package com.intrence.config.collection;

import java.util.*;

public final class ConfigList extends BaseConfigCollection implements List<Object> {
    private final List<Object> list;

    public ConfigList() {
        this(Collections.emptyList());
    }

    public ConfigList(List list) {
        this(list, "");
    }

    protected ConfigList(List inputList, String path) {
        if (inputList == null) throw new IllegalArgumentException("List value must be non-null");
        ArrayList<Object> list = new ArrayList<Object>();

        String newPath;
        Object value;
        int i = 0;
        for (Object oValue : inputList) {
            newPath = path + "." + i;

            value = validateAndConvert(oValue, newPath);
            if (value != null) {
                list.add(value);
            }
        }

        this.list = Collections.unmodifiableList(list);
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return list.contains(o);
    }

    @Override
    public Iterator<Object> iterator() {
        return list.iterator();
    }

    @Override
    public Object[] toArray() {
        return list.toArray();
    }

    @Override
    public <T> T[] toArray(T[] ts) {
        return list.toArray(ts);
    }

    @Override
    public boolean add(Object o) {
        return list.add(o);
    }

    @Override
    public boolean remove(Object o) {
        return list.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> objects) {
        return list.containsAll(objects);
    }

    @Override
    public boolean addAll(Collection<? extends Object> objects) {
        return list.addAll(objects);
    }

    @Override
    public boolean addAll(int i, Collection<? extends Object> objects) {
        return list.addAll(i, objects);
    }

    @Override
    public boolean removeAll(Collection<?> objects) {
        return list.removeAll(objects);
    }

    @Override
    public boolean retainAll(Collection<?> objects) {
        return list.retainAll(objects);
    }

    @Override
    public void clear() {
        list.clear();
    }

    @Override
    public Object get(int i) {
        return list.get(i);
    }

    @Override
    public Object set(int i, Object o) {
        return list.set(i, o);
    }

    @Override
    public void add(int i, Object o) {
        list.add(i, o);
    }

    @Override
    public Object remove(int i) {
        return list.remove(i);
    }

    @Override
    public int indexOf(Object o) {
        return list.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return list.lastIndexOf(o);
    }

    @Override
    public ListIterator<Object> listIterator() {
        return list.listIterator();
    }

    @Override
    public ListIterator<Object> listIterator(int i) {
        return list.listIterator(i);
    }

    @Override
    public List<Object> subList(int i, int i1) {
        return list.subList(i, i1);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ConfigList objects = (ConfigList) o;

        return list.equals(objects.list);
    }

    @Override
    public int hashCode() {
        return list.hashCode();
    }

    @Override
    public String toString() {
        return list.toString();
    }

    @Override
    protected Object get(String path) {
        int i;

        try {
            i = Integer.parseInt(path);
        } catch (Exception e) {
            return null;
        }

        return get(i);
    }
}
