package com.whatsappcrm.doctor_service.entity;

import com.whatsappcrm.doctor_service.enums.DoctorClinicStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(
        name = "doctor_clinics",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_doctor_clinic",
                        columnNames = {
                                "tenant_id",
                                "doctor_id"
                        }
                )
        },
        indexes = {
                @Index(
                        name = "idx_doctor_clinic_tenant",
                        columnList = "tenant_id"
                ),
                @Index(
                        name = "idx_doctor_clinic_doctor",
                        columnList = "doctor_id"
                )
        }
)
@SQLRestriction("deleted = false")
@Getter
@Setter
@NoArgsConstructor
public class DoctorClinic extends TenantAwareEntity {

    @Column(name = "doctor_id", nullable = false)
    private Long doctorId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DoctorClinicStatus status =
            DoctorClinicStatus.ACTIVE;

    @Column(name = "default_appointment_duration_minutes")
    private Integer defaultAppointmentDurationMinutes = 30;
}