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

@Entity
@Table(
        name = "clinic_holidays",
        indexes = {
                @Index(
                        name = "idx_clinic_holiday",
                        columnList = "tenant_id, holiday_date"
                )
        }
)
@SQLRestriction("deleted = false")
@Getter
@Setter
@NoArgsConstructor
public class ClinicHoliday extends TenantAwareEntity {

    @Column(name = "holiday_date", nullable = false)
    private LocalDate holidayDate;

    private String description;
}
