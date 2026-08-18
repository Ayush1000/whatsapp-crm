package com.whatsappcrm.appointment_service.controller;


import com.whatsappcrm.appointment_service.client.PatientClient;
import com.whatsappcrm.appointment_service.dto.request.CreateAppointmentRequest;
import com.whatsappcrm.appointment_service.dto.request.UpdateAppointmentRequest;
import com.whatsappcrm.appointment_service.entity.Appointment;
import com.whatsappcrm.appointment_service.enums.AppointmentStatus;
import com.whatsappcrm.appointment_service.repository.AppointmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AppointmentControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @MockitoBean
    private PatientClient patientClient;

    @BeforeEach
    void setUp() {
        appointmentRepository.deleteAll();

        // Every patient is considered valid unless a test says otherwise
        doNothing()
                .when(patientClient)
                .validatePatientExists(anyLong());
    }

    private CreateAppointmentRequest createRequest(
            long patientId,
            long doctorId,
            LocalTime startTime,
            LocalTime endTime) {

        CreateAppointmentRequest request =
                new CreateAppointmentRequest();

        request.setPatientId(patientId);
        request.setDoctorId(doctorId);
        request.setAppointmentDate(
                LocalDate.of(2026, 8, 20)
        );
        request.setStartTime(startTime);
        request.setEndTime(endTime);
        request.setReason("Consultation");

        return request;
    }

    private Appointment saveAppointment(
            long patientId,
            long doctorId,
            LocalTime start,
            LocalTime end) {

        Appointment appointment = new Appointment();

        appointment.setTenantId(1L);
        appointment.setPatientId(patientId);
        appointment.setDoctorId(doctorId);
        appointment.setAppointmentDate(
                LocalDate.of(2026, 8, 20)
        );
        appointment.setStartTime(start);
        appointment.setEndTime(end);
        appointment.setStatus(AppointmentStatus.BOOKED);

        return appointmentRepository.save(appointment);
    }

    @Test
    void createAppointment_ShouldReturnCreatedAppointment()
            throws Exception {

        CreateAppointmentRequest request =
                createRequest(
                        1L,
                        10L,
                        LocalTime.of(10, 0),
                        LocalTime.of(10, 30)
                );

        mockMvc.perform(
                        post("/api/appointments")
                                .contentType("application/json")
                                .content(
                                        objectMapper.writeValueAsString(request)
                                )
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.patientId").value(1))
                .andExpect(jsonPath("$.doctorId").value(10))
                .andExpect(jsonPath("$.status").value("BOOKED"));
    }

    @Test
    void getAppointmentById_ShouldReturnAppointment()
            throws Exception {

        Appointment appointment =
                saveAppointment(
                        1L,
                        10L,
                        LocalTime.of(10, 0),
                        LocalTime.of(10, 30)
                );

        mockMvc.perform(
                        get("/api/appointments/" + appointment.getId())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(appointment.getId()))
                .andExpect(jsonPath("$.patientId").value(1))
                .andExpect(jsonPath("$.doctorId").value(10));
    }

    @Test
    void getAppointmentById_ShouldReturn404_WhenNotFound()
            throws Exception {

        mockMvc.perform(
                        get("/api/appointments/99999")
                )
                .andExpect(status().isNotFound());
    }

    @Test
    void createAppointment_ShouldRejectOverlappingAppointment()
            throws Exception {

        saveAppointment(
                1L,
                10L,
                LocalTime.of(10, 0),
                LocalTime.of(10, 30)
        );

        CreateAppointmentRequest request =
                createRequest(
                        2L,
                        10L,
                        LocalTime.of(10, 15),
                        LocalTime.of(10, 45)
                );

        mockMvc.perform(
                        post("/api/appointments")
                                .contentType("application/json")
                                .content(
                                        objectMapper.writeValueAsString(request)
                                )
                )
                .andExpect(status().isConflict());
    }

    @Test
    void createAppointment_ShouldAllowBackToBackAppointment()
            throws Exception {

        saveAppointment(
                1L,
                10L,
                LocalTime.of(10, 0),
                LocalTime.of(10, 30)
        );

        CreateAppointmentRequest request =
                createRequest(
                        2L,
                        10L,
                        LocalTime.of(10, 30),
                        LocalTime.of(11, 0)
                );

        mockMvc.perform(
                        post("/api/appointments")
                                .contentType("application/json")
                                .content(
                                        objectMapper.writeValueAsString(request)
                                )
                )
                .andExpect(status().isOk());
    }

    @Test
    void updateAppointment_ShouldUpdateAppointment()
            throws Exception {

        Appointment appointment =
                saveAppointment(
                        1L,
                        10L,
                        LocalTime.of(10, 0),
                        LocalTime.of(10, 30)
                );

        UpdateAppointmentRequest request =
                new UpdateAppointmentRequest();

        request.setDoctorId(10L);
        request.setAppointmentDate(
                LocalDate.of(2026, 8, 20)
        );
        request.setStartTime(
                LocalTime.of(11, 0)
        );
        request.setEndTime(
                LocalTime.of(11, 30)
        );

        mockMvc.perform(
                        put("/api/appointments/" + appointment.getId())
                                .contentType("application/json")
                                .content(
                                        objectMapper.writeValueAsString(request)
                                )
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.startTime").value("11:00:00"));
    }

    @Test
    void updateAppointment_ShouldRejectConflictingSlot()
            throws Exception {

        Appointment first =
                saveAppointment(
                        1L,
                        10L,
                        LocalTime.of(10, 0),
                        LocalTime.of(10, 30)
                );

        Appointment second =
                saveAppointment(
                        2L,
                        10L,
                        LocalTime.of(10, 30),
                        LocalTime.of(11, 0)
                );

        UpdateAppointmentRequest request =
                new UpdateAppointmentRequest();

        request.setDoctorId(10L);
        request.setAppointmentDate(
                LocalDate.of(2026, 8, 20)
        );
        request.setStartTime(
                LocalTime.of(10, 15)
        );
        request.setEndTime(
                LocalTime.of(10, 45)
        );

        mockMvc.perform(
                        put("/api/appointments/" + second.getId())
                                .contentType("application/json")
                                .content(
                                        objectMapper.writeValueAsString(request)
                                )
                )
                .andExpect(status().isConflict());
    }

    @Test
    void cancelAppointment_ShouldCancelAppointment()
            throws Exception {

        Appointment appointment =
                saveAppointment(
                        1L,
                        10L,
                        LocalTime.of(10, 0),
                        LocalTime.of(10, 30)
                );

        mockMvc.perform(
                        delete("/api/appointments/" + appointment.getId())
                )
                .andExpect(status().isNoContent());

        Appointment updated =
                appointmentRepository
                        .findById(appointment.getId())
                        .orElseThrow();

        org.junit.jupiter.api.Assertions.assertEquals(
                AppointmentStatus.CANCELLED,
                updated.getStatus()
        );
    }
}