CREATE TABLE owner
(
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name varchar(255) NOT NULL ,
    company_id BIGINT NOT NULL ,
    employee_id varchar(255) NOT NULL
);

CREATE UNIQUE INDEX idx_owner_companyId_employeeId ON owner(company_id, employee_id);