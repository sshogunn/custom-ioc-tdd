package com.jeeconf;

import com.jeeconf.annotations.JEEConfComponent;
import com.jeeconf.annotations.JEEConfComponentType;
import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;

import java.util.HashMap;
import java.util.Map;

class DependenciesConfig {
    private String beansPath;
    private Map<Class<?>, Class<?>> registeredClasses = new HashMap<>();
    private Map<Class<?>, Object> registeredInstances = new HashMap<>();

    DependenciesConfig() {
    }

    DependenciesConfig(boolean autoSearch, String beansPath) {
        this.beansPath = beansPath;
        if (autoSearch) {
            loadBeans();
        }
    }

    private void loadBeans() {
        new FastClasspathScanner(beansPath)
                .scan()
                .getNamesOfAllStandardClasses()
                .stream()
                .map(this::loadClass).filter(c -> c.getAnnotation(JEEConfComponent.class) != null)
                .forEach(this::registerBean);

    }

    private void registerBean(Class<?> it) {
        JEEConfComponentType type = it.getAnnotation(JEEConfComponentType.class);
        if (type != null) {
            registeredClasses.put(type.value(), it);
        } else {
            registeredClasses.put(it, it);
        }
    }

    private Class<?> loadClass(String file) {
        try {
            Class<?> clazz = getClass().getClassLoader().loadClass(file);
            return clazz;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

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
