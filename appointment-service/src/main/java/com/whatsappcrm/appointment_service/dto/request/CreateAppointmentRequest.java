package com.whatsappcrm.appointment_service.dto.request;

import com.whatsappcrm.appointment_service.enums.AppointmentType;
import com.whatsappcrm.appointment_service.enums.VisitType;
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

    @NotNull(message = "Visit type is required")
    private VisitType visitType;

    @NotNull
    private AppointmentType appointmentType = AppointmentType.SCHEDULED;

    private String overrideReason;
}
