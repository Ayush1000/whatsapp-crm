package com.whatsappcrm.doctor_service.mapper;

import com.whatsappcrm.doctor_service.dto.request.CreateDoctorRequest;
import com.whatsappcrm.doctor_service.dto.request.UpdateDoctorRequest;
import com.whatsappcrm.doctor_service.dto.response.DoctorResponse;
import com.whatsappcrm.doctor_service.entity.Doctor;
import com.whatsappcrm.doctor_service.enums.DoctorStatus;

public final class DoctorMapper {

    private DoctorMapper() {
    }

    public static Doctor toEntity(
            CreateDoctorRequest request
            ) {

        Doctor doctor = new Doctor();


        doctor.setFirstName(request.getFirstName());
        doctor.setLastName(request.getLastName());
        doctor.setMobileNumber(request.getMobileNumber());
        doctor.setEmail(request.getEmail());
        doctor.setSpecialization(request.getSpecialization());
        doctor.setRegistrationNumber(
                request.getRegistrationNumber()
        );
        doctor.setQualification(request.getQualification());
        doctor.setExperienceYears(
                request.getExperienceYears()
        );
        doctor.setStatus(DoctorStatus.ACTIVE);

        return doctor;
    }

    public static void updateEntity(
            UpdateDoctorRequest request,
            Doctor doctor) {

        if (request.getFirstName() != null) {
            doctor.setFirstName(request.getFirstName());
        }

        if (request.getLastName() != null) {
            doctor.setLastName(request.getLastName());
        }

        if (request.getMobileNumber() != null) {
            doctor.setMobileNumber(request.getMobileNumber());
        }

        if (request.getEmail() != null) {
            doctor.setEmail(request.getEmail());
        }

        if (request.getSpecialization() != null) {
            doctor.setSpecialization(
                    request.getSpecialization()
            );
        }

        if (request.getRegistrationNumber() != null) {
            doctor.setRegistrationNumber(
                    request.getRegistrationNumber()
            );
        }

        if (request.getQualification() != null) {
            doctor.setQualification(
                    request.getQualification()
            );
        }

        if (request.getExperienceYears() != null) {
            doctor.setExperienceYears(
                    request.getExperienceYears()
            );
        }

        if (request.getStatus() != null) {
            doctor.setStatus(request.getStatus());
        }
    }

    public static DoctorResponse toResponse(Doctor doctor) {

        return DoctorResponse.builder()
                .id(doctor.getId())
                .firstName(doctor.getFirstName())
                .lastName(doctor.getLastName())
                .mobileNumber(doctor.getMobileNumber())
                .email(doctor.getEmail())
                .specialization(doctor.getSpecialization())
                .registrationNumber(
                        doctor.getRegistrationNumber()
                )
                .qualification(doctor.getQualification())
                .experienceYears(doctor.getExperienceYears())
                .status(doctor.getStatus())
                .createdDate(doctor.getCreatedDate())
                .modifiedDate(doctor.getModifiedDate())
                .build();
    }
}