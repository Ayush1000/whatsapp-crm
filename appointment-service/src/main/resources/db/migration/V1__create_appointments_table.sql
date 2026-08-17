CREATE TABLE appointments (
    id BIGSERIAL PRIMARY KEY,

    tenant_id BIGINT NOT NULL,

    patient_id BIGINT NOT NULL,

    doctor_id BIGINT NOT NULL,

    appointment_date DATE NOT NULL,

    start_time TIME NOT NULL,

    end_time TIME NOT NULL,

    status VARCHAR(30) NOT NULL,

    reason VARCHAR(500),

    notes TEXT,

    created_date TIMESTAMP NOT NULL,

    modified_date TIMESTAMP NOT NULL,

    created_by VARCHAR(255),

    updated_by VARCHAR(255),

    deleted BOOLEAN NOT NULL DEFAULT FALSE,

    version INTEGER
);

CREATE INDEX idx_appointment_tenant
    ON appointments(tenant_id);

CREATE INDEX idx_appointment_tenant_patient
    ON appointments(tenant_id, patient_id);

CREATE INDEX idx_appointment_tenant_doctor_date
    ON appointments(tenant_id, doctor_id, appointment_date);