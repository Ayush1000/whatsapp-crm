package com.whatsappcrm.doctor_service.dto.request;

import com.whatsappcrm.doctor_service.enums.DoctorStatus;
import com.whatsappcrm.doctor_service.enums.Specialization;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UpdateDoctorRequest {

    private String firstName;

    private String lastName;

    @Pattern(
            regexp = "^[6-9][0-9]{9}$",
            message = "Mobile number must be a valid 10-digit Indian mobile number"
    )
    private String mobileNumber;

    @Email(message = "Invalid email address")
    private String email;

    private Specialization specialization;

    private String registrationNumber;

    private String qualification;

    @Min(value = 0, message = "Experience cannot be negative")
    private Integer experienceYears;

    private DoctorStatus status;
}