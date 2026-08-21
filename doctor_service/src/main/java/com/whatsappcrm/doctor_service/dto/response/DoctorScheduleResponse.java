package com.whatsappcrm.doctor_service.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Data
@Builder
public class DoctorScheduleResponse {

    private Long id;
    private Long tenantId;
    private Long doctorId;

    private DayOfWeek dayOfWeek;

    private LocalTime startTime;
    private LocalTime endTime;

    private boolean active;
}