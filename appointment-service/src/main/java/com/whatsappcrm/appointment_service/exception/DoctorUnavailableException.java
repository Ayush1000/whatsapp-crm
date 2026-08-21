package com.whatsappcrm.appointment_service.exception;


public class DoctorUnavailableException extends RuntimeException {

    public DoctorUnavailableException(String message) {
        super(message);
    }
}