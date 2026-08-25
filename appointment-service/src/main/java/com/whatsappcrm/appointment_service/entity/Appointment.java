package com.whatsappcrm.appointment_service.entity;

import com.whatsappcrm.appointment_service.enums.AppointmentStatus;
import com.whatsappcrm.appointment_service.enums.AppointmentType;
import com.whatsappcrm.appointment_service.enums.VisitType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

    @Enumerated(EnumType.STRING)
    @Column(name = "visit_type", nullable = false)
    private VisitType visitType;

    @Column(
            name = "consultation_fee",
            nullable = false,
            precision = 10,
            scale = 2
    )
    private BigDecimal consultationFee;

    @Enumerated(EnumType.STRING)
    @Column(name = "appointment_type", nullable = false)
    private AppointmentType appointmentType;

    @Column(name = "availability_override", nullable = false)
    private boolean availabilityOverride = false;

    @Column(name = "override_reason")
    private String overrideReason;

    @Column(name = "checked_in_at")
    private LocalDateTime checkedInAt;

    @Column(name = "actual_start_at")
    private LocalDateTime actualStartAt;

    @Column(name = "actual_end_at")
    private LocalDateTime actualEndAt;

    @Column(name = "starts_follow_up_window", nullable = false)
    private boolean startsFollowUpWindow = false;
}