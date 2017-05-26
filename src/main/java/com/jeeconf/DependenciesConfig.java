package com.jeeconf;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

class DependenciesConfig {
    private Map<Class<?>, Object> regInstances = new HashMap<>();

    Registration register(Class<?> regClass) {
        Object instance = loadObject(regClass);
        return new Registration(instance);
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
        Class<?> key = regInstances.keySet().stream().filter(i -> Objects.equals(i, type)).findFirst().get();
        return (T) regInstances.get(key);
    }

    public class Registration {
        private Object instance;
        private Class<?> type;

        Registration(Object instance) {
            this.instance = instance;
            this.type = instance.getClass();
        }

        Registration as(Class<?> type) {
            this.type = type;
            return this;
        }

        void complete() {
            regInstances.put(type, instance);
        }
    }
}
