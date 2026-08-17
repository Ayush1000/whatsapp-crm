package com.whatsappcrm.appointment_service.dto.request;

import com.whatsappcrm.appointment_service.enums.AppointmentStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
@Getter
@Setter
public class UpdateAppointmentRequest {

    private LocalDate appointmentDate;

    private LocalTime startTime;

    private LocalTime endTime;

    private Long doctorId;

    private String reason;

    private String notes;

    private AppointmentStatus status;
}
