CREATE TABLE owner
(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name varchar(255),
    company_id BIGINT,
    employee_id String
);

CREATE INDEX idx_owner_companyId_employeeId ON owner(company_id, employee_id);