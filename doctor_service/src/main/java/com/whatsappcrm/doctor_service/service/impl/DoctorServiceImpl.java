package com.whatsappcrm.doctor_service.service.impl;

import com.whatsappcrm.doctor_service.dto.request.AddDoctorToClinicRequest;
import com.whatsappcrm.doctor_service.dto.request.ConsultationPolicyRequest;
import com.whatsappcrm.doctor_service.dto.request.CreateDoctorRequest;
import com.whatsappcrm.doctor_service.dto.request.UpdateDoctorRequest;
import com.whatsappcrm.doctor_service.dto.response.ConsultationPolicyResponse;
import com.whatsappcrm.doctor_service.dto.response.DoctorClinicResponse;
import com.whatsappcrm.doctor_service.dto.response.DoctorResponse;
import com.whatsappcrm.doctor_service.entity.ConsultationPolicy;
import com.whatsappcrm.doctor_service.entity.Doctor;
import com.whatsappcrm.doctor_service.entity.DoctorClinic;
import com.whatsappcrm.doctor_service.enums.DoctorClinicStatus;
import com.whatsappcrm.doctor_service.enums.DoctorStatus;
import com.whatsappcrm.doctor_service.exception.DoctorNotFoundException;
import com.whatsappcrm.doctor_service.exception.ResourceAlreadyExistsException;
import com.whatsappcrm.doctor_service.mapper.DoctorMapper;
import com.whatsappcrm.doctor_service.repository.ConsultationPolicyRepository;
import com.whatsappcrm.doctor_service.repository.DoctorClinicRepository;
import com.whatsappcrm.doctor_service.repository.DoctorRepository;
import com.whatsappcrm.doctor_service.service.interfaces.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;
    private final DoctorClinicRepository doctorClinicRepository;
    private final ConsultationPolicyRepository consultationPolicyRepository;

    private Long getCurrentTenantId() {
        return 1L;
    }

    @Override
    @Transactional
    public DoctorResponse createDoctor(
            CreateDoctorRequest request) {

        Doctor doctor = doctorRepository
                .findByRegistrationNumber(
                        request.getRegistrationNumber()
                )
                .orElseGet(() -> {

                    Doctor newDoctor =
                            DoctorMapper.toEntity(request);

                    newDoctor.setStatus(
                            DoctorStatus.ACTIVE
                    );

                    return doctorRepository.save(
                            newDoctor
                    );
                });

        return DoctorMapper.toResponse(doctor);
    }

    @Override
    public DoctorResponse getDoctorById(Long id) {

        Long tenantId = getCurrentTenantId();

        doctorClinicRepository
                .findByTenantIdAndDoctorId(
                        tenantId,
                        id
                )
                .orElseThrow(() ->
                        new DoctorNotFoundException(
                                "Doctor not found for this clinic"
                        )
                );

        Doctor doctor = findDoctorById(id);

        return DoctorMapper.toResponse(doctor);
    }

    @Override
    public Page<DoctorResponse> getAllDoctors(
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

        return doctorRepository
                .findAllDoctorsByTenantId(
                        tenantId,
                        pageable
                )
                .map(DoctorMapper::toResponse);
    }

    @Override
    @Transactional
    public DoctorResponse updateDoctor(
            Long id,
            UpdateDoctorRequest request) {

        Doctor doctor = findDoctorById(id);

        /*
         * Registration number identifies the doctor globally.
         * If it is being changed, make sure another doctor
         * doesn't already have it.
         */
        if (request.getRegistrationNumber() != null
                && !request.getRegistrationNumber()
                .equals(doctor.getRegistrationNumber())) {

            Optional<Doctor> existingDoctor =
                    doctorRepository.findByRegistrationNumber(
                            request.getRegistrationNumber()
                    );

            if (existingDoctor.isPresent()
                    && !existingDoctor.get().getId().equals(doctor.getId())) {

                throw new ResourceAlreadyExistsException(
                        "Another doctor already exists with registration number "
                                + request.getRegistrationNumber()
                );
            }
        }

        DoctorMapper.updateEntity(
                request,
                doctor
        );

        doctor = doctorRepository.save(doctor);

        return DoctorMapper.toResponse(doctor);
    }

    /*@Override
    @Transactional
    public void deleteDoctor(Long id) {

        Doctor doctor = findDoctorById(id);

        doctor.setDeleted(true);
        doctor.setStatus(DoctorStatus.INACTIVE);

        doctorRepository.save(doctor);
    }*/

    @Override
    @Transactional
    public DoctorClinicResponse addDoctorToClinic(
            AddDoctorToClinicRequest request) {

        Long tenantId = getCurrentTenantId();

        Doctor doctor =
                findDoctorById(
                        request.getDoctorId()
                );

        if (doctor.getStatus() != DoctorStatus.ACTIVE) {
            throw new IllegalStateException(
                    "Inactive doctor cannot be added to clinic"
            );
        }

        if (doctorClinicRepository
                .existsByTenantIdAndDoctorId(
                        tenantId,
                        doctor.getId()
                )) {

            throw new ResourceAlreadyExistsException(
                    "Doctor is already associated with this clinic"
            );
        }

        DoctorClinic association =
                new DoctorClinic();

        association.setTenantId(tenantId);
        association.setDoctorId(
                doctor.getId()
        );
        association.setStatus(
                DoctorClinicStatus.ACTIVE
        );

        if (request.getDefaultAppointmentDurationMinutes()
                != null) {

            association
                    .setDefaultAppointmentDurationMinutes(
                            request
                                    .getDefaultAppointmentDurationMinutes()
                    );
        }

        association =
                doctorClinicRepository.save(
                        association
                );

        return DoctorClinicResponse.builder()
                .id(association.getId())
                .tenantId(
                        association.getTenantId()
                )
                .doctorId(
                        association.getDoctorId()
                )
                .status(
                        association.getStatus()
                )
                .defaultAppointmentDurationMinutes(
                        association
                                .getDefaultAppointmentDurationMinutes()
                )
                .build();
    }

    @Override
    @Transactional
    public ConsultationPolicyResponse
    createOrUpdateConsultationPolicy(
            Long doctorId,
            ConsultationPolicyRequest request) {

        Long tenantId = getCurrentTenantId();

        findDoctorById(doctorId);

        DoctorClinic association =
                doctorClinicRepository
                        .findByTenantIdAndDoctorId(
                                tenantId,
                                doctorId
                        )
                        .orElseThrow(() ->
                                new DoctorNotFoundException(
                                        "Doctor is not associated with this clinic"
                                )
                        );

        if (association.getStatus()
                != DoctorClinicStatus.ACTIVE) {

            throw new IllegalStateException(
                    "Doctor is not active for this clinic"
            );
        }

        ConsultationPolicy policy =
                consultationPolicyRepository
                        .findByTenantIdAndDoctorId(
                                tenantId,
                                doctorId
                        )
                        .orElseGet(
                                ConsultationPolicy::new
                        );

        policy.setTenantId(tenantId);
        policy.setDoctorId(doctorId);

        policy.setConsultationFee(
                request.getConsultationFee()
        );

        policy.setFreeFollowUpDays(
                request.getFreeFollowUpDays() != null
                        ? request.getFreeFollowUpDays()
                        : 0
        );

        policy.setReportReviewFree(
                request.isReportReviewFree()
        );

        policy.setFollowUpFee(
                request.getFollowUpFee()
        );

        policy =
                consultationPolicyRepository
                        .save(policy);

        return ConsultationPolicyResponse.builder()
                .id(policy.getId())
                .tenantId(policy.getTenantId())
                .doctorId(policy.getDoctorId())
                .consultationFee(
                        policy.getConsultationFee()
                )
                .freeFollowUpDays(
                        policy.getFreeFollowUpDays()
                )
                .reportReviewFree(
                        policy.isReportReviewFree()
                )
                .followUpFee(
                        policy.getFollowUpFee()
                )
                .build();
    }

    private Doctor findDoctorById(Long id) {

        return doctorRepository
                .findById(id)
                .orElseThrow(() ->
                        new DoctorNotFoundException(
                                "Doctor not found with id " + id
                        )
                );
    }
    @Override
    @Transactional
    public void removeDoctorFromClinic(Long doctorId) {

        Long tenantId = getCurrentTenantId();

        DoctorClinic association =
                doctorClinicRepository
                        .findByTenantIdAndDoctorId(
                                tenantId,
                                doctorId
                        )
                        .orElseThrow(() ->
                                new DoctorNotFoundException(
                                        "Doctor is not associated with this clinic"
                                )
                        );

        association.setStatus(
                DoctorClinicStatus.INACTIVE
        );

        association.setDeleted(true);

        doctorClinicRepository.save(association);
    }
}