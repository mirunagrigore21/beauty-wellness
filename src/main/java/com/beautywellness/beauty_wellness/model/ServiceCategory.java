package com.beautywellness.beauty_wellness.model;

//enum care defineste categoriile de servicii oferit
public enum ServiceCategory {

    //beauty
    HAIR("Servicii păr"),
    NAILS("Servicii unghii"),
    MAKEUP("Machiaj"),
    SKIN_CARE("Îngrijire ten"),
    EYEBROWS("Sprâncene"),
    EYELASHES("Gene"),

    //wellness
    MASSAGE("Masaj"),
    SPA("Spa"),
    AROMATHERAPY("Aromaterapie"),
    REFLEXOLOGY("Reflexologie");

    private final String displayName;

    ServiceCategory(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}