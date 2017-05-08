package com.jeeconf;

class JEEConfInjector {
    private final DependenciesConfig config;

    JEEConfInjector(DependenciesConfig config) {
        this.config = config;
    }

    <T> T get(Class<T> trainingClass) {
        Class<T> registeredClass = config.findClass(trainingClass);
        try {
            return registeredClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
