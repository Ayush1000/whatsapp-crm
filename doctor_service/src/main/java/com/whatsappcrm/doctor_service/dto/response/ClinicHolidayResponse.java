package com.whatsappcrm.doctor_service.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class ClinicHolidayResponse {

    private Long id;
    private Long tenantId;

    private LocalDate holidayDate;

    private String description;
}