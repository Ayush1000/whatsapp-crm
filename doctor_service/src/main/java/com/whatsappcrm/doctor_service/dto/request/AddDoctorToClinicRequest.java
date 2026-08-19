package com.whatsappcrm.doctor_service.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddDoctorToClinicRequest {

    @NotNull
    private Long doctorId;

    @Min(5)
    private Integer defaultAppointmentDurationMinutes;
}