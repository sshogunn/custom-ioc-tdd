package com.jeeconf;

import com.jeeconf.testing.simple.Presentation;
import com.jeeconf.testing.simple.Training;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.Is.is;

public class BeansRegistrationTest {

    @Test
    public void shouldReturnBeanRegisteredInConfiguration() {
        //GIVEN
        DependenciesConfig config = new DependenciesConfig();
        config.register(Training.class).complete();
        JEEConfInjector injector = new JEEConfInjector(config);
        //WHEN
        Training training = injector.get(Training.class);
        //THEN
        assertThat(training, is(notNullValue()));
    }

    @Test
    public void shouldReturnBeanRegisteredWithSpecifiedType() {
        //GIVEN
        DependenciesConfig config = new DependenciesConfig();
        config.register(Training.class).as(Presentation.class).complete();
        JEEConfInjector injector = new JEEConfInjector(config);
        //WHEN
        Presentation training = injector.get(Presentation.class);
        //THEN
        assertThat(training, is(notNullValue()));
    }

    @Test
    public void shouldReturnBeanRegisteredFromObjectInstance() {
        //GIVEN
        DependenciesConfig config = new DependenciesConfig();
        Training toBeRegistered = new Training();
        config.register(toBeRegistered).complete();
        JEEConfInjector injector = new JEEConfInjector(config);
        //WHEN
        Training training = injector.get(Training.class);
        //THEN
        assertThat(training, is(toBeRegistered));
    }
}
