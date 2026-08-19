CREATE TABLE doctors (
    id BIGSERIAL PRIMARY KEY,

    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100),

    mobile_number VARCHAR(20),
    email VARCHAR(255),

    specialization VARCHAR(50) NOT NULL,

    registration_number VARCHAR(100) NOT NULL UNIQUE,

    qualification VARCHAR(255),
    experience_years INTEGER,

    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',

    created_date TIMESTAMP NOT NULL,
    modified_date TIMESTAMP NOT NULL,

    created_by VARCHAR(255),
    updated_by VARCHAR(255),

    deleted BOOLEAN NOT NULL DEFAULT FALSE,

    version INTEGER
);


CREATE TABLE doctor_clinics (
    id BIGSERIAL PRIMARY KEY,

    tenant_id BIGINT NOT NULL,

    doctor_id BIGINT NOT NULL,

    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',

    default_appointment_duration_minutes INTEGER DEFAULT 30,

    created_date TIMESTAMP NOT NULL,
    modified_date TIMESTAMP NOT NULL,

    created_by VARCHAR(255),
    updated_by VARCHAR(255),

    deleted BOOLEAN NOT NULL DEFAULT FALSE,

    version INTEGER,

    CONSTRAINT fk_doctor_clinic_doctor
        FOREIGN KEY (doctor_id)
        REFERENCES doctors(id),

    CONSTRAINT uk_doctor_clinic
        UNIQUE (tenant_id, doctor_id)
);


CREATE TABLE consultation_policies (
    id BIGSERIAL PRIMARY KEY,

    tenant_id BIGINT NOT NULL,

    doctor_id BIGINT NOT NULL,

    consultation_fee NUMERIC(10,2) NOT NULL,

    free_follow_up_days INTEGER NOT NULL DEFAULT 0,

    report_review_free BOOLEAN NOT NULL DEFAULT TRUE,

    follow_up_fee NUMERIC(10,2),

    created_date TIMESTAMP NOT NULL,
    modified_date TIMESTAMP NOT NULL,

    created_by VARCHAR(255),
    updated_by VARCHAR(255),

    deleted BOOLEAN NOT NULL DEFAULT FALSE,

    version INTEGER,

    CONSTRAINT fk_policy_doctor
        FOREIGN KEY (doctor_id)
        REFERENCES doctors(id),

    CONSTRAINT uk_policy_doctor_tenant
        UNIQUE (tenant_id, doctor_id)
);


CREATE INDEX idx_doctor_clinic_tenant
ON doctor_clinics(tenant_id);

CREATE INDEX idx_doctor_clinic_doctor
ON doctor_clinics(doctor_id);

CREATE INDEX idx_policy_tenant_doctor
ON consultation_policies(tenant_id, doctor_id);