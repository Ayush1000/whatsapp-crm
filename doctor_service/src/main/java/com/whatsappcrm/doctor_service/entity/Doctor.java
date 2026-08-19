package com.whatsappcrm.doctor_service.entity;

import com.whatsappcrm.doctor_service.enums.DoctorStatus;
import com.whatsappcrm.doctor_service.enums.Specialization;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(
        name = "doctors",
        indexes = {
                @Index(
                        name = "idx_doctor_registration",
                        columnList = "registration_number"
                )
        }
)
@SQLRestriction("deleted = false")
@Getter
@Setter
@NoArgsConstructor
public class Doctor extends BaseEntity {

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "mobile_number", nullable = false)
    private String mobileNumber;

    @Column(name = "email")
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "specialization", nullable = false)
    private Specialization specialization;

    @Column(
            name = "registration_number",
            nullable = false,
            unique = true
    )
    private String registrationNumber;

    private String qualification;

    @Column(name = "experience_years")
    private Integer experienceYears;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private DoctorStatus status = DoctorStatus.ACTIVE;
}