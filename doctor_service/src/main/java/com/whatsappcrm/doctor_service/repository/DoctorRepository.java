package com.whatsappcrm.doctor_service.repository;

import com.whatsappcrm.doctor_service.entity.Doctor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DoctorRepository
        extends JpaRepository<Doctor, Long> {

    Optional<Doctor> findByRegistrationNumber(
            String registrationNumber
    );

    boolean existsByRegistrationNumber(
            String registrationNumber
    );
    @Query("""
       SELECT d
       FROM Doctor d
       WHERE d.id IN (
           SELECT dc.doctorId
           FROM DoctorClinic dc
           WHERE dc.tenantId = :tenantId
           AND dc.status = 'ACTIVE'
       )
       """)
    Page<Doctor> findAllDoctorsByTenantId(
            @Param("tenantId") Long tenantId,
            Pageable pageable
    );
}