package com.jeeconf.testing.lunch;

import com.jeeconf.annotations.AutoSearch;
import com.jeeconf.annotations.JEEConfComponent;
import com.jeeconf.annotations.Key;

@JEEConfComponent
public class Lunch {
    private Starter starter;

    @AutoSearch
    public Lunch(@Key("best-soup") Starter starter) {
        this.starter = starter;
    }

    public Starter getStarter() {
        return starter;
    }
}
