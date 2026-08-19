package com.whatsappcrm.appointment_service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Bean
    public RestClient patientRestClient(
            @Value("${patient-service.url}") String patientServiceUrl) {

        return RestClient.builder()
                .baseUrl(patientServiceUrl)
                .build();
    }

    @Bean
    public RestClient doctorRestClient(
            @Value("${doctor-service.url}") String doctorServiceUrl) {

        return RestClient.builder()
                .baseUrl(doctorServiceUrl)
                .build();
    }
}