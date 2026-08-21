package com.whatsappcrm.appointment_service.client;

import com.whatsappcrm.appointment_service.dto.response.DoctorAvailabilityResponse;
import com.whatsappcrm.appointment_service.exception.DoctorNotFoundException;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.time.LocalDate;
import java.time.LocalTime;

@Component
public class DoctorClient {

    private final RestClient doctorRestClient;
    public DoctorClient(
            @Qualifier("doctorRestClient")
                    RestClient doctorRestClient) {

        this.doctorRestClient = doctorRestClient;
    }
    public void validateDoctorExists(Long doctorId) {

        doctorRestClient
                .get()
                .uri("/api/doctors/{id}", doctorId)
                .retrieve()
                .onStatus(
                        status -> status.value() == 404,
                        (request, response) -> {
                            throw new DoctorNotFoundException(
                                    "Doctor not found for this clinic with id "
                                            + doctorId
                            );
                        }
                )
                .toBodilessEntity();
    }

    public DoctorAvailabilityResponse checkAvailability(
            Long doctorId,
            LocalDate date,
            LocalTime startTime,
            LocalTime endTime) {

        return doctorRestClient
                .get()
                .uri(uriBuilder ->
                        uriBuilder
                                .path("/api/schedules/availability/doctor/{doctorId}")
                                .queryParam("date", date)
                                .queryParam("startTime", startTime)
                                .queryParam("endTime", endTime)
                                .build(doctorId)
                )
                .retrieve()
                .onStatus(
                        status -> status.value() == 404,
                        (request, response) -> {
                            throw new DoctorNotFoundException(
                                    "Doctor not found for this clinic with id "
                                            + doctorId
                            );
                        }
                )
                .body(DoctorAvailabilityResponse.class);
    }
}