CREATE TABLE patients (
    id BIGSERIAL PRIMARY KEY,

    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255),

    mobile_number VARCHAR(20) NOT NULL UNIQUE,

    email VARCHAR(255),
    date_of_birth DATE,
    gender VARCHAR(50),
    blood_group VARCHAR(20),

    address VARCHAR(500),
    city VARCHAR(100),
    state VARCHAR(100),
    pin_code VARCHAR(20),

    active BOOLEAN NOT NULL DEFAULT TRUE,
    deleted BOOLEAN NOT NULL DEFAULT FALSE,

    created_date TIMESTAMP NOT NULL,
    modified_date TIMESTAMP NOT NULL,

    created_by VARCHAR(255),
    updated_by VARCHAR(255),

    version INTEGER
);