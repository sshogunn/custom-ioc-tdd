package com.jeeconf;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

class DependenciesConfig {
    private List<Object> regInstances = new ArrayList<>();

    void register(Class<?> regClass) {
        Object instance = loadObject(regClass);
        this.regInstances.add(instance);
    }

    private Object loadObject(Class<?> regClass) {
        try {
            return regClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("Cannot load class instance", e);
        }
    }

    @SuppressWarnings("unchecked")
    <T> T findInstance(Class<T> type) {
        return (T) regInstances.stream().filter(i -> Objects.equals(i.getClass(), type)).findFirst().get();
    }
}
