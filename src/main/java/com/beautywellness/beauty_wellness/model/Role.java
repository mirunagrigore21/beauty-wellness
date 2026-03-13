package com.beautywellness.beauty_wellness.model;

//enum care defineste rolurile utilizatorului aplicatiei
public enum Role {

    ADMIN("Administrator"),
    EMPLOYEE("Angajat"),
    CLIENT("Client");

    private final String displayName;

    Role(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}