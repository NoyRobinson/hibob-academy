CREATE INDEX IF NOT EXISTS idx_companyId_employeeId ON feedback(company_id, employee_id);

CREATE INDEX IF NOT EXISTS idx_feedbackId ON response(feedback_id);