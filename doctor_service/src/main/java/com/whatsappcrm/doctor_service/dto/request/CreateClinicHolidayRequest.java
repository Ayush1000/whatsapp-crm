package com.whatsappcrm.doctor_service.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CreateClinicHolidayRequest {

    @NotNull
    private LocalDate holidayDate;

    private String description;
}