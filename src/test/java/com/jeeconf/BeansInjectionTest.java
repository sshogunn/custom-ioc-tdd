package com.jeeconf;

import com.jeeconf.testing.injection.Organizer;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;


public class BeansInjectionTest {

    @Test
    public void shouldReturnBeanWithDependency() {
        //GIVEN
        DependenciesConfig config = new DependenciesConfig("com.jeeconf.testing.injection");
        //WHEN
        Organizer organizer = new JEEConfInjector(config).get(Organizer.class);
        //THEN
        assertThat(organizer, is(notNullValue()));
        assertThat(organizer.getAssistant(), is(notNullValue()));
    }
}
