package com.whatsappcrm.doctor_service.service.impl;

import com.whatsappcrm.doctor_service.dto.request.CreateClinicHolidayRequest;
import com.whatsappcrm.doctor_service.dto.request.CreateDoctorLeaveRequest;
import com.whatsappcrm.doctor_service.dto.request.CreateDoctorScheduleRequest;
import com.whatsappcrm.doctor_service.dto.response.ClinicHolidayResponse;
import com.whatsappcrm.doctor_service.dto.response.DoctorAvailabilityResponse;
import com.whatsappcrm.doctor_service.dto.response.DoctorLeaveResponse;
import com.whatsappcrm.doctor_service.dto.response.DoctorScheduleResponse;
import com.whatsappcrm.doctor_service.entity.ClinicHoliday;
import com.whatsappcrm.doctor_service.entity.DoctorLeave;
import com.whatsappcrm.doctor_service.entity.DoctorSchedule;
import com.whatsappcrm.doctor_service.exception.DoctorNotFoundException;
import com.whatsappcrm.doctor_service.exception.ResourceAlreadyExistsException;
import com.whatsappcrm.doctor_service.repository.ClinicHolidayRepository;
import com.whatsappcrm.doctor_service.repository.DoctorClinicRepository;
import com.whatsappcrm.doctor_service.repository.DoctorLeaveRepository;
import com.whatsappcrm.doctor_service.repository.DoctorScheduleRepository;
import com.whatsappcrm.doctor_service.service.interfaces.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {

    private final DoctorScheduleRepository scheduleRepository;
    private final DoctorLeaveRepository leaveRepository;
    private final ClinicHolidayRepository holidayRepository;
    private final DoctorClinicRepository doctorClinicRepository;

    private Long getCurrentTenantId() {
        return 1L;
    }

    /*
     * ==========================================================
     * DOCTOR SCHEDULE
     * ==========================================================
     */

    @Override
    @Transactional
    public DoctorScheduleResponse createSchedule(
            CreateDoctorScheduleRequest request) {

        Long tenantId = getCurrentTenantId();

        validateDoctorBelongsToClinic(
                tenantId,
                request.getDoctorId()
        );

        validateTimeRange(
                request.getStartTime(),
                request.getEndTime()
        );
        boolean overlap =
                scheduleRepository.hasOverlappingSchedule(
                        tenantId,
                        request.getDoctorId(),
                        request.getDayOfWeek(),
                        request.getStartTime(),
                        request.getEndTime()
                );

        if (overlap) {
            throw new ResourceAlreadyExistsException(
                    "Doctor already has an overlapping schedule"
            );
        }

        DoctorSchedule schedule =
                new DoctorSchedule();

        schedule.setTenantId(tenantId);
        schedule.setDoctorId(request.getDoctorId());
        schedule.setDayOfWeek(request.getDayOfWeek());
        schedule.setStartTime(request.getStartTime());
        schedule.setEndTime(request.getEndTime());
        schedule.setActive(true);

        schedule = scheduleRepository.save(schedule);

        return toScheduleResponse(schedule);
    }

    @Override
    public List<DoctorScheduleResponse> getDoctorSchedules(
            Long doctorId) {

        Long tenantId = getCurrentTenantId();

        validateDoctorBelongsToClinic(
                tenantId,
                doctorId
        );

        return scheduleRepository
                .findByTenantIdAndDoctorIdAndActiveTrue(
                        tenantId,
                        doctorId
                )
                .stream()
                .map(this::toScheduleResponse)
                .toList();
    }

    @Override
    @Transactional
    public DoctorScheduleResponse updateSchedule(
            Long scheduleId,
            CreateDoctorScheduleRequest request) {

        Long tenantId = getCurrentTenantId();

        DoctorSchedule schedule =
                scheduleRepository
                        .findByIdAndTenantId(
                                scheduleId,
                                tenantId
                        )
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Schedule not found"
                                )
                        );

        /*
         * Don't allow someone to move a schedule
         * to a doctor outside this clinic.
         */
        validateDoctorBelongsToClinic(
                tenantId,
                request.getDoctorId()
        );

        validateTimeRange(
                request.getStartTime(),
                request.getEndTime()
        );

        schedule.setDoctorId(request.getDoctorId());
        schedule.setDayOfWeek(request.getDayOfWeek());
        schedule.setStartTime(request.getStartTime());
        schedule.setEndTime(request.getEndTime());

        schedule = scheduleRepository.save(schedule);

        return toScheduleResponse(schedule);
    }

    @Override
    @Transactional
    public void deleteSchedule(Long scheduleId) {

        Long tenantId = getCurrentTenantId();

        DoctorSchedule schedule =
                scheduleRepository
                        .findByIdAndTenantId(
                                scheduleId,
                                tenantId
                        )
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Schedule not found"
                                )
                        );

        schedule.setDeleted(true);
        schedule.setActive(false);

        scheduleRepository.save(schedule);
    }

    /*
     * ==========================================================
     * DOCTOR LEAVE
     * ==========================================================
     */

    @Override
    @Transactional
    public DoctorLeaveResponse createLeave(
            CreateDoctorLeaveRequest request) {

        Long tenantId = getCurrentTenantId();

        validateDoctorBelongsToClinic(
                tenantId,
                request.getDoctorId()
        );

        validateLeaveTimes(request);

        DoctorLeave leave =
                new DoctorLeave();

        leave.setTenantId(tenantId);
        leave.setDoctorId(request.getDoctorId());
        leave.setLeaveDate(request.getLeaveDate());
        leave.setStartTime(request.getStartTime());
        leave.setEndTime(request.getEndTime());
        leave.setReason(request.getReason());

        leave = leaveRepository.save(leave);

        return toLeaveResponse(leave);
    }

    @Override
    public List<DoctorLeaveResponse> getDoctorLeaves(
            Long doctorId) {

        Long tenantId = getCurrentTenantId();

        validateDoctorBelongsToClinic(
                tenantId,
                doctorId
        );

        return leaveRepository
                .findByTenantIdAndDoctorId(
                        tenantId,
                        doctorId
                )
                .stream()
                .map(this::toLeaveResponse)
                .toList();
    }

    @Override
    @Transactional
    public DoctorLeaveResponse updateLeave(
            Long leaveId,
            CreateDoctorLeaveRequest request) {

        Long tenantId = getCurrentTenantId();

        DoctorLeave leave =
                leaveRepository
                        .findByIdAndTenantId(
                                leaveId,
                                tenantId
                        )
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Leave not found"
                                )
                        );

        validateDoctorBelongsToClinic(
                tenantId,
                request.getDoctorId()
        );

        validateLeaveTimes(request);

        leave.setDoctorId(request.getDoctorId());
        leave.setLeaveDate(request.getLeaveDate());
        leave.setStartTime(request.getStartTime());
        leave.setEndTime(request.getEndTime());
        leave.setReason(request.getReason());

        leave = leaveRepository.save(leave);

        return toLeaveResponse(leave);
    }

    @Override
    @Transactional
    public void deleteLeave(Long leaveId) {

        Long tenantId = getCurrentTenantId();

        DoctorLeave leave =
                leaveRepository
                        .findByIdAndTenantId(
                                leaveId,
                                tenantId
                        )
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Leave not found"
                                )
                        );

        leave.setDeleted(true);

        leaveRepository.save(leave);
    }

    /*
     * ==========================================================
     * CLINIC HOLIDAY
     * ==========================================================
     */

    @Override
    @Transactional
    public ClinicHolidayResponse createHoliday(
            CreateClinicHolidayRequest request) {

        Long tenantId = getCurrentTenantId();

        if (holidayRepository
                .existsByTenantIdAndHolidayDate(
                        tenantId,
                        request.getHolidayDate()
                )) {

            throw new ResourceAlreadyExistsException(
                    "Holiday already exists for "
                            + request.getHolidayDate()
            );
        }

        ClinicHoliday holiday =
                new ClinicHoliday();

        holiday.setTenantId(tenantId);
        holiday.setHolidayDate(
                request.getHolidayDate()
        );
        holiday.setDescription(
                request.getDescription()
        );

        holiday = holidayRepository.save(holiday);

        return toHolidayResponse(holiday);
    }

    @Override
    public List<ClinicHolidayResponse> getHolidays() {

        Long tenantId = getCurrentTenantId();

        return holidayRepository
                .findByTenantId(tenantId)
                .stream()
                .map(this::toHolidayResponse)
                .toList();
    }

    @Override
    @Transactional
    public ClinicHolidayResponse updateHoliday(
            Long holidayId,
            CreateClinicHolidayRequest request) {

        Long tenantId = getCurrentTenantId();

        ClinicHoliday holiday =
                holidayRepository
                        .findByIdAndTenantId(
                                holidayId,
                                tenantId
                        )
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Holiday not found"
                                )
                        );

        holiday.setHolidayDate(
                request.getHolidayDate()
        );

        holiday.setDescription(
                request.getDescription()
        );

        holiday = holidayRepository.save(holiday);

        return toHolidayResponse(holiday);
    }

    @Override
    @Transactional
    public void deleteHoliday(Long holidayId) {

        Long tenantId = getCurrentTenantId();

        ClinicHoliday holiday =
                holidayRepository
                        .findByIdAndTenantId(
                                holidayId,
                                tenantId
                        )
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Holiday not found"
                                )
                        );

        holiday.setDeleted(true);

        holidayRepository.save(holiday);
    }

    /*
     * ==========================================================
     * VALIDATION
     * ==========================================================
     */

    private void validateDoctorBelongsToClinic(
            Long tenantId,
            Long doctorId) {

        doctorClinicRepository
                .findByTenantIdAndDoctorId(
                        tenantId,
                        doctorId
                )
                .orElseThrow(() ->
                        new DoctorNotFoundException(
                                "Doctor not found for this clinic"
                        )
                );
    }

    private void validateTimeRange(
            java.time.LocalTime start,
            java.time.LocalTime end) {

        if (!start.isBefore(end)) {
            throw new IllegalArgumentException(
                    "Start time must be before end time"
            );
        }
    }

    private void validateLeaveTimes(
            CreateDoctorLeaveRequest request) {

        /*
         * Both null = full day leave.
         */
        if (request.getStartTime() == null
                && request.getEndTime() == null) {
            return;
        }

        /*
         * Only one supplied is invalid.
         */
        if (request.getStartTime() == null
                || request.getEndTime() == null) {

            throw new IllegalArgumentException(
                    "Both startTime and endTime must be provided for partial-day leave"
            );
        }

        validateTimeRange(
                request.getStartTime(),
                request.getEndTime()
        );
    }

    /*
     * ==========================================================
     * MAPPERS
     * ==========================================================
     */

    private DoctorScheduleResponse toScheduleResponse(
            DoctorSchedule schedule) {

        return DoctorScheduleResponse.builder()
                .id(schedule.getId())
                .tenantId(schedule.getTenantId())
                .doctorId(schedule.getDoctorId())
                .dayOfWeek(schedule.getDayOfWeek())
                .startTime(schedule.getStartTime())
                .endTime(schedule.getEndTime())
                .active(schedule.isActive())
                .build();
    }

    private DoctorLeaveResponse toLeaveResponse(
            DoctorLeave leave) {

        return DoctorLeaveResponse.builder()
                .id(leave.getId())
                .tenantId(leave.getTenantId())
                .doctorId(leave.getDoctorId())
                .leaveDate(leave.getLeaveDate())
                .startTime(leave.getStartTime())
                .endTime(leave.getEndTime())
                .reason(leave.getReason())
                .build();
    }

    private ClinicHolidayResponse toHolidayResponse(
            ClinicHoliday holiday) {

        return ClinicHolidayResponse.builder()
                .id(holiday.getId())
                .tenantId(holiday.getTenantId())
                .holidayDate(holiday.getHolidayDate())
                .description(holiday.getDescription())
                .build();
    }

    @Override
    public DoctorAvailabilityResponse checkAvailability(
            Long doctorId,
            LocalDate date,
            LocalTime startTime,
            LocalTime endTime) {

        Long tenantId = getCurrentTenantId();

        validateDoctorBelongsToClinic(
                tenantId,
                doctorId
        );

        validateTimeRange(startTime, endTime);

        // 1. Clinic holiday
        if (holidayRepository.existsByTenantIdAndHolidayDate(
                tenantId,
                date)) {

            return DoctorAvailabilityResponse.builder()
                    .doctorId(doctorId)
                    .available(false)
                    .reason("CLINIC_HOLIDAY")
                    .build();
        }

        // 2. Doctor must have a schedule covering the requested period
        List<DoctorSchedule> schedules =
                scheduleRepository
                        .findByTenantIdAndDoctorIdAndDayOfWeekAndActiveTrue(
                                tenantId,
                                doctorId,
                                date.getDayOfWeek()
                        );

        boolean insideWorkingHours =
                schedules.stream()
                        .anyMatch(schedule ->
                                !startTime.isBefore(schedule.getStartTime())
                                        && !endTime.isAfter(schedule.getEndTime())
                        );

        if (!insideWorkingHours) {

            return DoctorAvailabilityResponse.builder()
                    .doctorId(doctorId)
                    .available(false)
                    .reason("OUTSIDE_WORKING_HOURS")
                    .build();
        }

        // 3. Doctor leave
        List<DoctorLeave> leaves =
                leaveRepository
                        .findByTenantIdAndDoctorIdAndLeaveDate(
                                tenantId,
                                doctorId,
                                date
                        );

        for (DoctorLeave leave : leaves) {

            // Full-day leave
            if (leave.getStartTime() == null
                    && leave.getEndTime() == null) {

                return DoctorAvailabilityResponse.builder()
                        .doctorId(doctorId)
                        .available(false)
                        .reason("DOCTOR_ON_LEAVE")
                        .build();
            }

            // Partial-day overlap
            if (startTime.isBefore(leave.getEndTime())
                    && endTime.isAfter(leave.getStartTime())) {

                return DoctorAvailabilityResponse.builder()
                        .doctorId(doctorId)
                        .available(false)
                        .reason("DOCTOR_ON_LEAVE")
                        .build();
            }
        }

        return DoctorAvailabilityResponse.builder()
                .doctorId(doctorId)
                .available(true)
                .reason("AVAILABLE")
                .build();
    }
}