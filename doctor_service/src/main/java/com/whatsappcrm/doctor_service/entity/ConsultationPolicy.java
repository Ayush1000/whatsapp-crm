package com.whatsappcrm.doctor_service.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;

import java.math.BigDecimal;

@Entity
@Table(
        name = "consultation_policies",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_consultation_policy_doctor_clinic",
                        columnNames = {
                                "tenant_id",
                                "doctor_id"
                        }
                )
        }
)
@SQLRestriction("deleted = false")
@Getter
@Setter
@NoArgsConstructor
public class ConsultationPolicy extends TenantAwareEntity {

    @Column(name = "doctor_id", nullable = false)
    private Long doctorId;

    @Column(
            name = "consultation_fee",
            nullable = false,
            precision = 10,
            scale = 2
    )
    private BigDecimal consultationFee;

    @Column(name = "free_follow_up_days", nullable = false)
    private Integer freeFollowUpDays = 0;

    @Column(name = "report_review_free", nullable = false)
    private boolean reportReviewFree = true;

    @Column(
            name = "follow_up_fee",
            precision = 10,
            scale = 2
    )
    private BigDecimal followUpFee;
}