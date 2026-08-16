package com.whatsappcrm.patient_service.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;
@Data
public class UpdatePatientRequest {
    @Size(max = 100, message = "First name cannot exceed 100 characters")
    private String firstName;

    @Size(max = 100, message = "Last name cannot exceed 100 characters")
    private String lastName;

    @Pattern(
            regexp = "^[6-9][0-9]{9}$",
            message = "Mobile number must be a valid 10-digit Indian mobile number"
    )
    private String mobileNumber;

    @Email(message = "Invalid email address")
    @Size(max = 255, message = "Email cannot exceed 255 characters")
    private String email;

    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;
    private String gender;
    private String bloodGroup;
    private String address;
    private String city;
    private String state;
    @Pattern(
            regexp = "^[1-9][0-9]{5}$",
            message = "Pin code must be a valid 6-digit number"
    )
    private String pinCode;
}
