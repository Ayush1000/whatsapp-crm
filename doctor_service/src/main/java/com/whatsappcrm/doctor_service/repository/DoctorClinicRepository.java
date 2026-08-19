package com.whatsappcrm.doctor_service.repository;

import com.whatsappcrm.doctor_service.entity.DoctorClinic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DoctorClinicRepository
        extends JpaRepository<DoctorClinic, Long> {

    Optional<DoctorClinic>
    findByTenantIdAndDoctorId(
            Long tenantId,
            Long doctorId
    );

    boolean existsByTenantIdAndDoctorId(
            Long tenantId,
            Long doctorId
    );

    Page<DoctorClinic> findAllByTenantId(
            Long tenantId,
            Pageable pageable
    );
}