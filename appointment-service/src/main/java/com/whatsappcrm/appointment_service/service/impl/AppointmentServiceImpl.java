package com.whatsappcrm.appointment_service.service.impl;

import com.whatsappcrm.appointment_service.dto.request.CreateAppointmentRequest;
import com.whatsappcrm.appointment_service.dto.request.UpdateAppointmentRequest;
import com.whatsappcrm.appointment_service.dto.response.AppointmentResponse;
import com.whatsappcrm.appointment_service.entity.Appointment;
import com.whatsappcrm.appointment_service.exception.AppointmentNotFoundException;
import com.whatsappcrm.appointment_service.exception.ResourceAlreadyExistsException;
import com.whatsappcrm.appointment_service.mapper.AppointmentMapper;
import com.whatsappcrm.appointment_service.repository.AppointmentRepository;
import com.whatsappcrm.appointment_service.service.interfaces.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl
        implements AppointmentService {

    private final AppointmentRepository repository;

    private Long getCurrentTenantId() {
        return 1L;
    }

    @Override
    public AppointmentResponse createAppointment(
            CreateAppointmentRequest request) {

        // Temporary tenant ID.
        // Later this will come from authenticated user context.
        Long tenantId = getCurrentTenantId();


        boolean conflict = repository.hasOverlappingAppointment(
                tenantId,
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
        Appointment appointment =
                AppointmentMapper.toEntity(request, tenantId);

        appointment = repository.save(appointment);

        return AppointmentMapper.toResponse(appointment);
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
}