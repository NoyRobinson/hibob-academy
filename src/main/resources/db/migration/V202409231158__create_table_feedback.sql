CREATE TABLE feedback (
    id SERIAL PRIMARY KEY,
    employee_id INT NOT NULL,
    company_id INT NOT NULL,
    date_of_feedback DATE DEFAULT current_date,
    anonymity VARCHAR(255) NOT NULL,
    reviewed BOOLEAN NOT NULL DEFAULT false,
    feedback VARCHAR(255) NOT NULL,
    response_id INT[]
);





