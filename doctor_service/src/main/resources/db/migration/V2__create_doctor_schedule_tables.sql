CREATE TABLE doctor_schedules (
    id BIGSERIAL PRIMARY KEY,

    tenant_id BIGINT NOT NULL,
    doctor_id BIGINT NOT NULL,

    day_of_week VARCHAR(20) NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,

    active BOOLEAN NOT NULL DEFAULT TRUE,

    created_date TIMESTAMP NOT NULL,
    modified_date TIMESTAMP NOT NULL,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    deleted BOOLEAN NOT NULL DEFAULT FALSE,
    version INTEGER,

    CONSTRAINT fk_schedule_doctor
        FOREIGN KEY (doctor_id)
        REFERENCES doctors(id)
);


CREATE TABLE doctor_leaves (
    id BIGSERIAL PRIMARY KEY,

    tenant_id BIGINT NOT NULL,
    doctor_id BIGINT NOT NULL,

    leave_date DATE NOT NULL,
    start_time TIME,
    end_time TIME,

    reason VARCHAR(255),

    created_date TIMESTAMP NOT NULL,
    modified_date TIMESTAMP NOT NULL,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    deleted BOOLEAN NOT NULL DEFAULT FALSE,
    version INTEGER,

    CONSTRAINT fk_leave_doctor
        FOREIGN KEY (doctor_id)
        REFERENCES doctors(id)
);


CREATE TABLE clinic_holidays (
    id BIGSERIAL PRIMARY KEY,

    tenant_id BIGINT NOT NULL,

    holiday_date DATE NOT NULL,
    description VARCHAR(255),

    created_date TIMESTAMP NOT NULL,
    modified_date TIMESTAMP NOT NULL,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    deleted BOOLEAN NOT NULL DEFAULT FALSE,
    version INTEGER
);


CREATE INDEX idx_doctor_schedule_lookup
ON doctor_schedules(
    tenant_id,
    doctor_id,
    day_of_week
);

CREATE INDEX idx_doctor_leave_lookup
ON doctor_leaves(
    tenant_id,
    doctor_id,
    leave_date
);

CREATE INDEX idx_clinic_holiday
ON clinic_holidays(
    tenant_id,
    holiday_date
);