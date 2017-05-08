package com.jeeconf;

import com.jeeconf.testing.autosearch.Sponsor;
import com.jeeconf.testing.typed.Visitor;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.Is.is;

public class BeansAutoSearchTest {

    @Test
    public void shouldReturnBeanRegisteredByAutoSearch() {
        //GIVEN
        DependenciesConfig config = new DependenciesConfig("com.jeeconf.testing.autosearch");
        JEEConfInjector injector = new JEEConfInjector(config);
        //WHEN
        Sponsor sponsor = injector.get(Sponsor.class);
        //THEN
        assertThat(sponsor, is(notNullValue()));
    }

    @Test
    public void shouldReturnBeanRegisteredWithTypeByAutoSearch() {
        //GIVEN
        DependenciesConfig config = new DependenciesConfig("com.jeeconf.testing.typed");
        JEEConfInjector injector = new JEEConfInjector(config);
        //WHEN
        Visitor speaker = injector.get(Visitor.class);
        //THEN
        assertThat(speaker, is(notNullValue()));
    }
}
