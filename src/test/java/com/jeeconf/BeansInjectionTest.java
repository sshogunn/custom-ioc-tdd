package com.jeeconf;

import com.jeeconf.testing.autosearch.Organizer;
import com.jeeconf.testing.lunch.Borsh;
import com.jeeconf.testing.lunch.Lunch;
import com.jeeconf.testing.lunch.TomatoSoup;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;

public class BeansInjectionTest {

    @Test
    public void shouldReturnBeanWithOneDependency() {
        //GIVEN
        DependenciesConfig config = new DependenciesConfig("com.jeeconf.testing.autosearch");
        JEEConfInjector injector = new JEEConfInjector(config);
        //WHEN
        Organizer lunch = injector.get(Organizer.class);
        //THEN
        assertThat(lunch, is(notNullValue()));
        assertThat(lunch.getSponsor(), is(notNullValue()));
    }


    @Test
    public void shouldReturnBeanWithDependencyIdentifiedByKey() {
        //GIVEN
        DependenciesConfig config = new DependenciesConfig("com.jeeconf.testing.lunch");
        JEEConfInjector injector = new JEEConfInjector(config);
        //WHEN
        Lunch lunch = injector.get(Lunch.class);
        //THEN
        assertThat(lunch, is(notNullValue()));
        assertThat(lunch.getStarter(), instanceOf(Borsh.class));
    }
}
