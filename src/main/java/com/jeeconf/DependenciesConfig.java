package com.jeeconf;

import com.jeeconf.annotations.JEEConfComponent;
import com.jeeconf.annotations.JEEConfComponentType;
import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

class DependenciesConfig {
    private Map<Class<?>, Object> regInstances = new HashMap<>();
    private String path;

    DependenciesConfig() {
    }

    DependenciesConfig(String path) {
        this.path = path;
        loadBeansByAutoSearch();
    }

    private void loadBeansByAutoSearch() {
        new FastClasspathScanner(path)
                .scan()
                .getNamesOfClassesWithAnnotation(JEEConfComponent.class)
                .stream()
                .map(this::loadClass)
                .forEach(this::registerBean);
    }

    private void registerBean(Class<?> beanClass) {
        Class<?> beanType = beanClass;
        JEEConfComponentType typeAnn = beanClass.getAnnotation(JEEConfComponentType.class);
        if (typeAnn != null) {
            beanType = typeAnn.value();
        }
        register(beanClass).as(beanType).complete();
    }

    private Class<?> loadClass(String name) {
        try {
            return getClass().getClassLoader().loadClass(name);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Cannot load class " + name, e);
        }
    }


    Registration register(Class<?> regClass) {
        Object instance = loadObject(regClass);
        return new Registration(instance);
    }

    Registration register(Object instance) {
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
