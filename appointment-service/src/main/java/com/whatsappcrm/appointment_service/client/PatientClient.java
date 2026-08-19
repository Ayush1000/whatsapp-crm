package com.whatsappcrm.appointment_service.client;

import com.whatsappcrm.appointment_service.exception.PatientNotFoundException;

import org.springframework.beans.factory.annotation.Qualifier;

import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;


@Component
public class PatientClient {

    private final RestClient patientRestClient;

    public PatientClient(
            @Qualifier("patientRestClient")
                    RestClient patientRestClient) {

        this.patientRestClient = patientRestClient;
    }
    public void validatePatientExists(Long patientId) {

        patientRestClient
                .get()
                .uri("/api/patients/{id}", patientId)
                .retrieve()
                .onStatus(
                        HttpStatusCode::is4xxClientError,
                        (request, response) -> {

                            if (response.getStatusCode().value() == 404) {
                                throw new PatientNotFoundException(
                                        "Patient not found with id "
                                                + patientId
                                );
                            }
                        }
                )
                .toBodilessEntity();
    }
}