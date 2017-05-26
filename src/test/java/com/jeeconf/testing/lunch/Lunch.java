package com.jeeconf.testing.lunch;

import com.jeeconf.annotations.AutoSearch;
import com.jeeconf.annotations.JEEConfComponent;

@JEEConfComponent
public class Lunch {
    private Starter starter;

    @AutoSearch
    public Lunch(Starter starter) {
        this.starter = starter;
    }

    public Starter getStarter() {
        return starter;
    }
}
