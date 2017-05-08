package com.jeeconf;

import java.util.HashMap;
import java.util.Map;

class DependenciesConfig {
    private Map<Class<?>, Class<?>> registeredClasses = new HashMap<>();

    Registration register(Class<?> trainingClass) {
        registeredClasses.put(trainingClass, trainingClass);
        return new Registration(trainingClass);
    }

    <T> Class<T> findClass(Class<T> trainingClass) {
        Class<T> key = (Class<T>) registeredClasses.keySet().stream().filter(c -> c.equals(trainingClass)).findFirst().get();
        return (Class<T>) registeredClasses.get(key);
    }

    class Registration {
        private final Class<?> trainingClass;

        Registration(Class<?> trainingClass) {
            this.trainingClass = trainingClass;
        }

        void as(Class<?> type) {
            registeredClasses.put(type, trainingClass);
        }
    }
}
