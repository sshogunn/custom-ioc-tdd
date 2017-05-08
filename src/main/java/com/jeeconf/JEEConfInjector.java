package com.jeeconf;

class JEEConfInjector {
    private final DependenciesConfig config;

    JEEConfInjector(DependenciesConfig config) {
        this.config = config;
    }

    <T> T get(Class<T> clazz) {
        return config.findInstance(clazz);
    }
}
