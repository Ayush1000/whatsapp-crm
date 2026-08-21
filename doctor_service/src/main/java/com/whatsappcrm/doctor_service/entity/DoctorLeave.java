package com.whatsappcrm.doctor_service.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(
        name = "doctor_leaves",
        indexes = {
                @Index(
                        name = "idx_doctor_leave_lookup",
                        columnList = "tenant_id, doctor_id, leave_date"
                )
        }
)
@SQLRestriction("deleted = false")
@Getter
@Setter
@NoArgsConstructor
public class DoctorLeave extends TenantAwareEntity {

    @Column(name = "doctor_id", nullable = false)
    private Long doctorId;

    @Column(name = "leave_date", nullable = false)
    private LocalDate leaveDate;

    @Column(name = "start_time")
    private LocalTime startTime;

    @Column(name = "end_time")
    private LocalTime endTime;

    private String reason;
}
