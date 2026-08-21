package com.whatsappcrm.doctor_service.service.interfaces;

import com.whatsappcrm.doctor_service.dto.request.CreateClinicHolidayRequest;
import com.whatsappcrm.doctor_service.dto.request.CreateDoctorLeaveRequest;
import com.whatsappcrm.doctor_service.dto.request.CreateDoctorScheduleRequest;
import com.whatsappcrm.doctor_service.dto.response.ClinicHolidayResponse;
import com.whatsappcrm.doctor_service.dto.response.DoctorLeaveResponse;
import com.whatsappcrm.doctor_service.dto.response.DoctorScheduleResponse;

import java.util.List;

public interface ScheduleService {

    // Doctor schedule

    DoctorScheduleResponse createSchedule(
            CreateDoctorScheduleRequest request
    );

    List<DoctorScheduleResponse> getDoctorSchedules(
            Long doctorId
    );

    DoctorScheduleResponse updateSchedule(
            Long scheduleId,
            CreateDoctorScheduleRequest request
    );

    void deleteSchedule(Long scheduleId);


    // Doctor leave

    DoctorLeaveResponse createLeave(
            CreateDoctorLeaveRequest request
    );

    List<DoctorLeaveResponse> getDoctorLeaves(
            Long doctorId
    );

    DoctorLeaveResponse updateLeave(
            Long leaveId,
            CreateDoctorLeaveRequest request
    );

    void deleteLeave(Long leaveId);


    // Clinic holiday

    ClinicHolidayResponse createHoliday(
            CreateClinicHolidayRequest request
    );

    List<ClinicHolidayResponse> getHolidays();

    ClinicHolidayResponse updateHoliday(
            Long holidayId,
            CreateClinicHolidayRequest request
    );

    void deleteHoliday(Long holidayId);
}