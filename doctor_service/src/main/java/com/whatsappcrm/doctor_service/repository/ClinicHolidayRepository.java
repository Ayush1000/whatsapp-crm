package com.whatsappcrm.doctor_service.repository;

import com.whatsappcrm.doctor_service.entity.ClinicHoliday;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ClinicHolidayRepository
        extends JpaRepository<ClinicHoliday, Long> {

    boolean existsByTenantIdAndHolidayDate(
            Long tenantId,
            LocalDate holidayDate
    );
    List<ClinicHoliday> findByTenantId(
            Long tenantId
    );

    Optional<ClinicHoliday>
    findByIdAndTenantId(
            Long id,
            Long tenantId
    );
}