ALTER TABLE feedback DROP COLUMN IF EXISTS employee_id;
ALTER TABLE feedback ADD COLUMN employee_id INT;