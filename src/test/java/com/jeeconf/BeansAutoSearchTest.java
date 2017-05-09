package com.jeeconf;

import com.jeeconf.testing.autosearch.Sponsor;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;

public class BeansAutoSearchTest {


    @Test
    public void shouldAutSearchBeansWhenAutoSearchIsEnabled() {
        //GIVEN
        DependenciesConfig config = new DependenciesConfig(true,  "com.jeeconf.testing.autosearch");
        JEEConfInjector injector = new JEEConfInjector(config);
        //WHEN
        Sponsor sponsor = injector.get(Sponsor.class);
        //THEN
        assertThat(sponsor, is(notNullValue()));
    }
}
