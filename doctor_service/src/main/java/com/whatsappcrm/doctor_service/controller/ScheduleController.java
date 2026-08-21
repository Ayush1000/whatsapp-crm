package com.whatsappcrm.doctor_service.controller;

import com.whatsappcrm.doctor_service.dto.request.CreateClinicHolidayRequest;
import com.whatsappcrm.doctor_service.dto.request.CreateDoctorLeaveRequest;
import com.whatsappcrm.doctor_service.dto.request.CreateDoctorScheduleRequest;
import com.whatsappcrm.doctor_service.dto.response.ClinicHolidayResponse;
import com.whatsappcrm.doctor_service.dto.response.DoctorAvailabilityResponse;
import com.whatsappcrm.doctor_service.dto.response.DoctorLeaveResponse;
import com.whatsappcrm.doctor_service.dto.response.DoctorScheduleResponse;
import com.whatsappcrm.doctor_service.service.interfaces.ScheduleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/api/schedules")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    /*
     * ==========================================================
     * DOCTOR SCHEDULE
     * ==========================================================
     */

    @PostMapping("/doctor")
    public ResponseEntity<DoctorScheduleResponse> createSchedule(
            @Valid
            @RequestBody
                    CreateDoctorScheduleRequest request) {

        return ResponseEntity.ok(
                scheduleService.createSchedule(request)
        );
    }

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<DoctorScheduleResponse>>
    getDoctorSchedules(
            @PathVariable Long doctorId) {

        return ResponseEntity.ok(
                scheduleService.getDoctorSchedules(
                        doctorId
                )
        );
    }

    @PutMapping("/doctor/{scheduleId}")
    public ResponseEntity<DoctorScheduleResponse> updateSchedule(
            @PathVariable Long scheduleId,
            @Valid
            @RequestBody
                    CreateDoctorScheduleRequest request) {

        return ResponseEntity.ok(
                scheduleService.updateSchedule(
                        scheduleId,
                        request
                )
        );
    }

    @DeleteMapping("/doctor/{scheduleId}")
    public ResponseEntity<Void> deleteSchedule(
            @PathVariable Long scheduleId) {

        scheduleService.deleteSchedule(scheduleId);

        return ResponseEntity.noContent().build();
    }


    /*
     * ==========================================================
     * DOCTOR LEAVE
     * ==========================================================
     */

    @PostMapping("/leave")
    public ResponseEntity<DoctorLeaveResponse> createLeave(
            @Valid
            @RequestBody
                    CreateDoctorLeaveRequest request) {

        return ResponseEntity.ok(
                scheduleService.createLeave(request)
        );
    }

    @GetMapping("/leave/doctor/{doctorId}")
    public ResponseEntity<List<DoctorLeaveResponse>>
    getDoctorLeaves(
            @PathVariable Long doctorId) {

        return ResponseEntity.ok(
                scheduleService.getDoctorLeaves(
                        doctorId
                )
        );
    }

    @PutMapping("/leave/{leaveId}")
    public ResponseEntity<DoctorLeaveResponse> updateLeave(
            @PathVariable Long leaveId,
            @Valid
            @RequestBody
                    CreateDoctorLeaveRequest request) {

        return ResponseEntity.ok(
                scheduleService.updateLeave(
                        leaveId,
                        request
                )
        );
    }

    @DeleteMapping("/leave/{leaveId}")
    public ResponseEntity<Void> deleteLeave(
            @PathVariable Long leaveId) {

        scheduleService.deleteLeave(leaveId);

        return ResponseEntity.noContent().build();
    }


    /*
     * ==========================================================
     * CLINIC HOLIDAY
     * ==========================================================
     */

    @PostMapping("/holiday")
    public ResponseEntity<ClinicHolidayResponse> createHoliday(
            @Valid
            @RequestBody
                    CreateClinicHolidayRequest request) {

        return ResponseEntity.ok(
                scheduleService.createHoliday(request)
        );
    }

    @GetMapping("/holiday")
    public ResponseEntity<List<ClinicHolidayResponse>>
    getHolidays() {

        return ResponseEntity.ok(
                scheduleService.getHolidays()
        );
    }

    @PutMapping("/holiday/{holidayId}")
    public ResponseEntity<ClinicHolidayResponse> updateHoliday(
            @PathVariable Long holidayId,
            @Valid
            @RequestBody
                    CreateClinicHolidayRequest request) {

        return ResponseEntity.ok(
                scheduleService.updateHoliday(
                        holidayId,
                        request
                )
        );
    }

    @DeleteMapping("/holiday/{holidayId}")
    public ResponseEntity<Void> deleteHoliday(
            @PathVariable Long holidayId) {

        scheduleService.deleteHoliday(holidayId);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/availability/doctor/{doctorId}")
    public ResponseEntity<DoctorAvailabilityResponse> checkAvailability(
            @PathVariable Long doctorId,
            @RequestParam LocalDate date,
            @RequestParam LocalTime startTime,
            @RequestParam LocalTime endTime) {

        return ResponseEntity.ok(
                scheduleService.checkAvailability(
                        doctorId,
                        date,
                        startTime,
                        endTime
                )
        );
    }
}