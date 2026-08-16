package com.whatsappcrm.patient_service.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
@Data
@Builder
public class PatientResponse {
    private Long id;

    private String firstName;
    private String lastName;
    private String mobileNumber;
    private String email;
    private LocalDate dateOfBirth;
    private String gender;
    private String bloodGroup;
    private String address;
    private String city;
    private String state;
    private String pinCode;
}
