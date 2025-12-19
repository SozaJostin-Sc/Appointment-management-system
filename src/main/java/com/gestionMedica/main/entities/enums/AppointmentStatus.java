package com.gestionMedica.main.entities.enums;

import lombok.Getter;

@Getter
public enum AppointmentStatus {
    SCHEDULED("scheduled"),
    CONFIRMED("confirmed"),
    IN_PROGRESS("in_progress"),
    COMPLETED("completed"),
    CANCELLED("cancelled"),
    NO_SHOW("no_show");

    private final String value;

    AppointmentStatus(String value) {
        this.value = value;
    }

    public static AppointmentStatus fromValue(String value) {
        for (AppointmentStatus status : values()) {
            if (status.value.equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown appointment status: " + value);
    }
}