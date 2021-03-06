package com.jeeconf;

import com.jeeconf.annotations.*;
import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

class DependenciesConfig {
    private Map<Identity, Implementation> regInstances = new HashMap<>();
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

    private void loadBeansWithoutDep() {
        beanClassesStream()
                .filter(c -> c.getConstructors().length == 1)
                .filter(c -> c.getConstructors()[0].getParameterCount() == 0)
                .forEach(this::registerBean);
    }

    private void loadBeansWithDep() {
        beanClassesStream()
                .filter(c -> c.getConstructors()[0].getParameterCount() > 0)
                .filter(c -> c.getConstructors()[0].getAnnotation(AutoSearch.class) != null)
                .forEach(this::registerBeanWithDep);
    }

    private Stream<? extends Class<?>> beanClassesStream() {
        return new FastClasspathScanner(path)
                .scan()
                .getNamesOfClassesWithAnnotation(JEEConfComponent.class)
                .stream()
                .map(this::loadClass)
                .filter(c -> c.getConstructors().length == 1);
    }

    private void registerBean(Class<?> beanClass) {
        Class<?> beanType = getBeanType(beanClass);
        register(beanClass).as(beanType).complete();
    }

    private void registerBeanWithDep(Class<?> beanClass) {
        Class<?> beanType = getBeanType(beanClass);
        Constructor<?> constructor = beanClass.getConstructors()[0];
        Class<?>[] paramTypes = constructor.getParameterTypes();
        Object[] params = new Object[paramTypes.length];
        for (int i = 0; i < paramTypes.length; i++) {
            Key keyAnn = constructor.getParameters()[i].getAnnotation(Key.class);
            Class<?> paramType = paramTypes[i];
            if (keyAnn == null) {
                params[i] = findInstance(paramType);
            } else {
                params[i] = findInstance(paramType, keyAnn.value());
            }
        }
        Object instance = loadObjectWithParams(constructor, params);
        register(instance).as(beanType).complete();
    }

    private Class<?> getBeanType(Class<?> beanClass) {
        Class<?> beanType = beanClass;
        JEEConfComponentType typeAnn = beanClass.getAnnotation(JEEConfComponentType.class);
        if (typeAnn != null) {
            beanType = typeAnn.value();
        }
        return beanType;
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

    private Object loadObjectWithParams(Constructor<?> constructor, Object[] params) {
        try {
            return constructor.newInstance(params);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Cannot create instance with params", e);
        }
    }

    @SuppressWarnings("unchecked")
    <T> T findInstance(Class<T> type) {
        Identity key = identityBaseStream(type)
                .findFirst()
                .get();
        return (T) searchInstanceBy(key);
    }

    @SuppressWarnings("unchecked")
    private <T> T findInstance(Class<?> type, String keyId) {
        Identity key = identityBaseStream(type)
                .filter(i -> Objects.equals(i.key, keyId))
                .findFirst()
                .get();
        return (T) searchInstanceBy(key);
    }

    private Object searchInstanceBy(Identity key) {
        Implementation impl = regInstances.get(key);
        if (impl.instance != null) {
            return impl.instance;
        }
        return loadObject(impl.implClass);
    }

    private Stream<Identity> identityBaseStream(Class<?> type) {
        return regInstances.keySet()
                .stream()
                .filter(i -> Objects.equals(i.type, type));
    }

    public class Registration {
        private Object instance;
        private Class<?> type;
        private String key;
        private boolean isPrototype;

        Registration(Object instance) {
            this.instance = instance;
            this.type = instance.getClass();
            this.key = extractBeanKeyId();
            this.isPrototype = checkOnPrototype();
        }

        private String extractBeanKeyId() {
            Key keyAnn = this.type.getAnnotation(Key.class);
            if (keyAnn != null) {
                return keyAnn.value();
            }
            return null;
        }

        private boolean checkOnPrototype() {
            return type.getAnnotation(ForeverNotAlone.class) != null;
        }

        Registration as(Class<?> type) {
            this.type = type;
            return this;
        }

        void complete() {
            Implementation impl = new Implementation();
            if (isPrototype) {
                impl.implClass = instance.getClass();
            } else {
                impl.instance = instance;
            }
            regInstances.put(new Identity(type, key), impl);
        }
    }

    private class Identity {
        private Class<?> type;
        private String key;

        Identity(Class<?> type, String key) {
            this.type = type;
            this.key = key;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Identity identity = (Identity) o;
            return com.google.common.base.Objects.equal(type, identity.type) &&
                    com.google.common.base.Objects.equal(key, identity.key);
        }

        @Override
        public int hashCode() {
            return com.google.common.base.Objects.hashCode(type, key);
        }
    }

    private class Implementation {
        private Object instance;
        private Class<?> implClass;
    }
}
