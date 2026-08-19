package com.whatsappcrm.doctor_service.controller;

import com.whatsappcrm.doctor_service.dto.request.AddDoctorToClinicRequest;
import com.whatsappcrm.doctor_service.dto.request.ConsultationPolicyRequest;
import com.whatsappcrm.doctor_service.dto.request.CreateDoctorRequest;
import com.whatsappcrm.doctor_service.dto.request.UpdateDoctorRequest;
import com.whatsappcrm.doctor_service.dto.response.ConsultationPolicyResponse;
import com.whatsappcrm.doctor_service.dto.response.DoctorClinicResponse;
import com.whatsappcrm.doctor_service.dto.response.DoctorResponse;
import com.whatsappcrm.doctor_service.service.interfaces.DoctorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/doctors")
@RequiredArgsConstructor
public class DoctorController {

    private final DoctorService doctorService;

    // Create doctor globally if not already present
    @PostMapping
    public ResponseEntity<DoctorResponse> createDoctor(
            @Valid @RequestBody CreateDoctorRequest request) {

        return ResponseEntity.ok(
                doctorService.createDoctor(request)
        );
    }

    // Get doctor only if associated with current clinic
    @GetMapping("/{id}")
    public ResponseEntity<DoctorResponse> getDoctorById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                doctorService.getDoctorById(id)
        );
    }

    // Get doctors associated with current clinic
    @GetMapping
    public ResponseEntity<Page<DoctorResponse>> getAllDoctors(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "firstName") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        return ResponseEntity.ok(
                doctorService.getAllDoctors(
                        page,
                        size,
                        sortBy,
                        sortDir
                )
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<DoctorResponse> updateDoctor(
            @PathVariable Long id,
            @Valid @RequestBody UpdateDoctorRequest request) {

        return ResponseEntity.ok(
                doctorService.updateDoctor(id, request)
        );
    }

    // Associate existing doctor with current clinic
    @PostMapping("/clinic")
    public ResponseEntity<DoctorClinicResponse> addDoctorToClinic(
            @Valid @RequestBody AddDoctorToClinicRequest request) {

        return ResponseEntity.ok(
                doctorService.addDoctorToClinic(request)
        );
    }

    // Remove doctor ONLY from current clinic
    @DeleteMapping("/{doctorId}/clinic")
    public ResponseEntity<Void> removeDoctorFromClinic(
            @PathVariable Long doctorId) {

        doctorService.removeDoctorFromClinic(doctorId);

        return ResponseEntity.noContent().build();
    }

    // Create/update pricing rules for this doctor at current clinic
    @PutMapping("/{doctorId}/consultation-policy")
    public ResponseEntity<ConsultationPolicyResponse>
    updateConsultationPolicy(
            @PathVariable Long doctorId,
            @Valid @RequestBody ConsultationPolicyRequest request) {

        return ResponseEntity.ok(
                doctorService.createOrUpdateConsultationPolicy(
                        doctorId,
                        request
                )
        );
    }
}