package com.jeeconf.testing.autosearch;

import com.jeeconf.annotations.JEEConfComponent;

@JEEConfComponent
public class Organizer {

    private Sponsor sponsor;

    public Organizer(Sponsor sponsor) {
        this.sponsor = sponsor;
    }

    public Sponsor getSponsor() {
        return sponsor;
    }
}
