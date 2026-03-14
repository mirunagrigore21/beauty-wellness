package com.beautywellness.beauty_wellness.model;

//enum care defineste statusurile posibile ale unei programari
public enum AppointmentStatus {


    PENDING("În așteptare"),

    CONFIRMED("Confirmată"),

    COMPLETED("Finalizată"),

    CANCELLED_BY_CLIENT("Anulată de client"),

    CANCELLED_BY_SALON("Anulată de salon"),

    NO_SHOW("Neprezent");

    private final String displayName;

    AppointmentStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}