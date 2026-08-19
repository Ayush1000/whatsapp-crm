package com.whatsappcrm.doctor_service.dto.response;

import com.whatsappcrm.doctor_service.enums.DoctorClinicStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DoctorClinicResponse {

    private Long id;
    private Long tenantId;
    private Long doctorId;
    private DoctorClinicStatus status;
    private Integer defaultAppointmentDurationMinutes;
}