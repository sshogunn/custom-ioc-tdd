package com.jeeconf.testing.autosearch;

import com.jeeconf.annotations.AutoSearch;
import com.jeeconf.annotations.JEEConfComponent;

@JEEConfComponent
public class Organizer {
    private Sponsor sponsor;

    @AutoSearch
    public Organizer(Sponsor sponsor) {
        this.sponsor = sponsor;
    }

    public Sponsor getSponsor() {
        return sponsor;
    }
}
