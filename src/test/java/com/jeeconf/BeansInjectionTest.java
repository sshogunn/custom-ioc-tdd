package com.jeeconf;

import com.jeeconf.testing.autosearch.Organizer;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

public class BeansInjectionTest {

    @Test
    public void shouldReturnBeanWithOneDependency() {
        //GIVEN
        DependenciesConfig dependenciesConfig = new DependenciesConfig(true, "com.jeeconf.testing.autosearch");
        JEEConfInjector injector = new JEEConfInjector(dependenciesConfig);
        //WHEN
        Organizer organizer = injector.get(Organizer.class);
        //THEN
        assertThat(organizer, is(notNullValue()));
        assertThat(organizer.getSponsor(),  is(notNullValue()));
    }
}
