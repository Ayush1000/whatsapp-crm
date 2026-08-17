package com.whatsappcrm.appointment_service.service.interfaces;

import com.whatsappcrm.appointment_service.dto.request.CreateAppointmentRequest;
import com.whatsappcrm.appointment_service.dto.request.UpdateAppointmentRequest;
import com.whatsappcrm.appointment_service.dto.response.AppointmentResponse;
import org.springframework.data.domain.Page;

public interface AppointmentService {

    AppointmentResponse createAppointment(
            CreateAppointmentRequest request);

    AppointmentResponse getAppointmentById(Long id);

    Page<AppointmentResponse> getAllAppointments(
            int page,
            int size,
            String sortBy,
            String sortDir);

    AppointmentResponse updateAppointment(
            Long id,
            UpdateAppointmentRequest request);

    void cancelAppointment(Long id);
}