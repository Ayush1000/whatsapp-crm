package com.whatsappcrm.doctor_service.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
public class DoctorLeaveResponse {

    private Long id;
    private Long tenantId;
    private Long doctorId;

    private LocalDate leaveDate;

    private LocalTime startTime;
    private LocalTime endTime;

    private String reason;
}