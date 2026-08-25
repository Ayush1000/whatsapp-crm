ALTER TABLE appointments
ADD COLUMN appointment_type VARCHAR(30);

UPDATE appointments
SET appointment_type = 'SCHEDULED'
WHERE appointment_type IS NULL;

ALTER TABLE appointments
ALTER COLUMN appointment_type SET NOT NULL;


ALTER TABLE appointments
ADD COLUMN availability_override BOOLEAN NOT NULL DEFAULT FALSE;

ALTER TABLE appointments
ADD COLUMN override_reason VARCHAR(500);

ALTER TABLE appointments
ADD COLUMN checked_in_at TIMESTAMP;

ALTER TABLE appointments
ADD COLUMN actual_start_at TIMESTAMP;

ALTER TABLE appointments
ADD COLUMN actual_end_at TIMESTAMP;

ALTER TABLE appointments
ADD COLUMN starts_follow_up_window BOOLEAN NOT NULL DEFAULT FALSE;