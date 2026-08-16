package com.whatsappcrm.patient_service.service.interfaces;

import com.whatsappcrm.patient_service.dto.request.CreatePatientRequest;
import com.whatsappcrm.patient_service.dto.request.UpdatePatientRequest;
import com.whatsappcrm.patient_service.dto.response.PatientResponse;
import org.springframework.data.domain.Page;


import java.util.List;

public interface PatientService {
    PatientResponse createPatient(CreatePatientRequest request);

    PatientResponse updatePatient(Long id,
                                  UpdatePatientRequest request);

    PatientResponse getPatientById(Long id);

    Page<PatientResponse> getAllPatients(
            int page,
            int size,
            String sortBy,
            String sortDir
    );

    PatientResponse getByMobile(String mobile);

    void deletePatient(Long id);
    Page<PatientResponse> searchPatients(
            String keyword,
            int page,
            int size,
            String sortBy,
            String sortDir
    );
}
