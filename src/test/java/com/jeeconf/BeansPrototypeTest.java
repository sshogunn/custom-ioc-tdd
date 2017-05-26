package com.jeeconf;

import com.jeeconf.testing.scope.Prototype;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class BeansPrototypeTest {

    @Test
    public void shouldReturnDifferentBeanInstancesWhenClassMarkedAsPrototype() {
        //GIVEN
        DependenciesConfig config = new DependenciesConfig("com.jeeconf.testing.scope");
        JEEConfInjector injector = new JEEConfInjector(config);
        //WHEN
        Prototype first = injector.get(Prototype.class);
        Prototype second = injector.get(Prototype.class);
        //THEN
        assertThat(first, is(not(second)));
    }
}
