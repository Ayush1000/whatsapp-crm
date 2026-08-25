package com.whatsappcrm.appointment_service.service.impl;

import com.whatsappcrm.appointment_service.client.DoctorClient;
import com.whatsappcrm.appointment_service.client.PatientClient;
import com.whatsappcrm.appointment_service.dto.request.CreateAppointmentRequest;
import com.whatsappcrm.appointment_service.dto.request.UpdateAppointmentRequest;
import com.whatsappcrm.appointment_service.dto.response.AppointmentResponse;
import com.whatsappcrm.appointment_service.dto.response.ConsultationPolicyResponse;
import com.whatsappcrm.appointment_service.dto.response.DoctorAvailabilityResponse;
import com.whatsappcrm.appointment_service.entity.Appointment;
import com.whatsappcrm.appointment_service.enums.AppointmentStatus;
import com.whatsappcrm.appointment_service.enums.AppointmentType;
import com.whatsappcrm.appointment_service.enums.VisitType;
import com.whatsappcrm.appointment_service.exception.AppointmentNotFoundException;
import com.whatsappcrm.appointment_service.exception.DoctorUnavailableException;
import com.whatsappcrm.appointment_service.exception.ResourceAlreadyExistsException;
import com.whatsappcrm.appointment_service.mapper.AppointmentMapper;
import com.whatsappcrm.appointment_service.repository.AppointmentRepository;
import com.whatsappcrm.appointment_service.service.interfaces.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl
        implements AppointmentService {

    private final AppointmentRepository repository;
    private final PatientClient patientClient;
    private final DoctorClient doctorClient;
    private Long getCurrentTenantId() {
        return 1L;
    }

    @Override
    @Transactional
    public AppointmentResponse createAppointment(
            CreateAppointmentRequest request) {

        Long tenantId = getCurrentTenantId();

        /*
         * 1. Validate patient
         */
        patientClient.validatePatientExists(
                request.getPatientId()
        );

        /*
         * 2. Validate doctor belongs to current clinic
         */
        doctorClient.validateDoctorExists(
                request.getDoctorId()
        );

        /*
         * 3. Basic time validation
         */
        if (!request.getStartTime()
                .isBefore(request.getEndTime())) {

            throw new IllegalArgumentException(
                    "Start time must be before end time"
            );
        }

        /*
         * 4. Check doctor's configured availability
         *
         * This checks:
         * - clinic holiday
         * - doctor schedule
         * - doctor leave
         */
        DoctorAvailabilityResponse availability =
                doctorClient.checkAvailability(
                        request.getDoctorId(),
                        request.getAppointmentDate(),
                        request.getStartTime(),
                        request.getEndTime()
                );

        boolean emergency =
                request.getAppointmentType()
                        == AppointmentType.EMERGENCY;

        boolean doctorUnavailable =
                availability == null
                        || !availability.isAvailable();

        /*
         * Normal appointment:
         * doctor MUST be available.
         *
         * Emergency:
         * availability can be overridden.
         */
        if (doctorUnavailable && !emergency) {

            String reason =
                    availability != null
                            ? availability.getReason()
                            : "UNKNOWN";

            throw new DoctorUnavailableException(
                    "Doctor is not available for requested slot. Reason: "
                            + reason
            );
        }

        /*
         * 5. Check whether doctor already has another appointment.
         */
        boolean appointmentConflict =
                repository.hasOverlappingAppointment(
                        tenantId,
                        request.getDoctorId(),
                        request.getAppointmentDate(),
                        request.getStartTime(),
                        request.getEndTime()
                );

        /*
         * Normal appointments cannot overlap.
         *
         * Emergency appointments are deliberately allowed
         * to override this rule.
         */
        if (appointmentConflict && !emergency) {

            throw new ResourceAlreadyExistsException(
                    "Doctor already has an appointment during this time"
            );
        }

        /*
         * 6. Emergency override must have a reason if it
         * actually violates normal availability/booking rules.
         */
        boolean overrideRequired =
                emergency
                        && (doctorUnavailable
                        || appointmentConflict);

        if (overrideRequired
                && (request.getOverrideReason() == null
                || request.getOverrideReason().isBlank())) {

            throw new IllegalArgumentException(
                    "Override reason is required for an emergency appointment"
            );
        }

        /*
         * 7. Calculate consultation fee.
         *
         * Backend decides fee based on:
         * - visit type
         * - doctor's clinic policy
         * - previous completed qualifying visits
         */
        BigDecimal consultationFee =
                calculateConsultationFee(
                        tenantId,
                        request
                );

        /*
         * 8. Map request to Appointment.
         */
        Appointment appointment =
                AppointmentMapper.toEntity(
                        request,
                        tenantId
                );

        appointment.setConsultationFee(
                consultationFee
        );

        appointment.setAppointmentType(
                request.getAppointmentType()
        );

        /*
         * 9. Persist intentional override information.
         */
        appointment.setAvailabilityOverride(
                overrideRequired
        );

        if (overrideRequired) {
            appointment.setOverrideReason(
                    request.getOverrideReason()
            );
        }

        /*
         * Booking does NOT automatically mean the consultation
         * has happened, so this stays false initially.
         */
        appointment.setStartsFollowUpWindow(false);

        appointment =
                repository.save(appointment);

        return AppointmentMapper.toResponse(
                appointment
        );
    }

    @Override
    public AppointmentResponse getAppointmentById(Long id) {

        Long tenantId = getCurrentTenantId();

        Appointment appointment = repository
                .findByIdAndTenantId(id, tenantId)
                .orElseThrow(() ->
                        new AppointmentNotFoundException(
                                "Appointment not found with id " + id));

        return AppointmentMapper.toResponse(appointment);
    }

    @Override
    public Page<AppointmentResponse> getAllAppointments(
            int page,
            int size,
            String sortBy,
            String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable =
                PageRequest.of(page, size, sort);

        Long tenantId = getCurrentTenantId();

        return repository
                .findAllByTenantId(tenantId, pageable)
                .map(AppointmentMapper::toResponse);
    }

    @Override
    public AppointmentResponse updateAppointment(
            Long id,
            UpdateAppointmentRequest request) {

        Long tenantId = getCurrentTenantId();

        Appointment appointment = repository
                .findByIdAndTenantId(id, tenantId)
                .orElseThrow(() ->
                        new AppointmentNotFoundException(
                                "Appointment not found with id " + id));

        Long doctorId =
                request.getDoctorId() != null
                        ? request.getDoctorId()
                        : appointment.getDoctorId();

        LocalDate appointmentDate =
                request.getAppointmentDate() != null
                        ? request.getAppointmentDate()
                        : appointment.getAppointmentDate();

        LocalTime startTime =
                request.getStartTime() != null
                        ? request.getStartTime()
                        : appointment.getStartTime();

        LocalTime endTime =
                request.getEndTime() != null
                        ? request.getEndTime()
                        : appointment.getEndTime();

        if (!startTime.isBefore(endTime)) {
            throw new IllegalArgumentException(
                    "Start time must be before end time"
            );
        }
        doctorClient.validateDoctorExists(doctorId);

        DoctorAvailabilityResponse availability =
                doctorClient.checkAvailability(
                        doctorId,
                        appointmentDate,
                        startTime,
                        endTime
                );
        if (availability == null || !availability.isAvailable()) {

            String reason = availability != null
                    ? availability.getReason()
                    : "UNKNOWN";

            throw new DoctorUnavailableException(
                    "Doctor is not available for the requested slot. Reason: "
                            + reason
            );
        }


        boolean conflict = repository.hasOverlappingAppointmentForUpdate(
                tenantId,
                id,
                request.getDoctorId(),
                request.getAppointmentDate(),
                request.getStartTime(),
                request.getEndTime()
        );

        if (conflict) {
            throw new ResourceAlreadyExistsException(
                    "Doctor already has an appointment during this time"
            );
        }

        AppointmentMapper.updateEntity(request, appointment);

        appointment = repository.save(appointment);

        return AppointmentMapper.toResponse(appointment);
    }

    @Override
    public void cancelAppointment(Long id) {

        Long tenantId = getCurrentTenantId();

        Appointment appointment = repository
                .findByIdAndTenantId(id, tenantId)
                .orElseThrow(() ->
                        new AppointmentNotFoundException(
                                "Appointment not found with id " + id));

        appointment.setStatus(
                com.whatsappcrm.appointment_service.enums.AppointmentStatus.CANCELLED
        );

        repository.save(appointment);
    }
    private BigDecimal calculateConsultationFee(
            Long tenantId,
            CreateAppointmentRequest request) {

        ConsultationPolicyResponse policy =
                doctorClient.getConsultationPolicy(
                        request.getDoctorId()
                );

        if (policy == null) {
            throw new IllegalStateException(
                    "Consultation policy is not configured"
            );
        }

        switch (request.getVisitType()) {

            case NEW_CONSULTATION:
                return policy.getConsultationFee();

            case REPORT_REVIEW:

                if (policy.isReportReviewFree()) {
                    return BigDecimal.ZERO;
                }

                return policy.getFollowUpFee() != null
                        ? policy.getFollowUpFee()
                        : policy.getConsultationFee();

            case FOLLOW_UP:
                return calculateFollowUpFee(
                        tenantId,
                        request,
                        policy
                );

            default:
                return policy.getConsultationFee();
        }
    }
    private BigDecimal calculateFollowUpFee(
            Long tenantId,
            CreateAppointmentRequest request,
            ConsultationPolicyResponse policy) {

        Optional<Appointment> previousPaidVisit =
                repository
                        .findFirstByTenantIdAndPatientIdAndDoctorIdAndStatusAndStartsFollowUpWindowTrueOrderByAppointmentDateDescActualEndAtDesc(
                                tenantId,
                                request.getPatientId(),
                                request.getDoctorId(),
                                AppointmentStatus.COMPLETED
                        );

        if (previousPaidVisit.isEmpty()) {

            // No previous paid consultation.
            // Treat this as chargeable.
            return policy.getFollowUpFee() != null
                    ? policy.getFollowUpFee()
                    : policy.getConsultationFee();
        }

        LocalDate previousVisitDate =
                previousPaidVisit.get()
                        .getAppointmentDate();

        LocalDate freeUntil =
                previousVisitDate.plusDays(
                        policy.getFreeFollowUpDays()
                );

        boolean insideFreeWindow =
                !request.getAppointmentDate()
                        .isAfter(freeUntil);

        if (insideFreeWindow) {
            return BigDecimal.ZERO;
        }

        return policy.getFollowUpFee() != null
                ? policy.getFollowUpFee()
                : policy.getConsultationFee();
    }

    @Override
    @Transactional
    public AppointmentResponse checkInAppointment(
            Long id) {

        Long tenantId = getCurrentTenantId();

        Appointment appointment =
                repository
                        .findByIdAndTenantId(
                                id,
                                tenantId
                        )
                        .orElseThrow(() ->
                                new AppointmentNotFoundException(
                                        "Appointment not found with id " + id
                                )
                        );

        if (appointment.getStatus()
                == AppointmentStatus.CANCELLED) {

            throw new IllegalStateException(
                    "Cancelled appointment cannot be checked in"
            );
        }

        if (appointment.getStatus()
                == AppointmentStatus.COMPLETED) {

            throw new IllegalStateException(
                    "Completed appointment cannot be checked in"
            );
        }

        appointment.setCheckedInAt(
                LocalDateTime.now()
        );

        appointment.setStatus(
                AppointmentStatus.WAITING
        );

        appointment =
                repository.save(appointment);

        return AppointmentMapper.toResponse(
                appointment
        );
    }

    @Override
    @Transactional
    public AppointmentResponse startAppointment(
            Long id) {

        Long tenantId = getCurrentTenantId();

        Appointment appointment =
                repository
                        .findByIdAndTenantId(
                                id,
                                tenantId
                        )
                        .orElseThrow(() ->
                                new AppointmentNotFoundException(
                                        "Appointment not found with id " + id
                                )
                        );

        if (appointment.getStatus()
                != AppointmentStatus.WAITING
                && appointment.getStatus()
                != AppointmentStatus.CHECKED_IN
                && appointment.getStatus()
                != AppointmentStatus.BOOKED
                && appointment.getStatus()
                != AppointmentStatus.CONFIRMED) {

            throw new IllegalStateException(
                    "Appointment cannot be started in status "
                            + appointment.getStatus()
            );
        }

        appointment.setActualStartAt(
                LocalDateTime.now()
        );

        appointment.setStatus(
                AppointmentStatus.IN_PROGRESS
        );

        appointment =
                repository.save(appointment);

        return AppointmentMapper.toResponse(
                appointment
        );
    }
    @Override
    @Transactional
    public AppointmentResponse completeAppointment(
            Long id) {

        Long tenantId = getCurrentTenantId();

        Appointment appointment =
                repository
                        .findByIdAndTenantId(
                                id,
                                tenantId
                        )
                        .orElseThrow(() ->
                                new AppointmentNotFoundException(
                                        "Appointment not found with id " + id
                                )
                        );

        if (appointment.getStatus()
                != AppointmentStatus.IN_PROGRESS) {

            throw new IllegalStateException(
                    "Only an appointment in progress can be completed"
            );
        }

        appointment.setActualEndAt(
                LocalDateTime.now()
        );

        appointment.setStatus(
                AppointmentStatus.COMPLETED
        );

        /*
         * Determine whether this completed visit should start
         * a future free-follow-up window.
         */
        boolean qualifyingPaidVisit =
                appointment.getConsultationFee() != null
                        && appointment
                        .getConsultationFee()
                        .compareTo(BigDecimal.ZERO) > 0;

        boolean qualifyingVisitType =
                appointment.getVisitType()
                        == VisitType.NEW_CONSULTATION;

        boolean emergencyTreatment =
                appointment.getAppointmentType()
                        == AppointmentType.EMERGENCY;

        appointment.setStartsFollowUpWindow(
                qualifyingPaidVisit
                        && (qualifyingVisitType
                        || emergencyTreatment)
        );

        appointment =
                repository.save(appointment);

        return AppointmentMapper.toResponse(
                appointment
        );
    }
}