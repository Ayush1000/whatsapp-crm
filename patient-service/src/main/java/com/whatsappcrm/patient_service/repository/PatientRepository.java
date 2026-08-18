package com.whatsappcrm.patient_service.repository;

import com.whatsappcrm.patient_service.entity.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface PatientRepository extends JpaRepository<Patient,Long> {

    Optional<Patient> findByTenantIdAndId(
            Long tenantId,
            Long id
    );
    Optional<Patient> findByTenantIdAndMobileNumber(
            Long tenantId,
            String mobileNumber
    );

    boolean existsByTenantIdAndMobileNumber(
            Long tenantId,
            String mobileNumber
    );

    Page<Patient> findByTenantIdAndFirstNameContainingIgnoreCaseOrTenantIdAndLastNameContainingIgnoreCaseOrTenantIdAndMobileNumberContaining(
            Long tenantId1,
            String firstName,
            Long tenantId2,
            String lastName,
            Long tenantId3,
            String mobileNumber,
            Pageable pageable
    );
    @Query("""
    SELECT p
    FROM Patient p
    WHERE p.tenantId = :tenantId
      AND (
           LOWER(p.firstName) LIKE LOWER(CONCAT('%', :keyword, '%'))
        OR LOWER(p.lastName) LIKE LOWER(CONCAT('%', :keyword, '%'))
        OR p.mobileNumber LIKE CONCAT('%', :keyword, '%')
      )
""")
    Page<Patient> searchPatients(
            @Param("tenantId") Long tenantId,
            @Param("keyword") String keyword,
            Pageable pageable
    );

    Page<Patient> findAllByTenantId(Long tenantId,Pageable pageable);
    @Query(
            value = "SELECT * FROM patients WHERE id = :id",
            nativeQuery = true
    )
    Optional<Patient> findByIdIncludingDeleted(@Param("id") Long id);

}
