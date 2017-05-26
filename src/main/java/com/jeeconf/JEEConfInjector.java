package com.jeeconf;

class JEEConfInjector {

    private DependenciesConfig config;

    JEEConfInjector(DependenciesConfig config) {
        this.config = config;
    }

    <T> T get(Class<T> type) {
        return config.findInstance(type);
    }
}
