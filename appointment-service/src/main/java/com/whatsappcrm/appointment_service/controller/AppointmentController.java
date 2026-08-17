package com.whatsappcrm.appointment_service.controller;

import com.whatsappcrm.appointment_service.dto.request.CreateAppointmentRequest;
import com.whatsappcrm.appointment_service.dto.request.UpdateAppointmentRequest;
import com.whatsappcrm.appointment_service.dto.response.AppointmentResponse;
import com.whatsappcrm.appointment_service.service.interfaces.AppointmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;

    @PostMapping
    public ResponseEntity<AppointmentResponse> createAppointment(
            @Valid @RequestBody CreateAppointmentRequest request) {

        return ResponseEntity.ok(
                appointmentService.createAppointment(request)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppointmentResponse> getAppointmentById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                appointmentService.getAppointmentById(id)
        );
    }

    @GetMapping
    public ResponseEntity<Page<AppointmentResponse>> getAllAppointments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "appointmentDate")
                    String sortBy,
            @RequestParam(defaultValue = "desc")
                    String sortDir) {

        return ResponseEntity.ok(
                appointmentService.getAllAppointments(
                        page,
                        size,
                        sortBy,
                        sortDir
                )
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<AppointmentResponse> updateAppointment(
            @PathVariable Long id,
            @Valid @RequestBody UpdateAppointmentRequest request) {

        return ResponseEntity.ok(
                appointmentService.updateAppointment(id, request)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelAppointment(
            @PathVariable Long id) {

        appointmentService.cancelAppointment(id);

        return ResponseEntity.noContent().build();
    }
}