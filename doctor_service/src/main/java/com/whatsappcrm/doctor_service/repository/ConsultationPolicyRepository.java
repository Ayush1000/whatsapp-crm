package com.whatsappcrm.doctor_service.repository;

import com.whatsappcrm.doctor_service.entity.ConsultationPolicy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConsultationPolicyRepository extends JpaRepository<ConsultationPolicy, Long> {

        Optional<ConsultationPolicy>
    findByTenantIdAndDoctorId(
            Long tenantId,
            Long doctorId
            );
            }
