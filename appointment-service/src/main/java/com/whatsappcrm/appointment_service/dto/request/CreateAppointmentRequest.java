package com.whatsappcrm.appointment_service.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
@Getter
@Setter
public class CreateAppointmentRequest {

    @NotNull
    private Long patientId;

    @NotNull
    private Long doctorId;

    @NotNull
    private LocalDate appointmentDate;

    @NotNull
    private LocalTime startTime;

    @NotNull
    private LocalTime endTime;

    private String reason;

    private String notes;
}
