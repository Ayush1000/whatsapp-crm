package com.whatsappcrm.appointment_service.client;

import com.whatsappcrm.appointment_service.exception.PatientNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Component
@RequiredArgsConstructor
public class PatientClient {

    private final RestClient patientRestClient;

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