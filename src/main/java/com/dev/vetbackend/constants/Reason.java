package com.dev.vetbackend.constants;

public enum Reason {
    ROUTINE_APPOINTMENT("RoutineAppointment"),
    CONTROL_APPOINTMENT("ControlAppointment"),
    DAYCARE("Daycare"),
    HAIRDRESSER("Hairdresser"),
    LABORATORY_TESTS("LaboratoryTests"),
    DERMATOLOGY("Dermatology"),
    CARDIOLOGY("Cardiology"),
    ONCOLOGY("Oncology"),
    SURGERY("Surgery"),
    DENTISTRY("Dentistry"),
    HOSPITALIZATION("Hospitalization");

    private final String value;

    Reason(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

