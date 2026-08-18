package com.whatsappcrm.patient_service.service.impl;

import com.whatsappcrm.patient_service.dto.request.CreatePatientRequest;
import com.whatsappcrm.patient_service.dto.request.UpdatePatientRequest;
import com.whatsappcrm.patient_service.dto.response.PatientResponse;
import com.whatsappcrm.patient_service.entity.Patient;
import com.whatsappcrm.patient_service.exception.PatientNotFoundException;
import com.whatsappcrm.patient_service.exception.ResourceAlreadyExistsException;
import com.whatsappcrm.patient_service.mapper.PatientMapper;
import com.whatsappcrm.patient_service.repository.PatientRepository;
import com.whatsappcrm.patient_service.service.interfaces.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {
    private final PatientRepository repository;


    private Long getCurrentTenantId() {
        return 1L;
    }

    @Override
    public PatientResponse createPatient(CreatePatientRequest request) {

        Long tenantId = getCurrentTenantId();

        if (repository.existsByTenantIdAndMobileNumber(
                tenantId,
                request.getMobileNumber())) {

            throw new ResourceAlreadyExistsException(
                    "Patient already exists with mobile number "
                            + request.getMobileNumber()
            );
        }

        Patient patient = PatientMapper.toEntity(request);
        patient.setTenantId(tenantId);

        patient = repository.save(patient);

        return PatientMapper.toResponse(patient);
    }

    @Override
    public PatientResponse updatePatient(
            Long id,
            UpdatePatientRequest request) {

        Patient patient = findPatientById(id);

        if (request.getMobileNumber() != null
                && !request.getMobileNumber()
                .equals(patient.getMobileNumber())
                && repository.existsByTenantIdAndMobileNumber(
                getCurrentTenantId(),
                request.getMobileNumber())) {

            throw new ResourceAlreadyExistsException(
                    "Patient already exists with mobile number "
                            + request.getMobileNumber()
            );
        }

        PatientMapper.updateEntity(request, patient);

        patient = repository.save(patient);

        return PatientMapper.toResponse(patient);
    }

    @Override
    public PatientResponse getPatientById(Long id) {

        Patient patient = findPatientById(id);

        return PatientMapper.toResponse(patient);
    }
    private Patient findPatientById(Long id) {

        return repository
                .findByTenantIdAndId(getCurrentTenantId(), id)
                .orElseThrow(() ->
                        new PatientNotFoundException(
                                "Patient not found with id " + id));
    }

    @Override
    public Page<PatientResponse> getAllPatients(
            int page,
            int size,
            String sortBy,
            String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        return repository.findAllByTenantId(
                        getCurrentTenantId(),
                        pageable
                )
                .map(PatientMapper::toResponse);
    }

    @Override
    public void deletePatient(Long id) {

        Patient patient = findPatientById(id);

        patient.setDeleted(true);

        repository.save(patient);
    }

    @Override
    public PatientResponse getByMobile(String mobileNumber) {

        Patient patient = repository.findByTenantIdAndMobileNumber(
                        getCurrentTenantId(),
                        mobileNumber
                )
                .orElseThrow(() ->
                        new PatientNotFoundException("Patient not found"));

        return PatientMapper.toResponse(patient);
    }
    @Override
    public Page<PatientResponse> searchPatients(
            String keyword,
            int page,
            int size,
            String sortBy,
            String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Long tenantId = getCurrentTenantId();

        Page<Patient> patients =
                repository.searchPatients(
                        tenantId,
                        keyword,
                        pageable
                );

        return patients.map(PatientMapper::toResponse);
    }
}
