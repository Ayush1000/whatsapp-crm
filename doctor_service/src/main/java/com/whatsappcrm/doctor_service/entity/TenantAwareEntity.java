package com.whatsappcrm.doctor_service.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@MappedSuperclass
public abstract class TenantAwareEntity extends com.whatsappcrm.doctor_service.entity.BaseEntity {

    @Column(name = "tenant_id", nullable = false)
    protected Long tenantId;
}