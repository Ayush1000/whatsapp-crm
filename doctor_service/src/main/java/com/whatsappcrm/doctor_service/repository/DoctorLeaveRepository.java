package com.whatsappcrm.doctor_service.repository;

import com.whatsappcrm.doctor_service.entity.DoctorLeave;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorLeaveRepository
        extends JpaRepository<DoctorLeave, Long> {

    List<DoctorLeave>
    findByTenantIdAndDoctorIdAndLeaveDate(
            Long tenantId,
            Long doctorId,
            LocalDate leaveDate
    );
    List<DoctorLeave>
    findByTenantIdAndDoctorId(
            Long tenantId,
            Long doctorId
    );

    Optional<DoctorLeave>
    findByIdAndTenantId(
            Long id,
            Long tenantId
    );
}
