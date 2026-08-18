package com.whatsappcrm.patient_service.controller;


import com.whatsappcrm.patient_service.dto.request.CreatePatientRequest;
import com.whatsappcrm.patient_service.entity.Patient;
import com.whatsappcrm.patient_service.repository.PatientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class PatientControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PatientRepository patientRepository;

    @BeforeEach
    void setUp() {
        patientRepository.deleteAll();
    }

    @Test
    void createPatient_ShouldReturnCreatedPatient() throws Exception {

        CreatePatientRequest request = new CreatePatientRequest();

        request.setFirstName("Nitish");
        request.setLastName("Jain");
        request.setMobileNumber("9876543216");
        request.setEmail("nj@gmail.com");
        request.setGender("Male");
        request.setAddress("Wakad");
        request.setCity("Pune");
        request.setState("Maharashtra");
        request.setPinCode("411002");

        mockMvc.perform(
                        post("/api/patients")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.firstName").value("Nitish"))
                .andExpect(jsonPath("$.lastName").value("Jain"))
                .andExpect(jsonPath("$.mobileNumber")
                        .value("9876543216"));
    }

    @Test
    void getPatientById_ShouldReturnPatient() throws Exception {

        Patient patient = Patient.builder()
                .firstName("Ayush")
                .lastName("Gupta")
                .mobileNumber("9876543210")
                .email("ayush@example.com")
                .city("Pune")
                .state("Maharashtra")
                .pinCode("411001")
                .build();
        patient.setTenantId(1L);
        Patient savedPatient = patientRepository.save(patient);

        mockMvc.perform(
                        get("/api/patients/" + savedPatient.getId())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id")
                        .value(savedPatient.getId()))
                .andExpect(jsonPath("$.firstName")
                        .value("Ayush"))
                .andExpect(jsonPath("$.mobileNumber")
                        .value("9876543210"));
    }

    @Test
    void getPatientById_ShouldReturn404_WhenPatientDoesNotExist()
            throws Exception {

        mockMvc.perform(
                        get("/api/patients/99999")
                )
                .andExpect(status().isNotFound());
    }

    @Test
    void deletePatient_ShouldSoftDeletePatient()
            throws Exception {

        Patient patient = Patient.builder()
                .firstName("Ayush")
                .lastName("Gupta")
                .mobileNumber("9876543211")
                .build();
        patient.setTenantId(1L);
        Patient savedPatient = patientRepository.save(patient);

        mockMvc.perform(
                        delete("/api/patients/" + savedPatient.getId())
                )
                .andExpect(status().isNoContent());

        /*
         * The entity itself should now have deleted = true.
         */
        Patient deletedPatient =
                patientRepository.findByIdIncludingDeleted(
                        savedPatient.getId()
                ).orElseThrow();

        assertTrue(deletedPatient.isDeleted());
    }

    @Test
    void deletedPatient_ShouldNotBeReturnedByGetById()
            throws Exception {

        Patient patient = Patient.builder()
                .firstName("Ayush")
                .lastName("Gupta")
                .mobileNumber("9876543210")
                .build();
        patient.setTenantId(1L);
        Patient savedPatient = patientRepository.save(patient);

        mockMvc.perform(
                        delete("/api/patients/" + savedPatient.getId())
                )
                .andExpect(status().isNoContent());

        /*
         * @SQLRestriction("deleted = false") should
         * prevent this patient from being returned.
         */
        mockMvc.perform(
                        get("/api/patients/" + savedPatient.getId())
                )
                .andExpect(status().isNotFound());
    }
}
