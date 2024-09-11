DROP INDEX IF EXISTS idx_owner_companyId_employeeId;

CREATE UNIQUE INDEX IF NOT EXISTS idx_owner_companyId_employeeId ON owner(company_id, employee_id);