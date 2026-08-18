ALTER TABLE patients
ADD COLUMN tenant_id BIGINT;

UPDATE patients
SET tenant_id = 1
WHERE tenant_id IS NULL;

ALTER TABLE patients
ALTER COLUMN tenant_id SET NOT NULL;

CREATE INDEX idx_patient_tenant
ON patients (tenant_id);