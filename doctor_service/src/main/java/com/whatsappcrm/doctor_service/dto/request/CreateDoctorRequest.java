package com.whatsappcrm.doctor_service.dto.request;

import com.whatsappcrm.doctor_service.enums.Specialization;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class CreateDoctorRequest {

    @NotBlank
    private String firstName;

    private String lastName;

    private String mobileNumber;

    @Email
    private String email;

    @NotNull
    private Specialization specialization;

    @NotBlank
    private String registrationNumber;

    private String qualification;

    @Min(0)
    private Integer experienceYears;
}