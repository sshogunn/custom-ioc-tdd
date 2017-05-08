package com.jeeconf;

import com.google.common.base.Objects;
import com.jeeconf.annotations.*;
import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

class DependenciesConfig {
    private Map<Identity, Object> registeredInstances = new HashMap<>();
    private String path;

    DependenciesConfig() {
    }

    DependenciesConfig(String path) {
        this.path = path;
        loadBeansByAutoSearch();
    }

    private void loadBeansByAutoSearch() {
        loadBeansWithoutDep();
        loadBeansWithDep();
    }

    private void loadBeansWithDep() {
        beansClassesStream()
                .filter(c -> c.getConstructors().length == 1)
                .filter(c -> c.getConstructors()[0].getAnnotation(AutoSearch.class) != null)
                .filter(c -> c.getConstructors()[0].getParameterCount() == 1)
                .map(this::registerWithDep)
                .forEach(Registration::complete);
    }

    private void loadBeansWithoutDep() {
        beansClassesStream()
                .filter(c -> c.getConstructors().length == 1 && c.getConstructors()[0].getParameterCount() == 0)
                .map(this::registerByType)
                .forEach(Registration::complete);
    }

    private Stream<? extends Class<?>> beansClassesStream() {
        return new FastClasspathScanner(path)
                .scan()
                .getNamesOfClassesWithAnnotation(JEEConfComponent.class)
                .stream()
                .map(this::loadClass);
    }

    private Registration registerWithDep(Class<?> clazz) {
        Constructor constructor = clazz.getConstructors()[0];
        Class<?>[] paramTypes = constructor.getParameterTypes();
        Object[] params = new Object[1];
        String key = extractParamKey(constructor.getParameters()[0]);
        if (key != null) {
            params[0] = findInstance(paramTypes[0], key);
        } else {
            params[0] = findInstance(paramTypes[0]);
        }
        try {
            Object instance = constructor.newInstance(params);
            return new Registration(instance);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Cannot create an instance with dependencies", e);
        }
    }

    private String extractParamKey(Parameter parameter) {
        Key key = parameter.getAnnotation(Key.class);
        if (key != null) {
            return key.value();
        }
        return null;
    }

    private Class<?> loadClass(String name) {
        try {
            return getClass().getClassLoader().loadClass(name);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Class not found, class name is: " + name, e);
        }
    }

    private Registration registerByType(Class<?> clazz) {
        JEEConfComponentType type = clazz.getAnnotation(JEEConfComponentType.class);
        Registration registration = register(clazz);
        if (type != null) {
            registration = register(clazz).as(type.value());
        }
        return registration.key(extractKey(clazz));
    }

    private String extractKey(Class<?> clazz) {
        Key key = clazz.getAnnotation(Key.class);
        if (key != null) {
            return key.value();
        }
        return null;
    }

    Registration register(Class<?> regClass) {
        ForeverNotAlone foreverNotAlone = regClass.getAnnotation(ForeverNotAlone.class);
        if (foreverNotAlone == null) {
            Object instance = createInstanceFrom(regClass);
             return new Registration(instance);
        } else {
            return new Registration(regClass);
        }
    }

    Registration register(Object regInstance) {
        return new Registration(regInstance);
    }

    private Object createInstanceFrom(Class<?> regClass) {
        try {
            return regClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("Instance cannot be created", e);
        }
    }

    @SuppressWarnings("unchecked")
    <T> T findInstance(Class<T> instanceClazz) {
        Identity classKey = registeredInstances.keySet().stream().filter(i -> i.typeClazz.equals(instanceClazz)).findFirst().get();
        T instance = (T) registeredInstances.get(classKey);
        if (instance != null) {
            return instance;
        } else {
            return (T) createInstanceFrom(classKey.implClazz);
        }
    }

    @SuppressWarnings("unchecked")
    <T> T findInstance(Class<?> instanceClazz, String key) {
        Identity classKey = registeredInstances.keySet().stream().filter(i -> i.typeClazz.equals(instanceClazz)).filter(i -> i.key.equals(key)).findFirst().get();
        T instance = (T) registeredInstances.get(classKey);
        if (instance != null) {
            return instance;
        } else {
            return (T) createInstanceFrom(classKey.implClazz);
        }
    }

    class Registration {
        private Object instance;
        private Class<?> typeClass;
        private Class<?> implClass;
        private String key;

        Registration(Object instance) {
            this.instance = instance;
            this.implClass = instance.getClass();
        }

        Registration(Class<?> implClass) {
            this.implClass = implClass;
        }

        Registration as(Class<?> type) {
            this.typeClass = type;
            return this;
        }

        Registration key(String key) {
            this.key = key;
            return this;
        }

        void complete() {
            if (typeClass != null) {
                registeredInstances.put(new Identity(typeClass, instance.getClass(), key), instance);
            } else {
                registeredInstances.put(new Identity(implClass, implClass, key), instance);
            }
        }
    }

    private static class Identity {
        private Class<?> typeClazz;
        private Class<?> implClazz;
        private String key;

        Identity(Class<?> typeClazz, Class<?> implClazz, String key) {
            this.typeClazz = typeClazz;
            this.implClazz = implClazz;
            this.key = key;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Identity identity = (Identity) o;
            return Objects.equal(typeClazz, identity.typeClazz) &&
                    Objects.equal(key, identity.key);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(typeClazz, key);
        }
    }
}
