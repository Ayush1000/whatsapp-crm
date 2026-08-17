package com.whatsappcrm.appointment_service.dto.response;

import com.whatsappcrm.appointment_service.enums.AppointmentStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
@Getter
@Builder
public class AppointmentResponse {

    private Long id;

    private Long tenantId;

    private Long patientId;

    private Long doctorId;

    private LocalDate appointmentDate;

    private LocalTime startTime;

    private LocalTime endTime;

    private AppointmentStatus status;

    private String reason;

    private String notes;

    private LocalDateTime createdDate;

    private LocalDateTime modifiedDate;
}
