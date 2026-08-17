package com.whatsappcrm.appointment_service.mapper;

import com.whatsappcrm.appointment_service.dto.request.CreateAppointmentRequest;
import com.whatsappcrm.appointment_service.dto.request.UpdateAppointmentRequest;
import com.whatsappcrm.appointment_service.dto.response.AppointmentResponse;
import com.whatsappcrm.appointment_service.entity.Appointment;
import com.whatsappcrm.appointment_service.enums.AppointmentStatus;

public final class AppointmentMapper {

    private AppointmentMapper() {
    }

    public static Appointment toEntity(
            CreateAppointmentRequest request,
            Long tenantId) {

        return Appointment.builder()
                .tenantId(tenantId)
                .patientId(request.getPatientId())
                .doctorId(request.getDoctorId())
                .appointmentDate(request.getAppointmentDate())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .status(AppointmentStatus.BOOKED)
                .reason(request.getReason())
                .notes(request.getNotes())
                .build();
    }

    public static void updateEntity(
            UpdateAppointmentRequest request,
            Appointment appointment) {

        if (request.getDoctorId() != null) {
            appointment.setDoctorId(request.getDoctorId());
        }

        if (request.getAppointmentDate() != null) {
            appointment.setAppointmentDate(
                    request.getAppointmentDate());
        }

        if (request.getStartTime() != null) {
            appointment.setStartTime(
                    request.getStartTime());
        }

        if (request.getEndTime() != null) {
            appointment.setEndTime(
                    request.getEndTime());
        }

        if (request.getStatus() != null) {
            appointment.setStatus(
                    request.getStatus());
        }

        if (request.getReason() != null) {
            appointment.setReason(
                    request.getReason());
        }

        if (request.getNotes() != null) {
            appointment.setNotes(
                    request.getNotes());
        }
    }

    public static AppointmentResponse toResponse(
            Appointment appointment) {

        return AppointmentResponse.builder()
                .id(appointment.getId())
                .tenantId(appointment.getTenantId())
                .patientId(appointment.getPatientId())
                .doctorId(appointment.getDoctorId())
                .appointmentDate(appointment.getAppointmentDate())
                .startTime(appointment.getStartTime())
                .endTime(appointment.getEndTime())
                .status(appointment.getStatus())
                .reason(appointment.getReason())
                .notes(appointment.getNotes())
                .createdDate(appointment.getCreatedDate())
                .modifiedDate(appointment.getModifiedDate())
                .build();
    }
}