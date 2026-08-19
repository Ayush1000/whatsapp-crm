package com.whatsappcrm.doctor_service.exception;

public class ResourceAlreadyExistsException
        extends RuntimeException {

    public ResourceAlreadyExistsException(String message) {
        super(message);
    }
}