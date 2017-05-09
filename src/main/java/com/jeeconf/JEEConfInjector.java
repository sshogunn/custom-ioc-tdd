package com.jeeconf;

class JEEConfInjector {
    private final DependenciesConfig config;

    JEEConfInjector(DependenciesConfig config) {
        this.config = config;
    }

    <T> T get(Class<T> trainingClass) {
        T instance = config.findInstance(trainingClass);
        if (instance == null) {
            return createNewInstance(trainingClass);
        } else {
            return instance;
        }
    }

    private <T> T createNewInstance(Class<T> clazz) {
        Class<T> registeredClass = config.findClass(clazz);
        try {
            return registeredClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
