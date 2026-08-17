package com.whatsappcrm.appointment_service.entity;

import com.whatsappcrm.appointment_service.enums.AppointmentStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(
        name = "appointments",
        indexes = {
                @Index(name = "idx_appointment_tenant", columnList = "tenant_id"),
                @Index(
                        name = "idx_appointment_tenant_patient",
                        columnList = "tenant_id, patient_id"
                ),
                @Index(
                        name = "idx_appointment_tenant_doctor_date",
                        columnList = "tenant_id, doctor_id, appointment_date"
                )
        }
)
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Appointment extends TenantAwareEntity {

    @Column(name = "patient_id", nullable = false)
    private Long patientId;

    @Column(name = "doctor_id", nullable = false)
    private Long doctorId;

    @Column(name = "appointment_date", nullable = false)
    private LocalDate appointmentDate;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AppointmentStatus status;

    private String reason;

    private String notes;
}