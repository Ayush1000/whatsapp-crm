package com.whatsappcrm.appointment_service.repository;

import com.whatsappcrm.appointment_service.entity.Appointment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    Optional<Appointment> findByIdAndTenantId(
            Long id,
            Long tenantId
    );

    Page<Appointment> findAllByTenantId(
            Long tenantId,
            Pageable pageable
    );

    @Query("""
    SELECT COUNT(a) > 0
    FROM Appointment a
    WHERE a.tenantId = :tenantId
      AND a.doctorId = :doctorId
      AND a.appointmentDate = :appointmentDate
      AND a.deleted = false
      AND a.status <> 'CANCELLED'
      AND a.startTime < :endTime
      AND a.endTime > :startTime
""")
    boolean hasOverlappingAppointment(
            @Param("tenantId") Long tenantId,
            @Param("doctorId") Long doctorId,
            @Param("appointmentDate") LocalDate appointmentDate,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime
    );

    @Query("""
    SELECT COUNT(a) > 0
    FROM Appointment a
    WHERE a.tenantId = :tenantId
      AND a.doctorId = :doctorId
      AND a.appointmentDate = :appointmentDate
      AND a.deleted = false
      AND a.status <> 'CANCELLED'
      AND a.id <> :appointmentId
      AND a.startTime < :endTime
      AND a.endTime > :startTime
""")
    boolean hasOverlappingAppointmentForUpdate(
            @Param("tenantId") Long tenantId,
            @Param("appointmentId") Long appointmentId,
            @Param("doctorId") Long doctorId,
            @Param("appointmentDate") LocalDate appointmentDate,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime
    );
}
