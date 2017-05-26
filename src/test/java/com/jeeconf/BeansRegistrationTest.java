package com.jeeconf;

import com.jeeconf.testing.simple.Training;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.Is.is;

public class BeansRegistrationTest {

    @Test
    public void shouldReturnRegisteredBean() {
        //GIVEN
        DependenciesConfig config = new DependenciesConfig();
        config.register(Training.class);
        JEEConfInjector injector = new JEEConfInjector(config);
        //WHEN
        Training training = injector.get(Training.class);
        //THEN
        assertThat(training, is(notNullValue()));
    }
}
