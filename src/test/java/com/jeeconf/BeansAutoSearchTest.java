package com.jeeconf;

import com.jeeconf.testing.autosearch.Sponsor;
import com.jeeconf.testing.autosearch.typed.Speaker;
import com.jeeconf.testing.autosearch.typed.Visitor;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;

public class BeansAutoSearchTest {


    @Test
    public void shouldAutoSearchBeansWhenAutoSearchIsEnabled() {
        //GIVEN
        DependenciesConfig config = new DependenciesConfig(true,  "com.jeeconf.testing.autosearch");
        JEEConfInjector injector = new JEEConfInjector(config);
        //WHEN
        Sponsor sponsor = injector.get(Sponsor.class);
        //THEN
        assertThat(sponsor, is(notNullValue()));
    }

    @Test
    public void shouldRegisterBeanWithSpecifiedTypeWhenItIsMentionedInMetadata() {
        //GIVEN
        DependenciesConfig config = new DependenciesConfig(true,  "com.jeeconf.testing.autosearch.typed");
        JEEConfInjector injector = new JEEConfInjector(config);
        //WHEN
        Visitor speaker = injector.get(Visitor.class);
        //THEN
        assertThat(speaker, is(notNullValue()));
    }
}
