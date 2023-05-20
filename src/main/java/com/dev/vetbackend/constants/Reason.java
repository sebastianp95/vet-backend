package com.dev.vetbackend.constants;

public enum Reason {
    ROUTINE_APPOINTMENT("RoutineAppointment"),
    DAYCARE("Daycare"),
    HAIRDRESSER("Hairdresser");

    private final String value;

    Reason(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

