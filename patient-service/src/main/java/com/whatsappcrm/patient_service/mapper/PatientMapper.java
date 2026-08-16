package com.whatsappcrm.patient_service.mapper;

import com.whatsappcrm.patient_service.dto.request.CreatePatientRequest;
import com.whatsappcrm.patient_service.dto.request.UpdatePatientRequest;
import com.whatsappcrm.patient_service.dto.response.PatientResponse;
import com.whatsappcrm.patient_service.entity.Patient;

public class PatientMapper {

    public static Patient toEntity(CreatePatientRequest request) {

        return Patient.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .mobileNumber(request.getMobileNumber())
                .email(request.getEmail())
                .dateOfBirth(request.getDateOfBirth())
                .gender(request.getGender())
                .bloodGroup(request.getBloodGroup())
                .address(request.getAddress())
                .city(request.getCity())
                .state(request.getState())
                .pinCode(request.getPinCode())
                .build();
    }

    public static void updateEntity(UpdatePatientRequest request,
                                    Patient patient) {

        patient.setFirstName(request.getFirstName());
        patient.setLastName(request.getLastName());
        patient.setMobileNumber(request.getMobileNumber());
        patient.setEmail(request.getEmail());
        patient.setDateOfBirth(request.getDateOfBirth());
        patient.setGender(request.getGender());
        patient.setBloodGroup(request.getBloodGroup());
        patient.setAddress(request.getAddress());
        patient.setCity(request.getCity());
        patient.setState(request.getState());
        patient.setPinCode(request.getPinCode());
    }

    public static PatientResponse toResponse(Patient patient) {

        return PatientResponse.builder()
                .id(patient.getId())
                .firstName(patient.getFirstName())
                .lastName(patient.getLastName())
                .mobileNumber(patient.getMobileNumber())
                .email(patient.getEmail())
                .dateOfBirth(patient.getDateOfBirth())
                .gender(patient.getGender())
                .bloodGroup(patient.getBloodGroup())
                .address(patient.getAddress())
                .city(patient.getCity())
                .state(patient.getState())
                .pinCode(patient.getPinCode())
                .build();
    }
}
