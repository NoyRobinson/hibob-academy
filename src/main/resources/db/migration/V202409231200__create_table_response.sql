CREATE TABLE response (
      id SERIAL PRIMARY KEY,
      feedback_id INT NOT NULL,
      date_of_response DATE DEFAULT current_date,
      reviewer_id INT NOT NULL,
      response VARCHAR(255) NOT NULL
);





