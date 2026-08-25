ALTER TABLE appointments
ADD COLUMN visit_type VARCHAR(30);

UPDATE appointments
SET visit_type = 'NEW_CONSULTATION'
WHERE visit_type IS NULL;

ALTER TABLE appointments
ALTER COLUMN visit_type SET NOT NULL;


ALTER TABLE appointments
ADD COLUMN consultation_fee NUMERIC(10,2);

UPDATE appointments
SET consultation_fee = 0
WHERE consultation_fee IS NULL;

ALTER TABLE appointments
ALTER COLUMN consultation_fee SET NOT NULL;