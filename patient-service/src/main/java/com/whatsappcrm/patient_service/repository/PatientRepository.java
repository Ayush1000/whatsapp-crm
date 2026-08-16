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
    Optional<Patient> findByMobileNumber(String mobileNumber);

    boolean existsByMobileNumber(String mobileNumber);

    Page<Patient> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrMobileNumberContaining(
            String firstName,
            String lastName,
            String mobileNumber,
            Pageable pageable
    );
    @Query(
            value = "SELECT * FROM patients WHERE id = :id",
            nativeQuery = true
    )
    Optional<Patient> findByIdIncludingDeleted(@Param("id") Long id);

}
