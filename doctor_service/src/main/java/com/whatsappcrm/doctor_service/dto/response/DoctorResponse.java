package com.whatsappcrm.doctor_service.dto.response;

import com.whatsappcrm.doctor_service.enums.DoctorStatus;
import com.whatsappcrm.doctor_service.enums.Specialization;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class DoctorResponse {

    private Long id;

    private String firstName;
    private String lastName;
    private String mobileNumber;
    private String email;

    private Specialization specialization;
    private String registrationNumber;
    private String qualification;
    private Integer experienceYears;

    private DoctorStatus status;

    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
}