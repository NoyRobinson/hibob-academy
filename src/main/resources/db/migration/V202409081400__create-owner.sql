CREATE TABLE owner
(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name varchar(255),
    company_id UUID,
    employee_id UUID
);

CREATE INDEX idx_owner_company_id ON owner(company_id);
CREATE INDEX idx_owner_employee_id ON owner(employee_id);