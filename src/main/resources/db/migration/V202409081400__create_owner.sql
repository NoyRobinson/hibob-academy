CREATE TABLE owner
(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name varchar(255),
    company_id UUID,
    employee_id UUID
);

CREATE INDEX idx_owner ON owner(company_id, employee_id);