package com.jeeconf;


import com.jeeconf.testing.Presentation;
import com.jeeconf.testing.Training;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

public class BeansRegistrationTest {

    @Test
    public void shouldRegisterBeanWhenClassPassedInConfiguration() {
        //GIVEN
        DependenciesConfig config = new DependenciesConfig();
        config.register(Training.class);
        JEEConfInjector injector = new JEEConfInjector(config);
        //WHEN
        Training training = injector.get(Training.class);

        //THEN
        assertThat(training, is(notNullValue()));
    }

    @Test
    public void shouldRegisterBeanWithConcreteTypeWhenItIsPassedOnRegistration() {
        //GIVEN
        DependenciesConfig config = new DependenciesConfig();
        config.register(Training.class).as(Presentation.class);
        JEEConfInjector injector = new JEEConfInjector(config);
        //WHEN
        Presentation training = injector.get(Presentation.class);
        //THEN
        assertThat(training, is(notNullValue()));
    }

    @Test
    public void shouldRegisterBeanFromObjectInstance() {
        //GIVEN
        DependenciesConfig config = new DependenciesConfig();
        Training registered = new Training();
        config.register(registered);
        JEEConfInjector injector = new JEEConfInjector(config);
        //WHEN
        Training training = injector.get(Training.class);

        //THEN
        assertThat(training, is(registered));
    }

    @Test
    public void shouldRegisterBeanFromObjectInstanceWithConcreteTypeWhenTypeIsPassed() {
        //GIVEN
        DependenciesConfig config = new DependenciesConfig();
        Training registered = new Training();
        config.register(registered).as(Presentation.class);
        JEEConfInjector injector = new JEEConfInjector(config);
        //WHEN
        Presentation training = injector.get(Presentation.class);
        //THEN
        assertThat(training, is(registered));
    }
}
