package com.jeeconf;


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
}
