package com.whatsappcrm.doctor_service.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class CreateDoctorLeaveRequest {

    @NotNull
    private Long doctorId;

    @NotNull
    private LocalDate leaveDate;

    // null + null means full-day leave
    private LocalTime startTime;

    private LocalTime endTime;

    private String reason;
}