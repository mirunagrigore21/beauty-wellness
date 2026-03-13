package com.beautywellness.beauty_wellness.model;

//enum care defineste functiile pe care le poate avea un angajat
public enum EmployeeRole {

    //beauty
    HAIR_STYLIST("Coafor"),
    MANICURIST("Manichiuristă"),
    PEDICURIST("Pedichiuristă"),
    COSMETICIAN("Cosmeticiană"),
    MAKEUP_ARTIST("Make-up artist"),
    EYEBROW_SPECIALIST("Specialist sprâncene"),
    EYELASH_TECHNICIAN("Tehnician gene"),

    //wellness
    MASSAGE_THERAPIST("Terapeut masaj"),
    SPA_THERAPIST("Terapeut spa"),
    AROMATHERAPIST("Aromaterapist"),
    REFLEXOLOGIST("Reflexolog"),


    RECEPTIONIST("Recepționist"),
    MANAGER("Manager");

    private final String displayName;

    EmployeeRole(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}