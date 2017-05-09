package com.jeeconf;

import java.util.HashMap;
import java.util.Map;

class DependenciesConfig {
    private Map<Class<?>, Class<?>> registeredClasses = new HashMap<>();
    private Map<Class<?>, Object> registeredInstances = new HashMap<>();

    Registration register(Class<?> clazz) {
        registeredClasses.put(clazz, clazz);
        return new Registration(clazz);
    }

    @SuppressWarnings("unchecked")
    <T> Class<T> findClass(Class<T> clazz) {
        Class<T> key = (Class<T>) registeredClasses.keySet().stream().filter(c -> c.equals(clazz)).findFirst().get();
        return (Class<T>) registeredClasses.get(key);
    }

    <T> Registration register(T object) {
        this.registeredInstances.put(object.getClass(), object);
        return new Registration(object);
    }

    @SuppressWarnings("unchecked")
    <T> T findInstance(Class<T> clazz) {
        Class<T> key = (Class<T>) registeredInstances.keySet().stream().filter(c -> c.equals(clazz)).findFirst().orElse(null);
        return (T) registeredInstances.get(key);
    }

    class Registration {
        private Class<?> clazz;
        private Object object;

        Registration(Class<?> clazz) {
            this.clazz = clazz;
        }

        Registration(Object object) {
            this.object = object;
        }

        void as(Class<?> type) {
            if (object == null) {
                registeredClasses.put(type, clazz);
            } else {
                registeredInstances.put(type, object);
            }
        }
    }
}
