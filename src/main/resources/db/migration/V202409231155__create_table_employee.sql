CREATE TABLE employee (
    id SERIAL PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    role VARCHAR(255) NOT NULL,
    department VARCHAR(255) NOT NULL,
    company_id INT NOT NULL
);

CREATE INDEX idx_companyId_employeeId ON employee(company_id, id);
