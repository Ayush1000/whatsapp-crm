package com.whatsappcrm.patient_service.controller;

import com.whatsappcrm.patient_service.dto.request.CreatePatientRequest;
import com.whatsappcrm.patient_service.dto.request.UpdatePatientRequest;
import com.whatsappcrm.patient_service.dto.response.PatientResponse;
import com.whatsappcrm.patient_service.service.interfaces.PatientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;

    @PostMapping
    public ResponseEntity<PatientResponse> createPatient(
            @Valid @RequestBody CreatePatientRequest request) {

        return ResponseEntity.ok(
                patientService.createPatient(request)
        );
    }
    @GetMapping("/search")
    public ResponseEntity<Page<PatientResponse>> searchPatients(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdDate") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {

        return ResponseEntity.ok(
                patientService.searchPatients(
                        keyword,
                        page,
                        size,
                        sortBy,
                        sortDir
                )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<PatientResponse> getPatientById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                patientService.getPatientById(id)
        );
    }

    @GetMapping
    public ResponseEntity<Page<PatientResponse>> getAllPatients(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdDate") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {

        return ResponseEntity.ok(
                patientService.getAllPatients(
                        page,
                        size,
                        sortBy,
                        sortDir
                )
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<PatientResponse> updatePatient(
            @PathVariable Long id,
            @Valid @RequestBody UpdatePatientRequest request) {

        return ResponseEntity.ok(
                patientService.updatePatient(id, request)
        );
    }

        @DeleteMapping("/{id}")
        public ResponseEntity<Void> deletePatient(
                @PathVariable Long id) {

            patientService.deletePatient(id);

            return ResponseEntity.noContent().build();
        }

        @GetMapping("/mobile/{mobileNumber}")
        public ResponseEntity<PatientResponse> getByMobile(
                @PathVariable String mobileNumber) {

            return ResponseEntity.ok(
                    patientService.getByMobile(mobileNumber)
            );
        }


}
