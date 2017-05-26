package com.jeeconf;

import com.jeeconf.testing.simple.Presentation;
import com.jeeconf.testing.simple.Training;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;

public class BeansRegistrationTest {

    @Test
    public void shouldReturnRegisteredBean() {
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
    public void shouldReturnRegisteredByTypeBean() {
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
    public void shouldReturnBeanRegisteredAsObject() {
        //GIVEN
        DependenciesConfig config = new DependenciesConfig();
        Training expected = new Training();
        config.register(expected).complete();
        JEEConfInjector injector = new JEEConfInjector(config);
        //WHEN
        Training training = injector.get(Training.class);
        //THEN
        assertThat(training, is(notNullValue()));
    }
}
