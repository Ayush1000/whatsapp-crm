package com.whatsappcrm.doctor_service.repository;

import com.whatsappcrm.doctor_service.entity.DoctorSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorScheduleRepository
        extends JpaRepository<DoctorSchedule, Long> {

    List<DoctorSchedule>
    findByTenantIdAndDoctorIdAndDayOfWeekAndActiveTrue(
            Long tenantId,
            Long doctorId,
            DayOfWeek dayOfWeek
    );

    List<DoctorSchedule>
    findByTenantIdAndDoctorIdAndActiveTrue(
            Long tenantId,
            Long doctorId
    );

    Optional<DoctorSchedule>
    findByIdAndTenantId(
            Long id,
            Long tenantId
    );
    @Query("""
       SELECT COUNT(s) > 0
       FROM DoctorSchedule s
       WHERE s.tenantId = :tenantId
       AND s.doctorId = :doctorId
       AND s.dayOfWeek = :dayOfWeek
       AND s.active = true
       AND s.startTime < :endTime
       AND s.endTime > :startTime
       """)
    boolean hasOverlappingSchedule(
            @Param("tenantId") Long tenantId,
            @Param("doctorId") Long doctorId,
            @Param("dayOfWeek") DayOfWeek dayOfWeek,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime
    );
}
