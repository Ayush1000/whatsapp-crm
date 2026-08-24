package com.whatsappcrm.doctor_service.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DoctorAvailabilityResponse {

    private Long doctorId;
    private boolean available;
    private String reason;
}
