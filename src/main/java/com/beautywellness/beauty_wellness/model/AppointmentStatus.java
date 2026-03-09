package com.beautywellness.beauty_wellness.model;

//Enum definește statusurile posibile ale unei programări
public enum AppointmentStatus {

    //Programare în așteptare
    PENDING,
    //Programare confirmată
    CONFIRMED,
    //Programare finalizată
    COMPLETED,
    //Programare anulată de client
    CANCELLED_BY_CLIENT,
    //Programare anulată de salon
    CANCELLED_BY_SALON,
    //Client neprezent
    NO_SHOW
}