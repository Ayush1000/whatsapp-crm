package com.whatsappcrm.doctor_service.service.interfaces;

import com.whatsappcrm.doctor_service.dto.request.AddDoctorToClinicRequest;
import com.whatsappcrm.doctor_service.dto.request.ConsultationPolicyRequest;
import com.whatsappcrm.doctor_service.dto.request.CreateDoctorRequest;
import com.whatsappcrm.doctor_service.dto.request.UpdateDoctorRequest;
import com.whatsappcrm.doctor_service.dto.response.ConsultationPolicyResponse;
import com.whatsappcrm.doctor_service.dto.response.DoctorClinicResponse;
import com.whatsappcrm.doctor_service.dto.response.DoctorResponse;
import org.springframework.data.domain.Page;

public interface DoctorService {

    DoctorResponse createDoctor(CreateDoctorRequest request);

    DoctorResponse getDoctorById(Long id);

    Page<DoctorResponse> getAllDoctors(
            int page,
            int size,
            String sortBy,
            String sortDir
    );

    DoctorResponse updateDoctor(
            Long id,
            UpdateDoctorRequest request
    );

    //void deleteDoctor(Long id);

    DoctorClinicResponse addDoctorToClinic(
            AddDoctorToClinicRequest request
    );
    void removeDoctorFromClinic(
            Long doctorId
    );

    ConsultationPolicyResponse createOrUpdateConsultationPolicy(
            Long doctorId,
            ConsultationPolicyRequest request
    );
}