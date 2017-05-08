package com.jeeconf;

import java.util.ArrayList;
import java.util.List;

class DependenciesConfig {
    private List<Class<?>> registeredClasses = new ArrayList<>();

    void register(Class<?> trainingClass) {
        registeredClasses.add(trainingClass);
    }

    <T> Class<T> findClass(Class<T> trainingClass) {
        return (Class<T>) registeredClasses.stream().filter(c -> c.equals(trainingClass)).findFirst().get();
    }
}
