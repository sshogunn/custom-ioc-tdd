package com.jeeconf;

import com.jeeconf.testing.autosearch.Sponsor;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;

public class BeansAutoSearchTest {

    @Test
    public void shouldRegisterBeanWhenAutoSearchIsEnabled() {
        //GIVEN
        DependenciesConfig config = new DependenciesConfig("com.jeeconf.testing.autosearch");
        //WHEN
        Sponsor sponsor = new JEEConfInjector(config).get(Sponsor.class);
        //THEN
        assertThat(sponsor, is(notNullValue()));
    }
}
