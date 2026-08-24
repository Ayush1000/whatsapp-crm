package com.whatsappcrm.appointment_service.dto.response;

import lombok.Data;

@Data
public class DoctorAvailabilityResponse {

    private Long doctorId;
    private boolean available;
    private String reason;
}
