package com.whatsappcrm.patient_service.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDate;

@Entity
@Table(name = "patients")
@SQLRestriction("deleted = false")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Patient extends BaseEntity {
    @Column(nullable = false)
    private String firstName;

    private String lastName;

    @Column(nullable = false, unique = true)

    private String mobileNumber;

    @Email(message = "Invalid email address")
    private String email;

    private LocalDate dateOfBirth;

    private String gender;

    private String bloodGroup;

    private String address;

    private String city;

    private String state;

    private String pinCode;

    private Boolean active = true;

}
